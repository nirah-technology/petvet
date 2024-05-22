package io.nirahtech.petvet.ai.classifiers;

import java.io.BufferedInputStream;
import java.util.Optional;

interface Classifier {

    /**
     * Try to classify something inside a given source.
     * @param sourceToClassify Source that contains an element to classify.
     * @return The classified data.
     * @throws ClassificationException Error occured during the classification process.
     */
    Optional<String> classify(final BufferedInputStream sourceToClassify) throws ClassificationException;
}
