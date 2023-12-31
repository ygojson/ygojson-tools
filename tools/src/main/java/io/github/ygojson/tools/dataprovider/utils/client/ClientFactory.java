package io.github.ygojson.tools.dataprovider.utils.client;

import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import io.github.ygojson.tools.common.ApplicationInfo;

public class ClientFactory {

	private final ConcurrentHashMap<String, Retrofit> retrofitClients =
		new ConcurrentHashMap<>();

	private final ObjectMapper jsonMapper;
	private final ApplicationInfo info;

	public ClientFactory(
		final ObjectMapper jsonMapper,
		final ApplicationInfo info
	) {
		this.jsonMapper = jsonMapper;
		this.info = info;
	}

	public <T> T getClient(ClientConfig<T> config) {
		final Retrofit retrofit = retrofitClients.computeIfAbsent(
			config.name(),
			any -> createRetrofit(config)
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
			.addNetworkInterceptor(
				createRateLimitInterceptor(config.name(), config.rateLimit())
			)
			.addNetworkInterceptor(
				new UserAgentInterceptor(config.userAgentMapper(info))
			)
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
