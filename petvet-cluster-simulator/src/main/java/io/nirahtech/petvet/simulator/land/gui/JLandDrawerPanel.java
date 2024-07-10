package io.nirahtech.petvet.simulator.land.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;



public class JLandDrawerPanel extends JPanel {

    private final LinkedList<CornerSprite> cornerSprites = new LinkedList<>();
    private AtomicReference<CornerSprite> selectedCornerSprite = new AtomicReference<>();

    private final Layer layer = new LandLayer();

    public JLandDrawerPanel() {
        super();

        this.setBackground(new Color(0,0,0));
        this.setFocusable(true);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
            }

            @Override
            public void mousePressed(MouseEvent event) {
                if (SwingUtilities.isLeftMouseButton(event)) {
                    final Point clickedPoint = event.getPoint();
                    retrievePotentialSelectedCornerOnClick(clickedPoint).ifPresentOrElse((potentialSelectedCornerOnClick -> {
                        if (cornerSprites.getLast().equals(selectedCornerSprite.get()) && cornerSprites.getFirst().equals(potentialSelectedCornerOnClick)) {
                            createNewCorner(cornerSprites.getFirst().getPoint());
                        } else {
                            selectCornerSprite(potentialSelectedCornerOnClick);
                        }
                    }), () -> {
                        createNewCorner(clickedPoint);
                    });

                    repaint();
                }
            }

        });


        

        this.addMouseMotionListener(new MouseAdapter() {

            @Override
            public void mouseMoved(MouseEvent event) {
                final Point mousePosition = event.getPoint();
                cornerSprites.forEach(cornerSprite -> {
                    if (cornerSprite.isHover(mousePosition)) {
                        cornerSprite.displayOverlayDisk();
                    } else {
                        cornerSprite.hideOverlayDisk();
                    }
                });
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent event) {
                final Point mousePosition = event.getPoint();
                if (Objects.nonNull(selectedCornerSprite.get())) {
                    selectedCornerSprite.get().getPoint().setLocation(mousePosition);
                    repaint();
                }
            }

        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (Objects.nonNull(selectedCornerSprite.get())) {
                        final CornerSprite cornerSpriteToDelete = selectedCornerSprite.get();
                        if (cornerSprites.size() > 1) {
                            final int indexToDelete = cornerSprites.indexOf(cornerSpriteToDelete);
                            cornerSprites.remove(cornerSpriteToDelete);
                            if (indexToDelete < cornerSprites.size()) {
                                selectCornerSprite(cornerSprites.get(indexToDelete));
                            } else {
                                selectCornerSprite(cornerSprites.get(indexToDelete-1));
                                
                            }
                            repaint();
                        }
                    }
                }
            }
        });

    }

    private Optional<CornerSprite> retrievePotentialSelectedCornerOnClick(final Point mousePosition) {
        AtomicReference<CornerSprite> selectedCornerSpriteOnClick = new AtomicReference<>(null);
        this.cornerSprites.forEach(cornerSprite -> {
            if (cornerSprite.isHover(mousePosition)) {
                selectedCornerSpriteOnClick.set(cornerSprite);
            }
        });
        return Optional.ofNullable(selectedCornerSpriteOnClick.get());
    }

    private final void createNewCorner(final Point point) {
        final CornerSprite newCornerSprite = new CornerSprite(point, layer);
        cornerSprites.push(newCornerSprite);
        selectCornerSprite(newCornerSprite);
    }


    private final void selectCornerSprite(CornerSprite cornerSpriteToSelect) {
        this.cornerSprites.forEach(otherCornerSprite -> {
            if (otherCornerSprite != cornerSpriteToSelect) {
                otherCornerSprite.unselect();
            }
        });
        cornerSpriteToSelect.select();
        selectedCornerSprite.set(cornerSpriteToSelect);
    }

    private final void drawLines(Graphics graphics) {
        graphics.setColor(this.layer.getBorderColor());
        if (this.cornerSprites.size() >= 2) {
            Graphics2D graphics2D = (Graphics2D) graphics;
            for (int i = 0; i < this.cornerSprites.size(); i++) {
                final CornerSprite current = this.cornerSprites.get(i);
                CornerSprite other = null;
                if (i < this.cornerSprites.size()-1) {
                    other = this.cornerSprites.get(i+1);
                } else {
                    // other = this.cornerSprites.get(0);
                }
                if (Objects.nonNull(other)) {
                    graphics2D.setStroke(new BasicStroke(5.0F));
                    graphics2D.drawLine(current.getPoint().x, current.getPoint().y, other.getPoint().x, other.getPoint().y);
                }
            }

        }
    }

    private final void drawPoints(Graphics graphics) {
        this.cornerSprites.forEach(cornerSprite -> {
            cornerSprite.paintComponent(graphics);
        });
    }

    private final void fillIfRequired(Graphics graphics) {
        if (!this.cornerSprites.isEmpty() && this.cornerSprites.size() >= 3) {
            if (this.cornerSprites.getFirst().getPoint().equals(this.cornerSprites.getLast().getPoint())) {
                graphics.setColor(this.layer.getFillColor());
                final Polygon polygon = new Polygon();
                this.cornerSprites.stream().map(CornerSprite::getPoint).forEach(point -> {
                    polygon.addPoint(point.x, point.y);
                });
                graphics.fillPolygon(polygon);
            }
        }
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        graphics.setColor(new Color(0,0,0));
        graphics.fillRect(0, 0, (int) this.getSize().getWidth(), (int) this.getSize().getHeight());
        this.drawLines(graphics);
        this.drawPoints(graphics);
        this.fillIfRequired(graphics);
        
    }
}
