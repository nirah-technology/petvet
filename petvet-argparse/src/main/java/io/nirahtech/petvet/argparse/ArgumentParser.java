package io.nirahtech.petvet.argparse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class ArgumentParser {
    private static final String SIMPLE_DASH = "-";
    private static final String DOUBLE_DASH = "--";

    private final Map<Argument, Object> softwareArguments = new HashMap<>();
    private final Map<String, String> userArguments = new HashMap<>();

    public ArgumentParser() {
        this.add("help", "h", DOUBLE_DASH, false, false);
    }

    public final void add(final String longParameterName, final String shortParameterName, final String description,
            final boolean isRequired, final boolean isValueRequired) {
        final Argument argument = new Argument(longParameterName, shortParameterName, description, isRequired,
                isValueRequired);
        this.softwareArguments.put(argument, null);
    }

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

    private final void mapMissingAliasSoftwareArgumentsToUserArguments() {
        final Map<String, String> aliasesToAdd = new HashMap<>();
        this.userArguments.entrySet().forEach(userArgument -> {
            this.softwareArguments.keySet().forEach(softwareArgument -> {
                if (softwareArgument.longParameterName().equals(userArgument.getKey())
                        || softwareArgument.shortParameterName().equals(userArgument.getKey())) {
                    aliasesToAdd.put(softwareArgument.longParameterName(), userArgument.getValue());
                    aliasesToAdd.put(softwareArgument.shortParameterName(), userArgument.getValue());
                }
            });
        });

        this.userArguments.putAll(aliasesToAdd);
    }

    private final Collection<String> containsUnexpectedUserArguments() {
        return this.userArguments
                .keySet()
                .stream()
                .filter(userParameter -> this.softwareArguments
                        .keySet()
                        .stream()
                        .noneMatch(argument -> argument.longParameterName().equals(userParameter)
                                || argument.shortParameterName().equals(userParameter)))
                .collect(Collectors.toList());
    }

    private final Collection<String> retrieveMissingParameters() {
        final Collection<String> missingParameters = new ArrayList<>();
        for (Argument argument : this.softwareArguments.keySet()) {
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

    private final Collection<String> retrieveMissingRequiredValuesForParameters() {
        final Collection<String> missingValues = new ArrayList<>();
        for (Argument argument : this.softwareArguments.keySet()) {
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

    public final void parse(final String[] commandLineArguments) throws Exception {
        this.retrieveUserParameters(commandLineArguments);
        this.mapMissingAliasSoftwareArgumentsToUserArguments();
        Collection<String> unexpectedParameters = this.containsUnexpectedUserArguments();
        if (!unexpectedParameters.isEmpty()) {
            final String parametersAsString = unexpectedParameters.stream().collect(Collectors.joining(", "));
            throw new Exception("Some unexpected user argument was given: " + parametersAsString);
        }
        Collection<String> missingParameters = this.retrieveMissingParameters();
        if (!missingParameters.isEmpty()) {
            final String parametersAsString = missingParameters.stream().collect(Collectors.joining(", "));
            throw new Exception("Some required software arguments are missing: " + parametersAsString);
        }

        Collection<String> missingValues = this.retrieveMissingRequiredValuesForParameters();
        if (!missingValues.isEmpty()) {
            final String parametersAsString = missingValues.stream().collect(Collectors.joining(", "));
            throw new Exception("Some required values are missing for parameters: " + parametersAsString);
        }
    }

    public final String getHelp() {
        StringBuilder helpMessage = new StringBuilder();
        helpMessage.append("Usage:\n");

        for (Argument argument : this.softwareArguments.keySet()) {
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

    public final Optional<String> get(final String shortOrLongParameterName) {
        System.out.println(shortOrLongParameterName);
        System.out.println(this.userArguments);
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
