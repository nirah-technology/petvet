package io.nirahtech.petvet.ai.classifiers.classifiers;

public final class ClassifierFactory {
    private ClassifierFactory() { }

    public static final PlantClassifier plantClassifier() {
        return DefaultPlantClassifierImpl.getInstance();
    }

    public static final AnimalClassifier animalClassifier() {
        return DefaultAnimalClassifierImpl.getInstance();
    }
}
