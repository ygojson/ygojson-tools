package io.github.ygojson.tools.dataprovider.impl.yugipedia.playground;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.ygojson.model.utils.JsonUtils;
import io.github.ygojson.tools.common.ApplicationInfo;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaApi;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.YugipediaConfig;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.model.api.params.Limit;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.FetchDataProcessor;
import io.github.ygojson.tools.dataprovider.impl.yugipedia.processor.pagination.YugipediaStreamFactory;
import io.github.ygojson.tools.dataprovider.utils.client.ClientFactory;

public class CardsPlayground {

	final Logger log = LoggerFactory.getLogger(this.getClass());

	@Test
	public void test() {
		// TODO: get API
		final ClientFactory clientFactory = new ClientFactory(
			JsonUtils.getObjectMapper(),
			new ApplicationInfo("ygojson", "0.0.1-test","https://github.com/ygojson/ygojson-tools"));
		final YugipediaApi api = clientFactory.getClient(new YugipediaConfig());
		final YugipediaStreamFactory streamFactory = new YugipediaStreamFactory(api);
		final FetchDataProcessor processor = new FetchDataProcessor();

		final AtomicLong requests = new AtomicLong(0);
		final AtomicLong processedPrints = new AtomicLong(0);
		log.info("Fetching prints...");
		final long nCards = streamFactory.ofCards(Limit.getMax())
			.peek(response -> {
				final long n = requests.incrementAndGet();
				if (n % 10 == 0) {
					log.info("Performed {} requests", n);
				}
			})
			.flatMap(processor::processQuery)
			.peek(printData -> {
				final long n = processedPrints.incrementAndGet();
				if (n % 1_0000 == 0) {
					log.info("Processed {} prints", n, processedPrints.get());
				}
			})
			.count();

		log.info("Found {} prints in total ({} requests performed)", nCards, requests.get());
	}
}
