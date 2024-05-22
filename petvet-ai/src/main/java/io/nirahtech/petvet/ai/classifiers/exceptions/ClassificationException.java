package io.nirahtech.petvet.ai.classifiers.exceptions;

public class ClassificationException extends Exception {
    public ClassificationException(final String message) {
        super(message);
    }
    public ClassificationException(final Throwable throwable) {
        super(throwable);
    }
}
