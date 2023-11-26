package io.github.ygojson.tools.common;

import lombok.experimental.StandardException;

/**
 * Runtime exception for the YGOJSON tools.
 */
@StandardException
public class YgoJsonToolException extends RuntimeException {

    /**
     * Exception thrown when the inputs of a tool are incorrect.
     */
    @StandardException
    public static final class InputException extends YgoJsonToolException { }

}
