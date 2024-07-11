package io.nirahtech.petvet.simulator.land.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public final class JLayerPanel extends JPanel {

    private Consumer<Layer> onSelectedLayerEventListerner = null;

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


    final JCheckBox visibilityCheckBox = new JCheckBox("Is visible", isVisible);
    final JCheckBox lockedCheckBox = new JCheckBox("Is locked", isLocked);


    public JLayerPanel(final Layer layer) {
        super(new BorderLayout());
        this.layer = layer;
        this.setFocusable(true);
        this.setBackground(layer.getFillColor());
        final Dimension dimension = new Dimension(200, 50);
        final JPanel actionsPanel = new JPanel(new GridLayout(1, 2));
        this.add(new JLabel(this.layer.getClass().getSimpleName()), BorderLayout.NORTH);
        actionsPanel.add(this.visibilityCheckBox);

        this.visibilityCheckBox.addItemListener(new ItemListener() {    
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == 1) {
                    layer.display();
                } else {
                    layer.hide();
                }
            }
         });

        this.lockedCheckBox.addItemListener(new ItemListener() {    
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == 1) {
                    layer.lock();
                } else {
                    layer.unlock();
                }
            }
         });


        actionsPanel.add(this.lockedCheckBox);
        this.add(actionsPanel, BorderLayout.SOUTH);
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Dessiner le fond avec les marges intérieures
        Insets insets = this.getInsets();
        g.setColor(getBackground());
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
}
