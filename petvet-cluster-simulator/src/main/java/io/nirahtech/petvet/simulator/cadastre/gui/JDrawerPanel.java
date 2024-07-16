package io.nirahtech.petvet.simulator.cadastre.gui;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import io.nirahtech.petvet.simulator.cadastre.domain.Building;
import io.nirahtech.petvet.simulator.cadastre.domain.CadastralPlan;
import io.nirahtech.petvet.simulator.cadastre.domain.Land;
import io.nirahtech.petvet.simulator.cadastre.domain.Mathematics;
import io.nirahtech.petvet.simulator.cadastre.domain.Parcel;
import io.nirahtech.petvet.simulator.cadastre.domain.Section;
import io.nirahtech.petvet.simulator.cadastre.domain.Segment;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.JWindRoseCompassPanel;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers.BuildingLayer;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers.LandLayer;
import io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers.Layer;

public class JDrawerPanel extends JPanel {

    private static final Color BACKGROUND_COLOR = new Color(200, 200, 200);

    private final SortedMap<Layer, LinkedList<CornerSprite>> cornerSpritesByLayer = new TreeMap<>(
            Comparator.comparingInt(Layer::getOrder));
    private AtomicReference<CornerSprite> selectedCornerSprite = new AtomicReference<>(null);

    private final AtomicReference<Layer> selectedLayer = new AtomicReference<>(null);
    private final CadastralPlan cadastralPlan;
    private Parcel selectedParcel = null;

    public JDrawerPanel(final CadastralPlan cadastralPlan) {
        super();
        this.setLayout(new BorderLayout());

        this.setBackground(BACKGROUND_COLOR);
        this.setFocusable(true);

        this.cadastralPlan = cadastralPlan;

        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomRightPanel.setOpaque(false);
        bottomRightPanel.add(new JWindRoseCompassPanel());

        this.add(bottomRightPanel, BorderLayout.SOUTH);

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

    private final Point calculatePolygonCenter(Polygon polygon) {
        int numPoints = polygon.npoints;
        int[] xPoints = polygon.xpoints;
        int[] yPoints = polygon.ypoints;

        double sumX = 0;
        double sumY = 0;

        for (int i = 0; i < numPoints; i++) {
            sumX += xPoints[i];
            sumY += yPoints[i];
        }

        double centerX = sumX / numPoints;
        double centerY = sumY / numPoints;

        return new Point((int) centerX, (int) centerY);
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
                            double length = current.distance(other);
                            graphics2D.drawString(String.format("%.2f m", length / 10), (int) textPosition[0],
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

    private final void displaySurfaceInfos(Graphics graphics, final Polygon polygon, final Layer layer) {
        final Point center = this.calculatePolygonCenter(polygon);
        StringBuilder stringBuilder = new StringBuilder()
                .append("Perimeter: ")
                .append(layer.getSurface().calculatePerimeter())
                .append("<br/>")
                .append("Area: ")
                .append(layer.getSurface().calculateArea());

        graphics.drawString(stringBuilder.toString(), center.x, center.y);
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
                    this.displaySurfaceInfos(graphics, polygon, layer);
                }
            }
        }
    }

    private final void drawAngles(Graphics graphics, LinkedList<Point> points) {
        if (Objects.nonNull(points)) {

            if (points.size() >= 3) {
                Graphics2D graphics2d = (Graphics2D) graphics;
                graphics2d.setColor(Color.WHITE);

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



                    final double[] acMiddle = Mathematics.computeMiddle(
                            new double[] { a.getX(), a.getY() },
                            new double[] { c.getX(), c.getY() });


                    final double[] textPosition = Mathematics.computePoint(
                            new double[] { b.getX(), b.getY() },
                            acMiddle,
                            10);


                    graphics2d.drawString(
                            String.format("%.2f° / %.2f°", innerAngleABCInDegrees, outerAngleABCInDegrees),
                            (int) textPosition[0], (int) textPosition[1]);
                }

            }
        }

    }

    @Override
    protected void paintComponent(Graphics graphics) {
        graphics.setColor(BACKGROUND_COLOR);
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
                                if (cornerSpritesByLayer.get(layer).getLast()
                                        .equals(selectedCornerSprite.get())
                                        && cornerSpritesByLayer.get(layer).getFirst()
                                                .equals(potentialSelectedCornerOnClick)) {
                                    createNewCorner(
                                            cornerSpritesByLayer.get(layer).getFirst().getPoint());

                                    updateCadastrePlan(layer);
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

        private void updateCadastrePlan(final Layer layer) {
            final List<Segment> landSegments = new ArrayList<>();
            final List<List<Segment>> buildingsSegments = new ArrayList<>();
            // final List<Building> buildings = new ArrayList<>();
            final List<Point> landVertices = new ArrayList<>();

            List<Segment> segments = createSegments(layer.getPoints());

            if (layer instanceof LandLayer) {
                landSegments.addAll(segments);
                landVertices.addAll(layer.getPoints());
                final Land land = new Land(segments.toArray(new Segment[0]), landVertices);
                final Parcel parcel = new Parcel(0, land);
                final Section section = new Section("DEFAULT", parcel);
                cadastralPlan.addSection(section);
                selectedParcel = parcel;
                layer.setSurface(land);
            } else if (layer instanceof BuildingLayer) {
                buildingsSegments.add(segments);
                if (Objects.nonNull(selectedParcel)) {
                    final Building building = new Building(segments.toArray(new Segment[0]), layer.getPoints());
                    selectedParcel.land().addBuilding(building);
                    layer.setSurface(building);

                }
            }
        }

    }

    private List<Segment> createSegments(List<Point> points) {
        List<Segment> segments = new ArrayList<>();
        int size = points.size();

        for (int index = 0; index < size; index++) {
            Point from = points.get(index);
            Point to = points.get((index + 1) % size);
            segments.add(new Segment(from, to));
        }

        return segments;
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
