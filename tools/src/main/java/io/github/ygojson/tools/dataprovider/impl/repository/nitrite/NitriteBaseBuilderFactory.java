package io.github.ygojson.tools.dataprovider.impl.repository.nitrite;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.NitriteBuilder;
import org.dizitart.no2.common.mapper.JacksonMapperModule;

public class NitriteBaseBuilderFactory {

	private NitriteBaseBuilderFactory() {
		// NO-OP
	}


	public static final NitriteBuilder create() {
		return Nitrite.builder()
			.loadModule(new JacksonMapperModule(
				new JavaTimeModule() // include to store the lastModifiedTime on the database
			));
	}

}
