package io.nirahtech.petvet.core;

import io.nirahtech.petvet.core.animalpark.AnimalIdentifier;
import io.nirahtech.petvet.core.base.HouseIdentifier;
import io.nirahtech.petvet.core.util.identifier.Identifier;

public class ProgramTest {
    public static void main(String[] args) {
        AnimalIdentifier id = AnimalIdentifier.generate();
    }
}
