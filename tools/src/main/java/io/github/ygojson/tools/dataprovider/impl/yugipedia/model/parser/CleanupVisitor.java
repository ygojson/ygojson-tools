package io.github.ygojson.tools.dataprovider.impl.yugipedia.model.parser;

import de.fau.cs.osr.ptk.common.AstVisitor;
import de.fau.cs.osr.ptk.common.ast.AstNode;
import de.fau.cs.osr.ptk.common.ast.AstText;
import org.sweble.wikitext.parser.nodes.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Visitor to cleanup the wikitext nodes already parsed.
 */
public class CleanupVisitor extends AstVisitor<WtNode> {

	//private final WikitextStringConverter converter = new WikitextStringConverter();
	private final YugipediaWikitextParser parser;
	private List<Object> cleanupParts;
	// private StringBuilder sb;

	private CleanupVisitor(final YugipediaWikitextParser parser) {
		this.parser = parser;
	}

	@Override
	public WtNode before(WtNode node) {
		// sb = new StringBuilder();
		cleanupParts = new ArrayList<>();
		return node;
	}

	@Override
	protected Object after(WtNode node, Object result) {
		//final String wikitext = sb.toString();
		// TODO: cleanup resources?
		return cleanupParts;
	}

	/**
	 * Gets the node as a String without the considered markup.
	 *
	 * @param node the node to cleanup.
	 *
	 * @return the String node.
	 * @param <R> the type of the node.
	 */
	public static <R extends WtNode> List<Object> getAsObjectList(final R node, final YugipediaWikitextParser parser) {
		final CleanupVisitor visitor = new CleanupVisitor(parser);
		return (List<Object>) visitor.go(node);
	}

	/**
	 * Visit all child nodes of this node.
	 *
	 * @param n the node to visit.
	 */
	public void visit(AstNode<WtNode> n) {
		n.forEach(this::dispatch);
	}

	/**
	 * Cleanup internal links
	 *
	 * @param internalLink internal link to cleanup
	 */
	public void visit(WtInternalLink internalLink) {
		// cleanup the internal link
		final String linkValue = internalLink.getTarget().getAsString();
		//sb.append(linkValue);
		cleanupParts.add(linkValue);
	}

	public void visit(WtOrderedList orderedList) {
		doVisitList(orderedList.stream(), "ol");
	}

	public void visit(WtUnorderedList unorderedList) {
		doVisitList(unorderedList.stream(), "ul");
	}

	// TODO: this is interesting
	private void doVisitList(final Stream<WtNode> nodes, final String marker) {
		final List<Object> items = nodes.map(item -> getAsObjectList(item, parser)) // recursive expansion
			.flatMap(l -> l.stream())
			.toList();
		cleanupParts.add(items);
		//sb.append("<").append(marker).append(">");
		//items.forEach(s -> sb.append("<li>").append(s).append("</li>"));
		//sb.append("</").append(marker).append(">");
	}

	/**
	 * Convert as-is the rest of the text nodes.
	 *
	 * @param n text nodes
	 */
	public void visit(AstText<WtNode> n) {
		final String content = parser.convertToWikitext((WtNode) n);
		cleanupParts.add(content);
		//sb.append(content);
	}

	/**
	 * Convert the node into the new-line representation.
	 *
	 * @param n the node.
	 */
	public void visit(WtNewline n) {
		cleanupParts.add("\n");
		//sb.append("\n");
	}
}
