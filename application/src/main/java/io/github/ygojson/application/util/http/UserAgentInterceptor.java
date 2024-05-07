package io.github.ygojson.application.util.http;

import java.io.IOException;
import java.util.function.Function;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class UserAgentInterceptor implements Interceptor {

	private static final String USER_AGENT_HEADER_NAME = "User-Agent";

	// using a function to support:
	// 1. custom user-agents overrides
	// 2. provide a rotating user-agent
	// 3. include the default user-agent (if used on network interceptor)
	private final Function<String, String> userAgentSupplier;

	public UserAgentInterceptor(
		final Function<String, String> userAgentSupplier
	) {
		this.userAgentSupplier = userAgentSupplier;
	}

	@Override
	public Response intercept(final Chain chain) throws IOException {
		final Request original = chain.request();
		final String originalHeader = original.header(USER_AGENT_HEADER_NAME);
		final Request request = original
			.newBuilder()
			.header(USER_AGENT_HEADER_NAME, userAgentSupplier.apply(originalHeader))
			.method(original.method(), original.body())
			.build();
		return chain.proceed(request);
	}
}
