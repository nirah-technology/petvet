package io.nirahtech.petvet.simulator.electronicalcard.gui.widgets;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class JZoomPanel extends JPanel {
    private static final int CROSS_WIDTH = 20;
    private static final int CROSS_HEIGHT = CROSS_WIDTH;

    private final JButton zoomOutButton;  
    private final JButton zoomInButton;
    
    public JZoomPanel() {
        super();

        this.setOpaque(false);

        this.zoomOutButton = new JButton();
        this.zoomInButton = new JButton();

        BufferedImage originalImage = loadImage("zoom-in.png");
        Image resizedImage = resizeImage(originalImage, CROSS_WIDTH, CROSS_HEIGHT);
        this.zoomInButton.setIcon(new ImageIcon(resizedImage));


        originalImage = loadImage("zoom-out.png");
        resizedImage = resizeImage(originalImage, CROSS_WIDTH, CROSS_HEIGHT);
        this.zoomOutButton.setIcon(new ImageIcon(resizedImage));


        this.setLayout(new GridLayout(2, 1));

        this.add(this.zoomOutButton);
        this.add(this.zoomInButton);

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

    public void setOnZoomInButtonPressedEventListener(final Runnable eventListener) {
        this.zoomInButton.addActionListener(event -> {
            if (Objects.nonNull(eventListener)) {
                eventListener.run();
            }
        });
    }
    
    public void setOnZoomOutButtonPressedEventListener(final Runnable eventListener) {
        this.zoomOutButton.addActionListener(event -> {
            if (Objects.nonNull(eventListener)) {
                eventListener.run();
            }
        });
    }
    
}
