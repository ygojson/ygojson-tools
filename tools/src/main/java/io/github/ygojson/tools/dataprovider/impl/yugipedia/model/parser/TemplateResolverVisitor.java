package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.parser;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import org.sweble.wikitext.parser.nodes.*;

import java.text.MessageFormat;
import java.util.List;

public class TemplateResolverVisitor extends AstVisitor<WtNode> {

	private static final String HTML_RUBY_MESSAGE_FORMAT =
		"<ruby>{0}<rt>{1}</rt></ruby>";

	private final YugipediaWikitextParser parser;
	private final boolean expandArgs;

	private final WikitextNodeFactoryImpl nodeFactory;

	// TODO: remove in favor of always true!
	TemplateResolverVisitor(final YugipediaWikitextParser parser) {
		this(parser, true);
	}

	TemplateResolverVisitor(final YugipediaWikitextParser parser, final boolean expandArgs) {
		this.parser = parser;
		this.expandArgs = expandArgs;

		this.nodeFactory = new WikitextNodeFactoryImpl(parser.getConfig());
	}

	/**
	 * Visit all child nodes of this node.
	 *
	 * @param n the node to visit.
	 */
	public void visit(final AstNode<WtNode> n) {
		final int size = n.size();
		for (int i = 0; i < size; i++) {
			// sometimes the transformation might join together String nodes
			// and thus the size might be changed
			// thus, skip those cases here
			if (i >= n.size()) {
				break;
			}
			// safeguard because if transformed, the size might be changed
			final WtNode child = n.get(i);
			boolean transformed = false;
			if (child instanceof WtTemplate wtTemplate) {
				final WtNode transform = resolveTemplate(wtTemplate);
				if (transform != null) {
					n.set(i, transform);
					transformed = true;
				}
			}
			if (!transformed) {
				dispatch(child);
			}
		}
	}

	/**
	 * Visit the template that was not transformed
	 *
	 * @param wtTemplate template that was not transformed
	 */
	public void visit(final WtTemplate wtTemplate) {
		if (expandArgs) {
			final List<WtTemplateArgument> argList = wtTemplate.getArgs().stream()
				.map(arg -> expandArg((WtTemplateArgument) arg))
				.toList();
			final WtTemplateArguments arguments = nodeFactory.tmplArgs(nodeFactory.list(argList));
			wtTemplate.setArgs(arguments);
		}
	}

	private WtTemplateArgument expandArg(final WtTemplateArgument arg) {
		final String argString = parser.convertToWikitext(arg.getValue());
		final WtParsedWikitextPage processed = parser.rawParse(argString);
		if (processed == null) {
			return arg;
		}
		final WtValue value = nodeFactory.value(processed);
		return nodeFactory.tmplArg(arg.getName(), value);
	}

	private WtNode resolveTemplate(WtTemplate wtTemplate) {
		if (isRubyTemplate(wtTemplate)) {
			return resolveRubyTemplate(
				(WtTemplateArgument) wtTemplate.getArgs().get(0),
				(WtTemplateArgument) wtTemplate.getArgs().get(1)
			);
		}
		return null;
	}

	private boolean isRubyTemplate(final WtTemplate template) {
		return template.getName().getAsString().equalsIgnoreCase("ruby");
	}

	private WtNode resolveRubyTemplate(WtTemplateArgument kanjiNode, WtTemplateArgument furiganaNode) {
		final String rubyHtml = MessageFormat.format(HTML_RUBY_MESSAGE_FORMAT,
			parser.convertToWikitext(kanjiNode.getValue()),
			parser.convertToWikitext(furiganaNode.getValue())
		);
		return nodeFactory.text(rubyHtml);
	}

}
