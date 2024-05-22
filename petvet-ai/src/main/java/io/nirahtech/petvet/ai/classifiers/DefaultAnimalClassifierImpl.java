package io.nirahtech.petvet.ai.classifiers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import io.nirahtech.petvet.ai.classifiers.detectors.AnimalDetector;
import io.nirahtech.petvet.ai.classifiers.detectors.CatAnimalDetector;

public final class DefaultAnimalClassifierImpl implements AnimalClassifier {

    private static AnimalClassifier instance;

    /**
     * @return the instance
     */
    static final AnimalClassifier getInstance() {
        if (Objects.isNull(instance)) {
            instance = new DefaultAnimalClassifierImpl();
        }
        return instance;
    }

    private final Set<AnimalDetector> animalsDetectors;

    private DefaultAnimalClassifierImpl() {
        this.animalsDetectors = Set.of(
                new CatAnimalDetector()
        );
    }

    private Optional<String> detectAnimal(final byte[] image) throws IOException {
        return this.animalsDetectors.stream()
                .flatMap(detector -> detector.detect(image).stream())
                .findFirst();
    }

    @Override
    public Optional<String> classify(final BufferedInputStream sourceToClassify) throws ClassificationException {
        try {
            final byte[] image = sourceToClassify.readAllBytes();
            return this.detectAnimal(image);
        } catch (IOException e) {
            throw new ClassificationException(e);
        }
    }

}
