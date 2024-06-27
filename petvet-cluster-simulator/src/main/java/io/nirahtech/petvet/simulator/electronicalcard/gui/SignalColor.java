package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.Color;

public final class SignalColor {

    private SignalColor() { }

    public static Color getSignalColor(float percentage) {
        if (percentage < 0) {
            percentage = 0;
        } else if (percentage > 100) {
            percentage = 100;
        }

        // Couleurs de début (rouge) et de fin (vert)
        Color startColor = new Color(255, 0, 0);        // 0%
        Color midColor = new Color(255, 255, 0);        // 50%
        Color endColor = new Color(0, 255, 0);          // 100%

        if (percentage <= 50) {
            return interpolateColor(startColor, midColor, percentage / 50.0);
        } else {
            return interpolateColor(midColor, endColor, (percentage - 50) / 50.0);
        }
    }

    private static Color interpolateColor(Color startColor, Color endColor, double fraction) {
        int red = (int) (startColor.getRed() + fraction * (endColor.getRed() - startColor.getRed()));
        int green = (int) (startColor.getGreen() + fraction * (endColor.getGreen() - startColor.getGreen()));
        int blue = (int) (startColor.getBlue() + fraction * (endColor.getBlue() - startColor.getBlue()));

        return new Color(red, green, blue);
    }

}
