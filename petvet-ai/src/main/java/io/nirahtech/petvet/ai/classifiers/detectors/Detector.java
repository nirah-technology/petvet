package io.nirahtech.petvet.ai.classifiers.detectors;

import java.util.Optional;

public interface Detector {
    String getClassification();
    Optional<String> detect(byte[] image);
}
