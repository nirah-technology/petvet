package io.nirahtech.petvet.simulator.cadastre.gui;

import java.awt.Dimension;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public final class WidgetUtils {
    private WidgetUtils() { }


    public static final JButton createSmallButton(final String text, final String iconName) {
        final JButton button = new JButton();
        button.setToolTipText(text);
        setButtonIcon(button, iconName);
        return button;
    }

    public static final void setButtonIcon(final JButton button, final String iconName) {
        URL iconUrl = WidgetUtils.class.getClassLoader().getResource("icons/" + iconName);
        if (iconUrl != null) {

            int desiredWidth = 20;
            int desiredHeight = desiredWidth;

            final Dimension dimension = new Dimension(desiredWidth + 10, desiredHeight + 10);
            button.setPreferredSize(dimension);
            button.setSize(dimension);

            // Redimensionner l'image
            final ImageIcon imageIcon = new ImageIcon(iconUrl);
            final Image originalImage = imageIcon.getImage();
            final Image resizedImage = originalImage.getScaledInstance(desiredWidth, desiredHeight,
                    Image.SCALE_SMOOTH);
            final ImageIcon resizedIcon = new ImageIcon(resizedImage);
            button.setIcon(resizedIcon);
        }
    }
}
