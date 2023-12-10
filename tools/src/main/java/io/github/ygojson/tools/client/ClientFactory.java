package io.github.ygojson.tools.client;

import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import io.github.ygojson.tools.common.ApplicationInfo;

@RequiredArgsConstructor
public class ClientFactory {

	private final ConcurrentHashMap<ClientConfig<?>, Retrofit> retrofitClients =
		new ConcurrentHashMap<>();

	private final ObjectMapper jsonMapper;
	private final ApplicationInfo info;

	public <T> T getClient(ClientConfig<T> config) {
		final Retrofit retrofit = retrofitClients.computeIfAbsent(
			config,
			this::createRetrofit
		);
		return retrofit.create(config.apiClass());
	}

	private Retrofit createRetrofit(ClientConfig<?> config) {
		return new Retrofit.Builder()
			.baseUrl(config.baseUrl())
			.client(createHttpClient(config))
			.addConverterFactory(ScalarsConverterFactory.create())
			.addConverterFactory(JacksonConverterFactory.create(jsonMapper))
			.build();
	}

	private OkHttpClient createHttpClient(ClientConfig<?> config) {
		return new OkHttpClient.Builder()
			.addInterceptor(
				createRateLimitInterceptor(config.name(), config.rateLimit())
			)
			.addInterceptor(new UserAgentInterceptor(config.userAgentMapper(info)))
			.build();
	}

	private static RateLimitInterceptor createRateLimitInterceptor(
		String name,
		ClientConfig.RateLimit rateLimit
	) {
		final RateLimiterConfig rateLimitConfig = RateLimiterConfig
			.custom()
			.limitForPeriod(rateLimit.maxRequest())
			.limitRefreshPeriod(rateLimit.duration())
			.timeoutDuration(rateLimit.duration())
			.build();
		final RateLimiter rateLimiter = RateLimiterRegistry
			.of(rateLimitConfig)
			.rateLimiter(name);
		return new RateLimitInterceptor(rateLimiter);
	}
}
