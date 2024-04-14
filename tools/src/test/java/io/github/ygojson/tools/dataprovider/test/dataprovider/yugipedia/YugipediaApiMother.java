package io.github.ygojson.tools.dataprovider.test.dataprovider.yugipedia;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import io.github.ygojson.application.yugipedia.client.YugipediaClient;
import io.github.ygojson.model.utils.serialization.JsonUtils;

public class YugipediaApiMother {

	private YugipediaApiMother() {
		// utility class
	}

	public static YugipediaClient productionTestClient() {
		return createClient("https://yugipedia.com", productionOkHttp());
	}

	public static YugipediaClient mockTestClient(final String mockedUrl) {
		return createClient(mockedUrl, new OkHttpClient.Builder().build());
	}

	private static OkHttpClient productionOkHttp() {
		final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
		interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
		return new OkHttpClient.Builder()
			.addInterceptor(interceptor)
			.addInterceptor(WAIT_2_SECONDS_ON_PRODUCTION)
			.build();
	}

	private static YugipediaClient createClient(
		final String baseUrl,
		final OkHttpClient okHttpClient
	) {
		final Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(baseUrl)
			.client(okHttpClient)
			.addConverterFactory(ScalarsConverterFactory.create())
			.addConverterFactory(
				JacksonConverterFactory.create(JsonUtils.getObjectMapper())
			)
			.build();
		return retrofit.create(YugipediaClient.class);
	}

	private static final Interceptor WAIT_2_SECONDS_ON_PRODUCTION = chain -> {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return chain.proceed(chain.request());
	};
}
