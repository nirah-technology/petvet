package io.nirahtech.petvet.argparse;

public record Argument(
    String longParameterName,
    String shortParameterName,
    String description,
    boolean isRequired,
    boolean isValueRequired
) { 
    
}
