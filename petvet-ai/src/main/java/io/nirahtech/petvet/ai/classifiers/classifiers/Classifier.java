package io.nirahtech.petvet.ai.classifiers.classifiers;

import java.io.BufferedInputStream;
import java.util.Optional;

import io.nirahtech.petvet.ai.classifiers.classification.Classification;
import io.nirahtech.petvet.ai.classifiers.exceptions.ClassificationException;

interface Classifier<T extends Classification> {

    /**
     * Try to classify something inside a given source.
     * @param sourceToClassify Source that contains an element to classify.
     * @return The classified data.
     * @throws ClassificationException Error occured during the classification process.
     */
    Optional<T> classify(final BufferedInputStream sourceToClassify) throws ClassificationException;
}
