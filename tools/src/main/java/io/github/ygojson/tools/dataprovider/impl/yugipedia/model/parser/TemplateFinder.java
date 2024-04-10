package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.parser;

import org.sweble.wikitext.parser.nodes.WtNode;
import org.sweble.wikitext.parser.nodes.WtTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TemplateFinder {

	private TemplateFinder() {
		//NO-OP
	}

    public static List<WtTemplate> getAllTemplates(WtNode node) {
		final List<WtTemplate> templates = new ArrayList<>();
		collectTemplates(node, templates);
		return templates;
	}

	public static Optional<WtTemplate> findFirstTemplate(final WtNode node, final String templateName) {
		if (node instanceof WtTemplate template) {
			if (templateName.equals(template.getName().getAsString().trim())) {
				return Optional.of(template);
			}

		}
		return node.stream()
			.map(n -> findFirstTemplate(n, templateName))
			.filter(Optional::isPresent)
			.findFirst()
			.orElse(Optional.empty());
	}

	public static void collectTemplates(WtNode n, final List<WtTemplate> collector) {
		if (n instanceof WtTemplate template) {
			collector.add(template);
		}
		n.forEach(child -> collectTemplates(child, collector));
	}

}
