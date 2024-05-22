package io.nirahtech.petvet.ai.classifiers.detectors;

import java.util.Optional;

public final class CatAnimalDetector implements AnimalDetector {

    private static final String CLASSIFICATION = "Cat";
    private static final byte[] PATTERN = {0x42, 0x4D};

    @Override
    public String getClassification() {
        return CLASSIFICATION;
    }

    @Override
    public Optional<String> detect(byte[] image) {
        Optional<String> classification = Optional.empty();
        int[] lps = computeLPS(PATTERN);
    
        int i = 0; // index in image[]
        int j = 0; // index in PATTERN[]
        while (i < image.length) {
            if (PATTERN[j] == image[i]) {
                i++;
                j++;
            }
            if (j == PATTERN.length) {
                classification = Optional.of(getClassification());
                break;
            }
            if (i < image.length && PATTERN[j] != image[i]) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }
        return classification;
    }
    
    // Compute the Longest Prefix Suffix array for the given pattern
    private int[] computeLPS(byte[] pattern) {
        int[] lps = new int[pattern.length];
        int len = 0; // length of the previous longest prefix suffix
        int i = 1;
        while (i < pattern.length) {
            if (pattern[i] == pattern[len]) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }
    
}
