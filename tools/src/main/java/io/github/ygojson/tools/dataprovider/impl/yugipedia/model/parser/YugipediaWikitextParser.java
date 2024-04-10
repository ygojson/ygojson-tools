package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.parser;

import de.fau.cs.osr.ptk.common.ParserCommon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sweble.wikitext.parser.ParserConfig;
import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.utils.AstTextUtils;
import org.sweble.wikitext.parser.utils.NonExpandingParser;
import xtc.parser.ParseException;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class YugipediaWikitextParser {

	private final Logger log = LoggerFactory.getLogger(this.getClass());
	private final ParserCommon<WtNode> parser;

	public YugipediaWikitextParser() {
		this.parser = createParser();
		// add the template resolver that we care about
		parser.addVisitor(new TemplateResolverVisitor(this));
	}

	private static ParserCommon<WtNode> createParser() {
		// TODO: which one should we use?
		final NonExpandingParser parser = new NonExpandingParser(true, false, false);
		// final NonPostproParser parser = new NonPostproParser(true, false, false);
		//final WikitextParser parser = new WikitextParser(new NonExpandingParserConfig(true, false, false));
		return parser;
	}

	public ParsedWikitext parse(final String title, final String wikitext) {
		if (wikitext == null) {
			// TODO: better to return null?
			return new ParsedWikitext(title, null, this);
		}
		try {
			// assume that the parser returns a page
			final WtParsedWikitextPage page = (WtParsedWikitextPage) parser.parseArticle(wikitext, title);
			return new ParsedWikitext(title, page, this);
		} catch (final IOException | ParseException e) {
			log.error("Error parsing wikitext", e);
			// TODO: better to return null?
			return new ParsedWikitext(title, null, this);
		}
	}

	WtParsedWikitextPage rawParse(final String wikitext) {
		try {
			// assume that the parser returns a page
			return (WtParsedWikitextPage) parser.parseArticle(wikitext, "raw");
		} catch (final IOException | ParseException e) {
			log.error("Error parsing wikitext", e);
			// TODO: better to return null?
			return null;
		}
	}

	ParserConfig getConfig() {
		return (ParserConfig) parser.getConfig();
	}

	String convertToWikitext(WtNode node) {
		final AstTextUtils.PartialConversion conversion = getConfig().getAstTextUtils().astToTextPartial(node);
		// TODO: should we also consider the tail?
		final String text = conversion.getText();
		// TODO: also remove start-of-string whitespaces?
		return text.trim();
	}

	String cleanupWikitext(WtNode node) {
		return cleanup(node).stream()
			.map(s -> {
				return switch (s) {
					case String str -> str;
					case List l -> String.join("<list-delim>", l);
					default -> Objects.toString(s);
				};
			})
			.collect(Collectors.joining(" "));
	}

	List<Object> cleanup(WtNode node) {
		return CleanupVisitor.getAsObjectList(node, this);
	}

}
