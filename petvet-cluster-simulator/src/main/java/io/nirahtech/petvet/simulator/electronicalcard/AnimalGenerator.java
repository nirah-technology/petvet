package io.nirahtech.petvet.simulator.electronicalcard;

import java.util.Random;

public final class AnimalGenerator {
    private static final Random random = new Random();

    private AnimalGenerator() {
    }

    private static float generateSize(float minSizeInCM, float maxSizeInCM) {
        return minSizeInCM + random.nextFloat() * (maxSizeInCM - minSizeInCM);
    }

    private static String generateSpecie(String[] species) {
        int index = random.nextInt(species.length);
        return species[index];
    }

    public static Animal generateCommonPet() {
        String[] species = { "Cat", "Dog" };
        float minSizeInCM = 10.0F; // Taille minimale arbitraire pour chats et chiens
        float maxSizeInCM = 80.0F; // Taille maximale arbitraire pour chats et chiens
        float size = generateSize(minSizeInCM, maxSizeInCM);
        return new Animal(size, generateSpecie(species));
    }

    private static Animal generateNewPet() {
        String[] species = { "Ferret", "Chinchilla", "Hedgehog", "Rat", "Mouse", "Gerbil", "Octodon",
                "African pygmy hedgehog", "Dwarf goat", "Parrot", "Budgie", "Cockatoo", "Canary", "Finch",
                "Dove", "Snake", "Lizard", "Gecko", "Land turtle", "Aquatic turtle", "Iguana", "Frog",
                "Salamander", "Axolotl", "Spider", "Scorpio", "Fish" };
        float minSizeInCM = 5.0F; // Taille minimale arbitraire pour NAC
        float maxSizeInCM = 30.0F; // Taille maximale arbitraire pour NAC
        float size = generateSize(minSizeInCM, maxSizeInCM);
        return new Animal(size, generateSpecie(species));
    }

    private static Animal generateShortBassAnimal() {
        String[] species = { "Hen", "Rooster", "Duck", "Goose", "Turkey" };
        float minSizeInCM = 15.0F; // Taille minimale arbitraire pour animaux de basse-cour
        float maxSizeInCM = 60.0F; // Taille maximale arbitraire pour animaux de basse-cour
        float size = generateSize(minSizeInCM, maxSizeInCM);
        return new Animal(size, generateSpecie(species));
    }

    private static Animal generateFarmAnimal() {
        String[] species = { "Cow", "Bull", "Sheep", "Goat", "Horse", "Donkey", "Pony", "Pig" };
        float minSizeInCM = 100.0F; // Taille minimale arbitraire pour animaux de ferme
        float maxSizeInCM = 300.0F; // Taille maximale arbitraire pour animaux de ferme
        float size = generateSize(minSizeInCM, maxSizeInCM);
        return new Animal(size, generateSpecie(species));
    }

    public static Animal generate() {
        double categoryChange = random.nextDouble() * 100; // Générer un nombre aléatoire entre 0 et 100
        Animal animal;
        if (categoryChange < 5) {
            animal = generateFarmAnimal();
        } else if (categoryChange < 10) {
            animal = generateShortBassAnimal();
        } else if (categoryChange < 30) {
            animal = generateNewPet();
        } else {
            animal = generateCommonPet();
        }
        return animal;
    }
}
