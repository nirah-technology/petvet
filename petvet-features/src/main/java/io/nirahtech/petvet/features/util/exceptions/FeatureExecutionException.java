package io.nirahtech.petvet.features.util.exceptions;

public final class FeatureExecutionException extends Exception {

    public FeatureExecutionException() {
    }

    public FeatureExecutionException(String message) {
        super(message);
    }

    public FeatureExecutionException(Throwable cause) {
        super(cause);
    }

    public FeatureExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FeatureExecutionException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
