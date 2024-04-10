package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.parser;


import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.utils.WtPrettyPrinter;

import java.io.StringWriter;

/**
 * Helper class to convert a node on the parser into a String.
 */
class WikitextStringConverter {

	private final StringWriter writer;
	private final WtPrettyPrinter printer;

	public WikitextStringConverter() {
		this.writer = new StringWriter();
		// TODO: the WtPrettyPrinter uses a PrintWriter under the hood
		// TODO: which messes up the end-of-line in Windows
		// TODO: thus, we should use a different way to convert to string here...
		this.printer = new WtPrettyPrinter(writer);
	}

	public  String toString(WtNode node) {
		printer.go(node);
		final String stringValue = writer.toString().trim();
		// reset the writer
		writer.getBuffer().setLength(0);
		return stringValue;
	}

}
