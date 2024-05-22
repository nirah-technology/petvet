package io.nirahtech.petvet.ai.classifiers.detectors;

import java.util.Optional;

import io.nirahtech.petvet.ai.classifiers.classification.Classification;
import io.nirahtech.petvet.ai.classifiers.exceptions.ClassificationException;

public interface Detector<T extends Classification> {
    Optional<T> detect(byte[] image) throws ClassificationException;
}
