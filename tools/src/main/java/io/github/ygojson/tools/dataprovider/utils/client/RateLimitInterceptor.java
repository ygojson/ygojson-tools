package io.github.ygojson.tools.dataprovider.utils.client;

import java.io.IOException;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

class RateLimitInterceptor implements Interceptor {

	private final RateLimiter rateLimiter;

	public RateLimitInterceptor(final RateLimiter rateLimiter) {
		this.rateLimiter = rateLimiter;
	}

	@Override
	public Response intercept(final Chain chain) throws IOException {
		try {
			return RateLimiter
				.decorateCheckedSupplier(
					rateLimiter,
					() -> chain.proceed(chain.request())
				)
				.get();
		} catch (final Throwable e) {
			return handleFailure(e);
		}
	}

	public Response handleFailure(Throwable throwable) throws IOException {
		return switch (throwable) {
			case RequestNotPermitted rnp -> tooManyRequestResponse();
			case IllegalStateException ise -> tooManyRequestResponse();
			case IOException ioe -> throw ioe;
			default -> throw new RuntimeException(
				"call::execute exception",
				throwable
			);
		};
	}

	private Response tooManyRequestResponse() {
		return new Response.Builder()
			.code(429)
			.body(
				ResponseBody.create(
					"Too many client requests",
					MediaType.get("text/plain")
				)
			)
			.build();
	}
}
