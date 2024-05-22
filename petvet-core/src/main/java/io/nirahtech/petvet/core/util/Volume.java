package io.nirahtech.petvet.core.util;

/**
 * Immutable class representing volume with various unit conversions.
 */
public final class Volume {
    private final double volumeInMilliLiters;
    
    /**
     * Private constructor to initialize volume in milliliters.
     * 
     * @param volumeInMilliLiters The volume in milliliters.
     */
    private Volume(final double volumeInMilliLiters) {
        if (volumeInMilliLiters < 0) {
            throw new IllegalArgumentException("Volume cannot be negative");
        }
        this.volumeInMilliLiters = volumeInMilliLiters;
    }

    // Accessor methods for different units
    public final double ml() {
        return this.volumeInMilliLiters;
    }

    public final double cl() {
        return this.volumeInMilliLiters / 10;
    }

    public final double dl() {
        return this.volumeInMilliLiters / 100;
    }

    public final double l() {
        return this.volumeInMilliLiters / 1_000;
    }

    public final double dal() {
        return this.volumeInMilliLiters / 10_000;
    }
    
    public final double hl() {
        return this.volumeInMilliLiters / 100_000;
    }

    public final double kl() {
        return this.volumeInMilliLiters / 1_000_000;
    }

    // Static factory methods for creating instances
    public static final Volume ml(final double milliLiters) {
        return new Volume(milliLiters);
    }

    public static final Volume cl(final double centiLiters) {
        return new Volume(centiLiters * 10);        
    }

    public static final Volume dl(final double deciLiters) {
        return new Volume(deciLiters * 100);
    }

    public static final Volume l(final double liters) {
        return new Volume(liters * 1_000);
    }

    public static final Volume dal(final double decaLiters) {
        return new Volume(decaLiters * 10_000);
    }

    public static final Volume hl(final double hectoLiters) {
        return new Volume(hectoLiters * 100_000);
    }

    public static final Volume kl(final double kiloLiters) {
        return new Volume(kiloLiters * 1_000_000);
    }
}
