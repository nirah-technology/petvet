package io.nirahtech.petvet.simulator.cadastre.gui.widgets;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class JWindRoseCompassPanel extends JPanel {
    private final JLabel label;

    public JWindRoseCompassPanel() {
        super();
        this.setLayout(new BorderLayout());
        this.setOpaque(false);
        this.label = new JLabel();

        BufferedImage originalImage = loadImage("wind-rose-compass.png");
        Image resizedImage = resizeImage(originalImage, 50, 50);
        this.label.setIcon(new ImageIcon(resizedImage));
        this.add(this.label, BorderLayout.CENTER);
    }


    private Image resizeImage(BufferedImage originalImage, int width, int height) {
        return originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
    }


    private BufferedImage loadImage(final String imageName) {
        final String imagePath = "/icons/"+imageName;
        BufferedImage image = null;
        try {
            URL resource = this.getClass().getResource(imagePath);
            if (resource != null) {
                image = ImageIO.read(resource);
            } else {
                throw new RuntimeException("Resource not found: " + imagePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return image;

    }
}
