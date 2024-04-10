package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.parser;

import org.sweble.wikitext.parser.nodes.WtParsedWikitextPage;
import org.sweble.wikitext.parser.nodes.WtTemplate;
import org.sweble.wikitext.parser.nodes.WtTemplateArgument;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a parsed wikitext.
 */
public class ParsedWikitext {

	private final String title;
	private final WtParsedWikitextPage page;
	private final YugipediaWikitextParser parser;
	//private final WikitextStringConverter converter;

	ParsedWikitext(final String title, final WtParsedWikitextPage page, final YugipediaWikitextParser parser) {
		this.title = title;
		this.page = page;
		this.parser = parser;
		//System.out.println(WtAstPrinter.print(page));// TODO: remove as this is debug info
		 //this.converter = new WikitextStringConverter();
	}

	public Map<String, Object> getTemplateMap2(final String templateName) {
		if (page == null) {
			// TODO: return null instead?
			return Map.of();
		}
		return TemplateFinder.findFirstTemplate(page, templateName)
			.map(this::templateNodeToMap)
			.orElseGet(Map::of); // TODO: return null instead?
	}

	public Map<String, String> getTemplateMap(final String templateName) {
		if (page == null) {
			// TODO: return null instead?
			return Map.of();
		}
		return TemplateFinder.findFirstTemplate(page, templateName)
			.map(this::templateNodeToMapString)
			.orElseGet(Map::of); // TODO: return null instead?
	}

	private Map<String, String> templateNodeToMapString(final WtTemplate template) {
		return template.getArgs().stream()
			.map(arg -> (WtTemplateArgument) arg)
			.collect(Collectors.toMap(
				// we don't expect that the name contains something to cleanup
				arg -> parser.convertToWikitext(arg.getName()),
				// we expect the arguments to have wikitext, internal links, etc
				arg ->  parser.cleanupWikitext(arg.getValue())));
	}

	private Map<String, Object> templateNodeToMap(final WtTemplate template) {
		return template.getArgs().stream()
			.map(arg -> (WtTemplateArgument) arg)
			.collect(Collectors.toMap(
				// we don't expect that the name contains something to cleanup
				arg -> parser.convertToWikitext(arg.getName()),
				// we expect the arguments to have wikitext, internal links, etc
				arg ->  {
					final List<Object> value = parser.cleanup(arg.getValue());
					return switch (value.size()) {
						case 1 -> value.getFirst();
						default -> value;
					};
				}));
	}

}
