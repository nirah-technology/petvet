package io.nirahtech.petvet.ai.classifiers.classifiers;

import java.io.BufferedInputStream;
import java.util.Objects;
import java.util.Optional;

import io.nirahtech.petvet.ai.classifiers.exceptions.ClassificationException;

public final class DefaultPlantClassifierImpl implements PlantClassifier {

    private static PlantClassifier instance;

    /**
     * @return the instance
     */
    static final PlantClassifier getInstance() {
        if (Objects.isNull(instance)) {
            instance = new DefaultPlantClassifierImpl();
        }
        return instance;
    }

    private DefaultPlantClassifierImpl() { }

    @Override
    public Optional<String> classify(final BufferedInputStream sourceToClassify) throws ClassificationException {
        // TODO Auto-generated method stub
        throw new ClassificationException("Unimplemented method 'classify'");
    }
    
}
