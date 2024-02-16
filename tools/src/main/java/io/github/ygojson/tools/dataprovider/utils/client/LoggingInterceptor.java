package io.github.ygojson.tools.dataprovider.utils.client;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class LoggingInterceptor implements Interceptor {

	private final Logger log;

	LoggingInterceptor(final Class<?> clientClass) {
		log = LoggerFactory.getLogger(clientClass);
	}


	@NotNull
	@Override
	public Response intercept(@NotNull Chain chain) throws IOException {
		log.info("{} {}", chain.request().method(), chain.request().url());
		return chain.proceed(chain.request());
	}
}
