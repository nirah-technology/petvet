package io.nirahtech.petvet.core.util;

public final class Weight {
    private final double weightInMilliGrams;
    
    private Weight(final double weightInMilliGrams) {
        if (weightInMilliGrams < 0) {
            throw new IllegalArgumentException("Weight cannot be negative");
        }
        this.weightInMilliGrams = weightInMilliGrams;
    }

    public final double mg() {
        return this.weightInMilliGrams;
    }

    public final double cg() {
        return this.weightInMilliGrams / 10;
    }

    public final double dg() {
        return this.weightInMilliGrams / 100;
    }

    public final double g() {
        return this.weightInMilliGrams / 1_000;
    }

    public final double dag() {
        return this.weightInMilliGrams / 10_000;
    }
    public final double hg() {
        return this.weightInMilliGrams / 100_000;
    }

    public final double kg() {
        return this.weightInMilliGrams / 1_000_000;
    }

    public static final Weight mg(final double milliGrams) {
        return new Weight(milliGrams);
    }

    public static final Weight cg(final double centiGrams) {
        return new Weight(centiGrams * 10);       
    }

    public static final Weight dg(final double deciGrams) {
        return new Weight(deciGrams * 100);  
    }

    public static final Weight g(final double grams) {
        return new Weight(grams * 1_000);  
    }

    public static final Weight dag(final double decaGrams) {
        return new Weight(decaGrams * 10_000);        
    }
    public static final Weight hg(final double hectoGrams) {
        return new Weight(hectoGrams * 100_000);
    }
    public static final Weight kg(final double kiloGrams) {
        return new Weight(kiloGrams * 1_000_000);
    }
}
