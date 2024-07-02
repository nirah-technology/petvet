package io.nirahtech.argparse;

/**
 * Represents a command line argument with both a long and short name,
 * description, and flags indicating whether it is required and whether it
 * requires a value.
 * 
 * @author Nicolas METIVIER <nicolas.a.metivier@gmail.com>
 * @serial 202407020907
 * @since 1.0
 * @version 1.0.0
 */
record Argument(
    String longParameterName,
    String shortParameterName,
    String description,
    boolean isRequired,
    boolean isValueRequired
) { 
    
}
