package io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URL;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class JLayerPanel extends JPanel {

    private Consumer<Layer> onSelectedLayerEventListerner = null;
    private Consumer<Layer> onLockChangedOnLayerEventListener = null;
    private Consumer<Layer> onVisibilityChangedOnLayerEventListener = null;
    

    private static final String HIDE_TEXT = "Hide";
    private static final String DISPLAY_TEXT = "Display";
    private static final String LOCK_TEXT = "Lock";
    private static final String UNLOCK_TEXT = "Unlock";

    private final Layer layer;

    private int topPadding = 5;
    private int leftPadding = 5;
    private int bottomPadding = 5;
    private int rightPadding = 5;

    private boolean isVisible = true;
    private boolean isLocked = false;


    final JButton visibilityButton;
    final JButton lockedButton;

    private final AtomicReference<Layer> selectedLayer = new AtomicReference<>(null);


    public JLayerPanel(final Layer layer) {
        super(new BorderLayout());
        this.layer = layer;
        this.setFocusable(true);
        this.setBackground(new Color(50,50,50));
        final Dimension dimension = new Dimension(200, this.getPreferredSize().height+10);
        final JPanel actionsPanel = new JPanel(new BorderLayout());
        this.add(new JLabel(this.layer.getClass().getSimpleName()), BorderLayout.CENTER);

        this.visibilityButton = this.createButton("Visibility", "display.png");
        this.lockedButton = this.createButton("Lock", "unlock.png");
        

        this.add(this.visibilityButton, BorderLayout.WEST);

        this.visibilityButton.addActionListener(event -> {
            if (layer.isVisible()) {
                layer.hide();
                setButtonIcon(this.visibilityButton, "hidden.png");
            } else {
                layer.display();
                setButtonIcon(this.visibilityButton, "display.png");
            }
            if (Objects.nonNull(onVisibilityChangedOnLayerEventListener)) {
                onVisibilityChangedOnLayerEventListener.accept(layer);
            }
        });

        this.lockedButton.addActionListener(event -> {
            if (layer.isLocked()) {
                layer.unlock();
                setButtonIcon(this.lockedButton, "unlock.png");
            } else {
                layer.lock();
                setButtonIcon(this.lockedButton, "lock.png");
            }
            if (Objects.nonNull(onLockChangedOnLayerEventListener)) {
                onLockChangedOnLayerEventListener.accept(layer);
            }
        });


        this.add(this.lockedButton, BorderLayout.EAST);
        // this.add(actionsPanel, BorderLayout.SOUTH);
        this.setPreferredSize(dimension);

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Objects.nonNull(onSelectedLayerEventListerner)) {
                    onSelectedLayerEventListerner.accept(layer);

                }                
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                
            }

            @Override
            public void mouseExited(MouseEvent e) {
                
            }

            @Override
            public void mousePressed(MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                
            }
            
        });

    }

    private final void setButtonIcon(final JButton button, final String iconName) {
        URL iconUrl = getClass().getClassLoader().getResource("icons/"+iconName);
        if (iconUrl != null) {

            int desiredWidth = 20;
            int desiredHeight = desiredWidth;

            final Dimension dimension = new Dimension(desiredWidth+10, desiredHeight+10);
            button.setPreferredSize(dimension);
            button.setSize(dimension);

            // Redimensionner l'image
            final ImageIcon imageIcon = new ImageIcon(iconUrl);
            final Image originalImage = imageIcon.getImage();
            final Image resizedImage = originalImage.getScaledInstance(desiredWidth, desiredHeight, Image.SCALE_SMOOTH);
            final ImageIcon resizedIcon = new ImageIcon(resizedImage);
            button.setIcon(resizedIcon);
        }
    }

    private JButton createButton(final String text, final String iconName) {
        final JButton button = new JButton();
        button.setToolTipText(text);
        this.setButtonIcon(button, iconName);
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessiner le fond avec les marges intérieures
        Insets insets = this.getInsets();
        if (this.selectedLayer.get() == this.layer) {
            this.setBackground(this.layer.getFillColor());
            g.setColor(this.getBackground());
        } else {
            this.setBackground(new Color(50,50,50));
        }
        g.setColor(this.getBackground());
        g.fillRect(insets.left + leftPadding, insets.top + topPadding,
                getWidth() - insets.left - insets.right - leftPadding - rightPadding,
                getHeight() - insets.top - insets.bottom - topPadding - bottomPadding);
    }

    @Override
    public Dimension getPreferredSize() {
        // Taille préférée en tenant compte des marges intérieures
        Dimension preferredSize = super.getPreferredSize();
        return new Dimension(preferredSize.width + leftPadding + rightPadding,
                preferredSize.height + topPadding + bottomPadding);
    }

    @Override
    public Insets getInsets() {
        // Retourner les marges extérieures
        return new Insets(topPadding, leftPadding, bottomPadding, rightPadding);
    }

    // Méthode pour modifier les marges intérieures
    public void setPadding(int top, int left, int bottom, int right) {
        this.topPadding = top;
        this.leftPadding = left;
        this.bottomPadding = bottom;
        this.rightPadding = right;
        this.revalidate(); // Mettre à jour la mise en page
        this.repaint(); // Redessiner le composant
    }

    public Layer getLayer() {
        return this.layer;
    }

    public void setOnSelectedLayerEventListerner(Consumer<Layer> onSelectedLayerEventListerner) {
        this.onSelectedLayerEventListerner = onSelectedLayerEventListerner;
    }

    public void setOnLockChangedOnLayerEventListener(Consumer<Layer> onLockChangedOnLayerEventListener) {
        this.onLockChangedOnLayerEventListener = onLockChangedOnLayerEventListener;
    }
    public void setOnVisibilityChangedOnLayerEventListener(Consumer<Layer> onVisibilityChangedOnLayerEventListener) {
        this.onVisibilityChangedOnLayerEventListener = onVisibilityChangedOnLayerEventListener;
    }


    public void setSelectedLayer(Layer layer) {
        this.selectedLayer.set(layer);
    }
    
}
