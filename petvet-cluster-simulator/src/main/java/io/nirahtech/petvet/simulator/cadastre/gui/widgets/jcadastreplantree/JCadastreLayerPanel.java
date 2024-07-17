package io.nirahtech.petvet.simulator.cadastre.gui.widgets.jcadastreplantree;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import io.nirahtech.petvet.simulator.cadastre.domain.Surface;
import io.nirahtech.petvet.simulator.cadastre.gui.WidgetUtils;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers.Layer;

final class JCadastreLayerPanel extends JPanel {

    private final JButton visibilityButton;
    private final JPanel thumbnailPanel;
    private final JLabel nameLabel;
    private final JButton lockButton;
    private final JButton deleteButton;

    private Consumer<Layer> onVisibilityChangedEventListener = null;
    private Consumer<Layer> onLockChangedEventListener = null;
    private Consumer<Layer> onDeleteEventListener = null;
    private Consumer<Layer> onClickEventListener = null;

    private final Surface surface;
    private final Layer layer;


    JCadastreLayerPanel(Surface surface, Layer layer) {
        super();

        super.setOpaque(true);

        this.visibilityButton = WidgetUtils.createSmallButton("Visibility", "display.png");
        this.lockButton = WidgetUtils.createSmallButton("Lock", "unlock.png");
        this.deleteButton = WidgetUtils.createSmallButton("Delete", "delete.png");

        this.attachEventListeners();

        this.thumbnailPanel = new JPanel();
        this.nameLabel = new JLabel(surface.getClass().getSimpleName());

        super.setLayout(new BorderLayout());
        super.add(this.visibilityButton, BorderLayout.WEST);
        
        this.setBackground(Color.RED);

        final JPanel thumbnailAndNamePanel = new JPanel(new BorderLayout());
        this.thumbnailPanel.setPreferredSize(new Dimension(32+16, 32));
        this.thumbnailPanel.setSize(this.thumbnailPanel.getPreferredSize());
        this.thumbnailPanel.setBackground(Color.WHITE);
        thumbnailAndNamePanel.add(this.thumbnailPanel, BorderLayout.WEST);
        thumbnailAndNamePanel.add(this.nameLabel, BorderLayout.CENTER);
        super.add(thumbnailAndNamePanel, BorderLayout.CENTER);

        final JPanel lackAndDeletePanel = new JPanel(new GridLayout(1, 2));
        lackAndDeletePanel.add(this.lockButton);
        lackAndDeletePanel.add(this.deleteButton);
        super.add(lackAndDeletePanel, BorderLayout.EAST);

        this.surface = surface;
        this.layer = layer;
    }

    private final void attachEventListeners() {

        this.visibilityButton.addActionListener(event -> {
            if (layer.isVisible()) {
                layer.hide();
                WidgetUtils.setButtonIcon(this.visibilityButton, "hidden.png");
            } else {
                layer.display();
                WidgetUtils.setButtonIcon(this.visibilityButton, "display.png");
            }
            if (Objects.nonNull(this.onVisibilityChangedEventListener)) {
                this.onVisibilityChangedEventListener.accept(layer);
            }
            
        });

        this.lockButton.addActionListener(event -> {
            if (layer.isLocked()) {
                layer.unlock();
                WidgetUtils.setButtonIcon(this.lockButton, "unlock.png");
            } else {
                layer.lock();
                WidgetUtils.setButtonIcon(this.lockButton, "lock.png");
            }
            if (Objects.nonNull(this.onLockChangedEventListener)) {
                this.onLockChangedEventListener.accept(layer);
            }
        });
        
        this.deleteButton.addActionListener(event -> {
            if (Objects.nonNull(this.onDeleteEventListener)) {
                this.onDeleteEventListener.accept(layer);
            }
        });

        this.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent event) {
                if (Objects.nonNull(onClickEventListener)) {
                    onClickEventListener.accept(layer);
                }
            }

            @Override
            public void mouseEntered(MouseEvent event) {
                
            }

            @Override
            public void mouseExited(MouseEvent event) {
                
            }

            @Override
            public void mousePressed(MouseEvent event) {
                
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                
            }
            
        });

    }


    public Surface getSurface() {
        return surface;
    }

    public Layer getLayer() {
        return layer;
    }

    public void addOnDeleteEventListener(final Consumer<Layer> onDeleteEventListener) {
        this.onDeleteEventListener = onDeleteEventListener;
    }
    public void addOnLockChangedEventListener(final Consumer<Layer> onLockChangedEventListener) {
        this.onLockChangedEventListener = onLockChangedEventListener;
    }
    public void addOnVisibilityChangedEventListener(final Consumer<Layer> onVisibilityChangedEventListener) {
        this.onVisibilityChangedEventListener = onVisibilityChangedEventListener;
    }
    public void addOnClickEventListener(final Consumer<Layer> onClickEventListener) {
        this.onClickEventListener = onClickEventListener;
    }



}
