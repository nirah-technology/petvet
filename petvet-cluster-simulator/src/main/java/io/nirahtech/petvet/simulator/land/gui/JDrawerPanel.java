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
import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import io.nirahtech.petvet.simulator.land.domain.Mathematics;
import io.nirahtech.petvet.simulator.land.gui.widgets.layers.Layer;

public class JDrawerPanel extends JPanel {

    private final SortedMap<Layer, LinkedList<CornerSprite>> cornerSpritesByLayer = new TreeMap<>(
            Comparator.comparingInt(Layer::getOrder));
    private AtomicReference<CornerSprite> selectedCornerSprite = new AtomicReference<>(null);

    private final AtomicReference<Layer> selectedLayer = new AtomicReference<>(null);

    public JDrawerPanel() {
        super();

        this.setBackground(new Color(0, 0, 0));
        this.setFocusable(true);

        this.addMouseListener(new DrawerMouseAdapter());
        this.addMouseMotionListener(new DrawerMouseMotionAdapter());
        this.addKeyListener(new DrawerKeyAdapter());

    }

    public void setSelectedLayer(Layer layer) {
        this.selectedLayer.set(layer);
        this.cornerSpritesByLayer.putIfAbsent(layer, new LinkedList<>());
        layer.getPoints().forEach(point -> {
            final CornerSprite cornerSprite = new CornerSprite(point, layer);
            this.cornerSpritesByLayer.get(layer).add(cornerSprite);
        });
        revalidate();
        repaint();
    }

    private Optional<CornerSprite> retrievePotentialSelectedCornerOnClick(final Point mousePosition) {
        AtomicReference<CornerSprite> selectedCornerSpriteOnClick = new AtomicReference<>(null);
        if (Objects.nonNull(selectedLayer.get())) {
            this.cornerSpritesByLayer.get(selectedLayer.get()).forEach(cornerSprite -> {
                if (cornerSprite.isHover(mousePosition)) {
                    selectedCornerSpriteOnClick.set(cornerSprite);
                }
            });
        }
        return Optional.ofNullable(selectedCornerSpriteOnClick.get());
    }

    private final void createNewCorner(final Point point) {
        if (Objects.nonNull(this.selectedLayer.get())) {
            this.selectedLayer.get().getPoints().add(point);
            final CornerSprite newCornerSprite = new CornerSprite(point, this.selectedLayer.get());
            this.cornerSpritesByLayer.get(selectedLayer.get()).add(newCornerSprite);
            selectCornerSprite(newCornerSprite);
        }
    }

    private final void selectCornerSprite(CornerSprite cornerSpriteToSelect) {
        this.cornerSpritesByLayer.get(selectedLayer.get()).forEach(otherCornerSprite -> {
            if (otherCornerSprite != cornerSpriteToSelect) {
                otherCornerSprite.unselect();
            }
        });
        cornerSpriteToSelect.select();
        selectedCornerSprite.set(cornerSpriteToSelect);
    }

    private final void drawLines(Graphics graphics, Layer layer) {
        final LinkedList<Point> points = layer.getPoints();
        if (Objects.nonNull(points)) {
            if (points.size() >= 2) {
                Graphics2D graphics2D = (Graphics2D) graphics;
                for (int i = 0; i < points.size(); i++) {
                    final Point current = points.get(i);
                    Point other = null;
                    if (i < points.size() - 1) {
                        other = points.get(i + 1);
                    } else {
                        // other = this.cornerSprites.get(0);
                    }
                    if (Objects.nonNull(other)) {
                        graphics.setColor(layer.getBorderColor());
                        graphics2D.setStroke(new BasicStroke(5.0F));
                        graphics2D.drawLine(current.x, current.y, other.x,
                                other.y);
                        if (!layer.isLocked()) {
                            graphics.setColor(Color.WHITE);
                            double[] textPosition = Mathematics.computeMiddle(
                                    new double[] { current.getX(), current.getY() },
                                    new double[] { other.getX(), other.getY() });
                            double length = Mathematics.computeLength(new double[] { current.getX(), current.getY() },
                                    new double[] { other.getX(), other.getY() });
                            graphics2D.drawString(String.format("%.2f", length), (int) textPosition[0],
                                    (int) textPosition[1]);
                        }
                    }
                }

            }

        }
    }

    private final void drawPoints(Graphics graphics, LinkedList<CornerSprite> points) {
        if (Objects.nonNull(points)) {
            points.forEach(cornerSprite -> {
                cornerSprite.paintComponent(graphics);
            });
        }
    }

    private final void fillIfRequired(Graphics graphics, final Layer layer) {
        LinkedList<Point> points = layer.getPoints();
        if (Objects.nonNull(points)) {

            if (!points.isEmpty()
                    && points.size() >= 3) {
                if (points.getFirst()
                        .equals(points.getLast())) {
                    graphics.setColor(layer.getFillColor());
                    final Polygon polygon = new Polygon();
                    points.stream()
                            .forEach(point -> {
                                polygon.addPoint(point.x, point.y);
                            });
                    graphics.fillPolygon(polygon);
                }
            }
        }
    }

    private final void drawAngles(Graphics graphics, LinkedList<Point> points) {
        if (Objects.nonNull(points)) {

            if (points.size() >= 3) {
                Graphics2D graphics2d = (Graphics2D) graphics;
                graphics2d.setColor(Color.WHITE);

                final double arcRadius = 20.0; // Distance de l'arc de cercle

                for (int index = 1; index < points.size() - 1; index++) {
                    final Point a = points.get(index - 1);
                    final Point b = points.get(index);
                    final Point c = points.get(index + 1);

                    final double innerAngleABCInDegrees = Mathematics.computeInnerAngleToDegrees(
                            new double[] { a.getX(), a.getY() },
                            new double[] { b.getX(), b.getY() },
                            new double[] { c.getX(), c.getY() });

                    final double outerAngleABCInDegrees = Mathematics.computeOuterAngleToDegrees(
                            new double[] { a.getX(), a.getY() },
                            new double[] { b.getX(), b.getY() },
                            new double[] { c.getX(), c.getY() });

                    final double[] startArcPointBA = Mathematics.computePoint(
                            new double[] { b.getX(), b.getY() },
                            new double[] { a.getX(), a.getY() },
                            arcRadius);

                    final double[] stopArcPointBC = Mathematics.computePoint(
                            new double[] { b.getX(), b.getY() },
                            new double[] { c.getX(), c.getY() },
                            arcRadius);

                    // Calcul des angles pour dessiner l'arc
                    double startAngle = Math
                            .toDegrees(Math.atan2(startArcPointBA[1] - b.getY(), startArcPointBA[0] - b.getX()));
                    double endAngle = Math
                            .toDegrees(Math.atan2(stopArcPointBC[1] - b.getY(), stopArcPointBC[0] - b.getX()));

                    // Ajustement des angles pour garantir un tracé anti-horaire
                    if (startAngle < 0) {
                        startAngle += 360;
                    }
                    if (endAngle < 0) {
                        endAngle += 360;
                    }

                    double arcAngle;
                    if (endAngle >= startAngle) {
                        arcAngle = endAngle - startAngle;
                    } else {
                        arcAngle = 360 - (startAngle - endAngle);
                    }

                    System.out.println(String.format("%s, %s", startAngle, endAngle));

                    // Norme du vecteur pour l'arc

                    // final int size = 5;
                    // graphics2d.setColor(new Color(0, 255, 255));
                    // graphics2d.drawOval((int) (startArcPointBA[0] - (size / 2)), (int)
                    // (startArcPointBA[1] - (size / 2)), size,
                    // size);
                    // graphics2d.setColor(new Color(255, 0, 255));
                    // graphics2d.drawOval((int) (stopArcPointBC[0] - (size / 2)), (int)
                    // (stopArcPointBC[1] - (size / 2)), size,
                    // size);

                    // Dessin de l'arc
                    // graphics2d.setColor(new Color(255, 255, 255, 128));
                    // graphics2d.setStroke(new BasicStroke(2));
                    // graphics2d.fillArc(
                    // (int) (b.getX() - arcRadius),
                    // (int) (b.getY() - arcRadius),
                    // (int) (2 * arcRadius),
                    // (int) (2 * arcRadius),
                    // (int) startAngle,
                    // (int) arcAngle);

                    final double[] acMiddle = Mathematics.computeMiddle(
                            new double[] { a.getX(), a.getY() },
                            new double[] { c.getX(), c.getY() });

                    // graphics2d.drawOval((int) (acMiddle[0] - (size / 2)), (int) (acMiddle[1] -
                    // (size / 2)), size,
                    // size);

                    final double[] textPosition = Mathematics.computePoint(
                            new double[] { b.getX(), b.getY() },
                            acMiddle,
                            10);

                    // graphics2d.drawString("A", a.x, a.y);
                    // graphics2d.drawString("B", b.x, b.y);
                    // graphics2d.drawString("C", c.x, c.y);

                    graphics2d.drawString(
                            String.format("%.2f° / %.2f°", innerAngleABCInDegrees, outerAngleABCInDegrees),
                            (int) textPosition[0], (int) textPosition[1]);
                }

            }
        }

    }

    @Override
    protected void paintComponent(Graphics graphics) {
        graphics.setColor(new Color(0, 0, 0));
        graphics.fillRect(0, 0, (int) this.getSize().getWidth(), (int) this.getSize().getHeight());

        this.cornerSpritesByLayer.entrySet().forEach(cornerSpriteByLayer -> {
            final Layer layer = cornerSpriteByLayer.getKey();
            if (layer.isVisible()) {
                this.fillIfRequired(graphics, layer);
                this.drawLines(graphics, layer);
                if (!layer.isLocked()) {
                    this.drawPoints(graphics, cornerSpriteByLayer.getValue());
                }
                if (!layer.isLocked()) {
                    this.drawAngles(graphics, layer.getPoints());
                }
            }
        });

    }

    public void redraw() {
        this.revalidate();
        this.repaint();
    }

    private final class DrawerMouseAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent event) {
            if (SwingUtilities.isLeftMouseButton(event)) {

                final Layer layer = selectedLayer.get();
                if (Objects.nonNull(layer) && layer.isVisible() && !layer.isLocked()) {
                    final Point clickedPoint = event.getPoint();
                    retrievePotentialSelectedCornerOnClick(clickedPoint)
                            .ifPresentOrElse((potentialSelectedCornerOnClick -> {
                                if (cornerSpritesByLayer.get(selectedLayer.get()).getLast()
                                        .equals(selectedCornerSprite.get())
                                        && cornerSpritesByLayer.get(selectedLayer.get()).getFirst()
                                                .equals(potentialSelectedCornerOnClick)) {
                                    createNewCorner(
                                            cornerSpritesByLayer.get(selectedLayer.get()).getFirst().getPoint());
                                } else {
                                    selectCornerSprite(potentialSelectedCornerOnClick);
                                }
                            }), () -> {
                                createNewCorner(clickedPoint);
                            });

                    repaint();
                }
            }
        }

    }

    private final class DrawerMouseMotionAdapter extends MouseAdapter {

        @Override
        public void mouseMoved(MouseEvent event) {
            if (Objects.nonNull(selectedLayer.get())) {
                final Layer layer = selectedLayer.get();
                if (layer.isVisible() && !layer.isLocked()) {
                    final Point mousePosition = event.getPoint();
                    cornerSpritesByLayer.get(selectedLayer.get()).forEach(cornerSprite -> {
                        if (cornerSprite.isHover(mousePosition)) {
                            cornerSprite.displayOverlayDisk();
                        } else {
                            cornerSprite.hideOverlayDisk();
                        }
                    });
                    repaint();
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            final Point mousePosition = event.getPoint();
            if (Objects.nonNull(selectedCornerSprite.get())) {
                final Layer layer = selectedLayer.get();
                if (layer.isVisible() && !layer.isLocked()) {
                    selectedCornerSprite.get().getPoint().setLocation(mousePosition);
                    repaint();
                }

            }
        }

    }

    private final class DrawerKeyAdapter extends KeyAdapter {

        private final void deleteSelectedPoint() {
            final Layer layer = selectedLayer.get();
            if (layer.isVisible() && !layer.isLocked()) {
                final CornerSprite cornerSpriteToDelete = selectedCornerSprite.get();
                if (cornerSpritesByLayer.get(layer).size() > 1) {
                    final int indexToDelete = cornerSpritesByLayer.get(layer).indexOf(cornerSpriteToDelete);
                    layer.getPoints().remove(cornerSpriteToDelete.getPoint());
                    cornerSpritesByLayer.get(layer).remove(cornerSpriteToDelete);
                    if (indexToDelete < cornerSpritesByLayer.get(layer).size()) {
                        selectCornerSprite(cornerSpritesByLayer.get(layer).get(indexToDelete));
                    } else {
                        selectCornerSprite(
                                cornerSpritesByLayer.get(layer).get(indexToDelete - 1));

                    }
                    redraw();
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.VK_DELETE) {
                if (Objects.nonNull(selectedCornerSprite.get())) {
                    this.deleteSelectedPoint();
                }
            } else if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
                selectedCornerSprite.set(null);
                if (Objects.nonNull(selectedLayer.get())) {
                    cornerSpritesByLayer.get(selectedLayer.get()).clear();
                    selectedLayer.get().getPoints().clear();
                    redraw();
                }
            }
        }
    }

}
