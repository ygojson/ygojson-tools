package io.github.ygojson.tools.common;

/**
 * Common interface for a YGOJSON tool.
 *
 * @param <Input> input type
 */
public interface YgoJsonTool<Input> {
	/**
	 * Executes the tool.
	 *
	 * @param input input
	 */
	public void execute(Input input) throws YgoJsonToolException;
}
