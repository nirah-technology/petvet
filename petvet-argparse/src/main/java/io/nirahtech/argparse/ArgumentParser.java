package io.nirahtech.argparse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * ArgumentParser is a library that parses and manages command line arguments.
 * </p>
 * <p>
 * It facilitates the interpretation of parameters provided by the user when a
 * program is executed via a terminal.
 * </p>
 * 
 * @author Nicolas METIVIER <nicolas.a.metivier@gmail.com>
 * @serial 202407020907
 * @since 1.0
 * @version 1.0.0
 */
public final class ArgumentParser {
    private static final String SIMPLE_DASH = "-";
    private static final String DOUBLE_DASH = "--";

    private final Set<Argument> softwareArguments = new HashSet<>();
    private final Map<String, String> userArguments = new HashMap<>();

    private String description = null;

    /**
     * Default constructor that initializes the parser with a default help argument.
     */
    public ArgumentParser() {
        this.add("help", "h", "Display this help.", false, false);
    }

    /**
     * Returns the description of the ArgumentParser.
     * 
     * @return the description of the ArgumentParser.
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Sets the description of the ArgumentParser.
     * 
     * @param description the new description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Adds an expected argument to the parser.
     * 
     * @param longParameterName  the long name of the parameter.
     * @param shortParameterName the short name of the parameter.
     * @param description        the description of the parameter.
     * @param isRequired         whether the parameter is required.
     * @param isValueRequired    whether the parameter requires a value.
     */
    public final void add(final String longParameterName, final String shortParameterName, final String description,
            final boolean isRequired, final boolean isValueRequired) {
        final Argument argument = new Argument(longParameterName, shortParameterName, description, isRequired,
                isValueRequired);
        this.softwareArguments.add(argument);
    }

    /**
     * Retrieves user-provided parameters from the command line arguments.
     * 
     * @param commandLineArguments the command line arguments.
     */
    private final void retrieveUserParameters(final String[] commandLineArguments) {
        for (int index = 0; index < commandLineArguments.length; index++) {
            final String givenPropertyByUser = commandLineArguments[index];
            if (givenPropertyByUser.startsWith(SIMPLE_DASH) || givenPropertyByUser.startsWith(DOUBLE_DASH)) {
                final String givenPropertyName = givenPropertyByUser.startsWith(DOUBLE_DASH)
                        ? givenPropertyByUser.substring(2)
                        : givenPropertyByUser.substring(1);

                userArguments.put(givenPropertyName, null);

                final int nextIndex = index + 1;
                if (nextIndex < commandLineArguments.length) {
                    final String nextUserValue = commandLineArguments[nextIndex];
                    if (!nextUserValue.startsWith(SIMPLE_DASH)) {
                        userArguments.put(givenPropertyName, nextUserValue);
                        index++;
                    }
                }
            }
        }
    }

    /**
     * Maps missing alias software arguments to user arguments.
     */
    private final void mapMissingAliasSoftwareArgumentsToUserArguments() {
        final Map<String, String> aliasesToAdd = new HashMap<>();
        this.userArguments.entrySet().forEach(userArgument -> {
            this.softwareArguments.forEach(softwareArgument -> {
                if (softwareArgument.longParameterName().equals(userArgument.getKey())
                        || softwareArgument.shortParameterName().equals(userArgument.getKey())) {
                    aliasesToAdd.put(softwareArgument.longParameterName(), userArgument.getValue());
                    aliasesToAdd.put(softwareArgument.shortParameterName(), userArgument.getValue());
                }
            });
        });

        this.userArguments.putAll(aliasesToAdd);
    }

    /**
     * Checks for unexpected user arguments.
     * 
     * @return a collection of unexpected user arguments.
     */
    private final Collection<String> containsUnexpectedUserArguments() {
        return this.userArguments
                .keySet()
                .stream()
                .filter(userParameter -> this.softwareArguments
                        .stream()
                        .noneMatch(argument -> argument.longParameterName().equals(userParameter)
                                || argument.shortParameterName().equals(userParameter)))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves missing required parameters.
     * 
     * @return a collection of missing required parameters.
     */
    private final Collection<String> retrieveMissingParameters() {
        final Collection<String> missingParameters = new ArrayList<>();
        for (Argument argument : this.softwareArguments) {
            if (argument.isRequired()) {
                String requiredParameter = argument.longParameterName();
                if (Objects.nonNull(requiredParameter)) {
                    if (!this.userArguments.containsKey(requiredParameter)) {
                        requiredParameter = argument.shortParameterName();
                        if (!this.userArguments.containsKey(requiredParameter)) {
                            missingParameters.add(requiredParameter);
                        }
                    }
                } else {
                    requiredParameter = argument.shortParameterName();
                    if (Objects.nonNull(requiredParameter)) {
                        if (!this.userArguments.containsKey(requiredParameter)) {
                            missingParameters.add(requiredParameter);
                        }
                    }
                }
            }
        }
        return missingParameters;
    }

    /**
     * Retrieves missing required values for parameters.
     * 
     * @return a collection of parameters that are missing required values.
     */
    private final Collection<String> retrieveMissingRequiredValuesForParameters() {
        final Collection<String> missingValues = new ArrayList<>();
        for (Argument argument : this.softwareArguments) {
            if (argument.isValueRequired()) {
                final String longParameterName = argument.longParameterName();
                final String shortParameterName = argument.shortParameterName();
                if (this.userArguments.containsKey(longParameterName)) {
                    if (Objects.isNull(this.userArguments.get(longParameterName))) {
                        missingValues.add(longParameterName);
                    }
                } else if (this.userArguments.containsKey(shortParameterName)) {
                    if (Objects.isNull(this.userArguments.get(shortParameterName))) {
                        missingValues.add(shortParameterName);
                    }
                }
            }
        }
        return missingValues;
    }

    /**
     * Parses the command line arguments.
     * 
     * @param commandLineArguments the command line arguments.
     * @throws ParseException if there are unexpected user arguments, missing
     *                        required arguments, or missing required values.
     */
    public final void parse(final String[] commandLineArguments) throws ParseException {
        this.retrieveUserParameters(commandLineArguments);
        this.mapMissingAliasSoftwareArgumentsToUserArguments();
        Collection<String> unexpectedParameters = this.containsUnexpectedUserArguments();
        if (!unexpectedParameters.isEmpty()) {
            final String parametersAsString = unexpectedParameters.stream().collect(Collectors.joining(", "));
            throw new ParseException("Some unexpected user argument was given: " + parametersAsString);
        }
        Collection<String> missingParameters = this.retrieveMissingParameters();
        if (!missingParameters.isEmpty()) {
            final String parametersAsString = missingParameters.stream().collect(Collectors.joining(", "));
            throw new ParseException("Some required software arguments are missing: " + parametersAsString);
        }

        Collection<String> missingValues = this.retrieveMissingRequiredValuesForParameters();
        if (!missingValues.isEmpty()) {
            final String parametersAsString = missingValues.stream().collect(Collectors.joining(", "));
            throw new ParseException("Some required values are missing for parameters: " + parametersAsString);
        }
    }

    /**
     * Generates a help message listing all expected arguments.
     * 
     * @return a help message.
     */
    public final String getHelp() {
        StringBuilder helpMessage = new StringBuilder();
        if (Objects.nonNull(this.description) && !this.description.isEmpty()) {
            helpMessage.append(this.description).append("\n\n");
        }
        helpMessage.append("Usage:\n");

        for (Argument argument : this.softwareArguments) {
            helpMessage.append("  --")
                    .append(argument.longParameterName())
                    .append(", -")
                    .append(argument.shortParameterName())
                    .append("\n    ")
                    .append(argument.description())
                    .append(argument.isRequired() ? " (required)" : "")
                    .append(argument.isValueRequired() ? " <value>" : "")
                    .append("\n");
        }

        return helpMessage.toString();
    }

    /**
     * Retrieves the value associated with a given argument.
     * 
     * @param shortOrLongParameterName the short or long name of the parameter.
     * @return an Optional containing the value of the parameter if present,
     *         otherwise an empty Optional.
     */
    public final Optional<String> get(final String shortOrLongParameterName) {
        String parameterValue = null;
        Optional<String> argumentValueFound = this.userArguments
                .entrySet()
                .stream()
                .filter((userArgument) -> userArgument.getKey().equals(shortOrLongParameterName))
                .map(Map.Entry::getValue)
                .findFirst();

        if (argumentValueFound.isPresent()) {
            parameterValue = argumentValueFound.get();
        }
        return Optional.ofNullable(parameterValue);
    }
}
