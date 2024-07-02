package io.nirahtech.argparse;

/**
 * Exception thrown when an error occurs during the parsing of command line arguments.
 * 
 * @author Nicolas METIVIER <nicolas.a.metivier@gmail.com>
 * @serial 202407020907
 * @since 1.0
 * @version 1.0.0
 */
public final class ParseException extends Exception {

    /**
     * Constructs a new ParseException with {@code null} as its detail message.
     */
    public ParseException() {
        super();
    }

    /**
     * Constructs a new ParseException with the specified detail message.
     *
     * @param message the detail message.
     */
    public ParseException(final String message) {
        super(message);
    }

    /**
     * Constructs a new ParseException with the specified cause.
     *
     * @param reason the cause.
     */
    public ParseException(final Throwable reason) {
        super(reason);
    }
}
