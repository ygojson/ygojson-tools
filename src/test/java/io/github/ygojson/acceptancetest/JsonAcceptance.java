package io.github.ygojson.acceptancetest;

import org.approvaltests.Approvals;

public class JsonAcceptance {

	// TODO: change for the serialization to be done within the method
	public void verify(final String testCase, final Object asJsonString) {
		Approvals.verify(
			asJsonString,
			Approvals.NAMES
				.withParameters()
				.forFile()
				.withBaseName(testCase)
				.forFile()
				.withExtension(".json")
		);
	}
}
