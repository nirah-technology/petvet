package io.nirahtech.petvet.simulator.electronicalcard.gui.widgets;

import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicReference;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class JCrossDirectionPanel extends JPanel {

    private static final int MOVE_TASK_INTERVAL = 50;

    private static final int CROSS_WIDTH = 20;
    private static final int CROSS_HEIGHT = CROSS_WIDTH;

    private final JButton topButton;  
    private final JButton rightButton;  
    private final JButton bottomButton;  
    private final JButton leftButton;
    
    public JCrossDirectionPanel() {
        super();

        this.setOpaque(false);

        this.topButton = new JButton();
        this.rightButton = new JButton();
        this.bottomButton = new JButton();
        this.leftButton = new JButton();

        BufferedImage originalImage = loadImage("top-arrow.png");
        Image resizedImage = resizeImage(originalImage, CROSS_WIDTH, CROSS_HEIGHT);
        this.topButton.setIcon(new ImageIcon(resizedImage));


        originalImage = loadImage("right-arrow.png");
        resizedImage = resizeImage(originalImage, CROSS_WIDTH, CROSS_HEIGHT);
        this.rightButton.setIcon(new ImageIcon(resizedImage));


        originalImage = loadImage("bottom-arrow.png");
        resizedImage = resizeImage(originalImage, CROSS_WIDTH, CROSS_HEIGHT);
        this.bottomButton.setIcon(new ImageIcon(resizedImage));


        originalImage = loadImage("left-arrow.png");
        resizedImage = resizeImage(originalImage, CROSS_WIDTH, CROSS_HEIGHT);
        this.leftButton.setIcon(new ImageIcon(resizedImage));


        this.setLayout(new GridLayout(3, 3));

        this.add(new JLabel());      // Cellule vide (0, 0)
        this.add(topButton);         // Cellule (0, 1)
        this.add(new JLabel());      // Cellule vide (0, 2)
        this.add(leftButton);        // Cellule (1, 0)
        this.add(new JLabel());      // Cellule vide (1, 1)
        this.add(rightButton);       // Cellule (1, 2)
        this.add(new JLabel());      // Cellule vide (2, 0)
        this.add(bottomButton);      // Cellule (2, 1)
        this.add(new JLabel());      // Cellule vide (2, 2)

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

    public void setOnTopButtonPressedEventListener(final Runnable eventListener) {
        final AtomicReference<Timer> timer = new AtomicReference<>();
        this.topButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                timer.set(new Timer());
                timer.get().scheduleAtFixedRate(new MoveTask(eventListener), 0, MOVE_TASK_INTERVAL);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                timer.get().cancel();
            }
        });
    }
    
    public void setOnRightButtonPressedEventListener(final Runnable eventListener) {
        final AtomicReference<Timer> timer = new AtomicReference<>();
        this.rightButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                timer.set(new Timer());
                timer.get().scheduleAtFixedRate(new MoveTask(eventListener), 0, MOVE_TASK_INTERVAL);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                timer.get().cancel();
            }
        });
    }
    
    public void setOnLeftButtonPressedEventListener(final Runnable eventListener) {
        final AtomicReference<Timer> timer = new AtomicReference<>();
        this.leftButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                timer.set(new Timer());
                timer.get().scheduleAtFixedRate(new MoveTask(eventListener), 0, MOVE_TASK_INTERVAL);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                timer.get().cancel();
            }
        });
    }
    
    public void setOnBottomButtonPressedEventListener(final Runnable eventListener) {
        final AtomicReference<Timer> timer = new AtomicReference<>();
        this.bottomButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent event) {
                timer.set(new Timer());
                timer.get().scheduleAtFixedRate(new MoveTask(eventListener), 0, MOVE_TASK_INTERVAL);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                timer.get().cancel();
            }
        });
    }

    private final class MoveTask extends TimerTask {

        private final Runnable moveTaskToRun;

        private MoveTask(final Runnable moveTaskToRun) {
            this.moveTaskToRun = moveTaskToRun;
        }

        @Override
        public void run() {
            if (Objects.nonNull(this.moveTaskToRun)) {
                moveTaskToRun.run();
            }
        }

    }
}
