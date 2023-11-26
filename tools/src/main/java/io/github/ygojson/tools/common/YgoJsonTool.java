package io.github.ygojson.tools.common;

import java.util.Optional;

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
	 * @return optional error;
	 *         {@link Optional#empty()} if no error occurs.
	 */
	public void execute(Input input) throws YgoJsonToolException;
}
