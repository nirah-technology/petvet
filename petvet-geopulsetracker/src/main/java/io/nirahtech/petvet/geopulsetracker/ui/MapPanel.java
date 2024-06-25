package io.nirahtech.petvet.geopulsetracker.ui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import io.nirahtech.petvet.geopulsetracker.domain.ESP32;
import io.nirahtech.petvet.geopulsetracker.domain.ElectronicChipBoard;

public class MapPanel extends JPanel {

    private static final int WIFI_MAX_COVERAGE_IN_CENTIMETERS = 20000 / 100;
    private static final int BLUETOOTH_MAX_COVERAGE_IN_CENTIMETERS = 1000 / 100;

    private static final Color UNSELECTED_CHIP_BOARD_COLOR = new Color(4, 99, 7);
    private static final Color SELECTED_CHIP_BOARD_COLOR = new Color(106, 207, 101);

    private static final Color WIFI_MAX_SIGNAL_COLOR = new Color(0, 123, 255);
    private static final Color WIFI_COVERAGE_COLOR = new Color(0, 123, 255, 32);

    private static final Color BLUETOOTH_MAX_SIGNAL_COLOR = new Color(0, 114, 188);
    private static final Color BLUETOOTH_COVERAGE_COLOR = new Color(0, 114, 188, 32);

    private final List<ElectronicChipBoard> electronicChipBoardsWithoutPositions = new ArrayList<>();
    private final List<ElectronicChipBoard> electronicChipBoards = new ArrayList<>();
    private final Map<ElectronicChipBoard, Point> electronicChipBoardsLocations = new HashMap<>();

    private ElectronicChipBoard selectedChipBoard = null;
    private boolean isMousePressedOnChipBoard = false;

    private RadarEcho radarEcho = null;

    private double zoomScale = 1.0;
    private Point viewCenter = new Point(0, 0);

    private Optional<ElectronicChipBoard> retriveSelectedChipBoard(Point clickedPoint) {
        Optional<ElectronicChipBoard> potentialSelectedChipBoard = Optional.empty();
        for (Map.Entry<ElectronicChipBoard, Point> chipBoardSprite : this.electronicChipBoardsLocations.entrySet()) {
            final Point point = chipBoardSprite.getValue();
            final ElectronicChipBoard chipBoard = chipBoardSprite.getKey();
            final int zoneX = (int) ((point.x - (chipBoard.getWidthInCm() / 2)) * zoomScale);
            final int zoneY = (int) ((point.y - (chipBoard.getHeightInCm() / 2)) * zoomScale);
            final Rectangle collisionArea = new Rectangle(zoneX, zoneY, (int) (chipBoard.getWidthInCm() * zoomScale),
                    (int) (chipBoard.getHeightInCm() * zoomScale));
            if (collisionArea.contains(clickedPoint)) {
                potentialSelectedChipBoard = Optional.of(chipBoardSprite.getKey());
                break;
            }
        }
        this.isMousePressedOnChipBoard = potentialSelectedChipBoard.isPresent();
        if (this.isMousePressedOnChipBoard) {
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
            setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

        }
        return potentialSelectedChipBoard;
    }

    public void addElectronicChipBoard(final ElectronicChipBoard electronicalCard) {
        this.electronicChipBoardsWithoutPositions.add(electronicalCard);
        this.electronicChipBoards.add(electronicalCard);
    }

    public MapPanel(Collection<ElectronicChipBoard> electronicChipBoards) {
        this.setBackground(Color.BLACK);
        electronicChipBoards.forEach(board -> addElectronicChipBoard(board));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                final Point clickedPoint = event.getPoint();
                final Optional<ElectronicChipBoard> potentialSelectedChipBoard = retriveSelectedChipBoard(clickedPoint);
                if (potentialSelectedChipBoard.isPresent()) {
                    selectedChipBoard = potentialSelectedChipBoard.get();
                } else {
                    if (!electronicChipBoardsWithoutPositions.isEmpty()) {
                        ElectronicChipBoard chipBoardWithoutPosition = electronicChipBoardsWithoutPositions.get(0);
                        electronicChipBoardsLocations.put(chipBoardWithoutPosition, clickedPoint);
                        selectedChipBoard = chipBoardWithoutPosition;
                        electronicChipBoardsWithoutPositions.remove(chipBoardWithoutPosition);
                    }
                }

                if (Objects.nonNull(selectedChipBoard)) {
                    if (radarEcho != null) {
                        radarEcho.stop();
                    }
                    Point center = electronicChipBoardsLocations.get(selectedChipBoard);
                    radarEcho = new RadarEcho(center, WIFI_MAX_COVERAGE_IN_CENTIMETERS / 2);
                    radarEcho.start();
                }

                repaint();
            }

            @Override
            public void mousePressed(MouseEvent event) {
                retriveSelectedChipBoard(event.getPoint()).ifPresent((electronicalCard) -> {
                    selectedChipBoard = electronicalCard;
                    isMousePressedOnChipBoard = true;
                    repaint();
                });
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                if (isMousePressedOnChipBoard) {
                    isMousePressedOnChipBoard = false;
                    repaint();
                }
            }

        });

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent event) {
                // int notches = event.getWheelRotation();
                // if (notches < 0) {
                // // Zoom in
                // zoom(event.getPoint(), 1.1);
                // repaint();
                // } else {
                // // Zoom out
                // zoom(event.getPoint(), 0.9);
                // repaint();
                // }
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                if (Objects.nonNull(selectedChipBoard) && isMousePressedOnChipBoard) {
                    electronicChipBoardsLocations.get(selectedChipBoard).setLocation(event.getPoint());
                    repaint();
                }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                if (event.getKeyCode() == KeyEvent.VK_DELETE) {
                    if (Objects.nonNull(selectedChipBoard)) {
                        electronicChipBoardsLocations.remove(selectedChipBoard);
                        electronicChipBoards.remove(selectedChipBoard);
                        electronicChipBoardsWithoutPositions.add(selectedChipBoard);
                        selectedChipBoard = null;
                        repaint();
                    }
                }
            }
        });

        this.setFocusable(true);
    }

    private void zoom(Point zoomPoint, final double zoomFactor) {
        int offsetX = (int) ((zoomPoint.x - viewCenter.getX()) * (zoomFactor - 1));
        int offsetY = (int) ((zoomPoint.y - viewCenter.getY()) * (zoomFactor - 1));
        this.zoomScale *= zoomFactor;
        this.viewCenter.translate(offsetX, offsetY);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        setBackground(Color.BLACK);

        final Graphics2D graphics2D = (Graphics2D) graphics.create();
        graphics2D.scale(zoomScale, zoomScale);
        graphics2D.translate(viewCenter.getX(), viewCenter.getY());

        for (Map.Entry<ElectronicChipBoard, Point> sprites : this.electronicChipBoardsLocations.entrySet()) {
            final Point point = sprites.getValue();
            final ElectronicChipBoard controller = sprites.getKey();

            // Draw the WiFi
            drawWiFiSignal(graphics2D, point);

            // Draw the Bluetooth
            drawBluetoothSignal(graphics2D, point);

            // Draw the board
            drawElectronicalChipBoard(graphics2D, point, controller);
        }

        if (Objects.nonNull(radarEcho)) {
            radarEcho.draw(graphics2D);
        }

        graphics2D.dispose();
    }

    private void drawElectronicalChipBoard(Graphics graphics, final Point point, final ElectronicChipBoard controller) {
        int boardLeft = (int) ((point.x - (controller.getWidthInCm() / 2)) * zoomScale);
        int boardTop = (int) ((point.y - (controller.getHeightInCm() / 2)) * zoomScale);
        int boardWidth = (int) (controller.getWidthInCm() * zoomScale);
        int boardHeight = (int) (controller.getHeightInCm() * zoomScale);
        if (controller.equals(this.selectedChipBoard)) {
            graphics.setColor(SELECTED_CHIP_BOARD_COLOR);
        } else {
            graphics.setColor(UNSELECTED_CHIP_BOARD_COLOR);
        }
        graphics.fillRect(boardLeft, boardTop, boardWidth, boardHeight);
    }

    private void drawBluetoothSignal(Graphics graphics, final Point point) {
        int bluetoothLeft = (int) ((point.x - (BLUETOOTH_MAX_COVERAGE_IN_CENTIMETERS / 2)) * zoomScale);
        int bluetoothTop = (int) ((point.y - (BLUETOOTH_MAX_COVERAGE_IN_CENTIMETERS / 2)) * zoomScale);
        int bluetoothDiameter = (int) (BLUETOOTH_MAX_COVERAGE_IN_CENTIMETERS * zoomScale);
        graphics.setColor(BLUETOOTH_COVERAGE_COLOR);
        graphics.fillOval(bluetoothLeft, bluetoothTop, bluetoothDiameter, bluetoothDiameter);

        graphics.setColor(BLUETOOTH_MAX_SIGNAL_COLOR);
        graphics.drawOval(bluetoothLeft, bluetoothTop, bluetoothDiameter, bluetoothDiameter);
    }

    private void drawWiFiSignal(Graphics graphics, final Point point) {
        int wifiLeft = (int) ((point.x - (WIFI_MAX_COVERAGE_IN_CENTIMETERS / 2)) * zoomScale);
        int wifiTop = (int) ((point.y - (WIFI_MAX_COVERAGE_IN_CENTIMETERS / 2)) * zoomScale);
        int wifiDiameter = (int) (WIFI_MAX_COVERAGE_IN_CENTIMETERS * zoomScale);
        graphics.setColor(WIFI_COVERAGE_COLOR);
        graphics.fillOval(wifiLeft, wifiTop, wifiDiameter, wifiDiameter);

        graphics.setColor(WIFI_MAX_SIGNAL_COLOR);
        graphics.drawOval(wifiLeft, wifiTop, wifiDiameter, wifiDiameter);
    }

    private class RadarEcho {
        private int currentRadius = 0;
        private final int maxRadius;
        private final Point center;
        private Timer animationTimer;

        public RadarEcho(Point center, int maxRadius) {
            this.center = center;
            this.maxRadius = maxRadius;
        }

        public void start() {
            stop();
            animationTimer = new Timer();
            TimerTask animationTask = new TimerTask() {
                @Override
                public void run() {
                    currentRadius += (maxRadius / 40); // 40 steps over 2 seconds (50ms interval)
                    if (currentRadius > maxRadius) {
                        currentRadius = 0;
                    }
                    repaint();
                }
            };

            animationTimer.scheduleAtFixedRate(animationTask, 0, 10);
        }

        public void draw(Graphics2D g2d) {
            g2d.setColor(new Color(0, 255, 0, 128)); // Vert translucide pour l'écho
            g2d.drawOval(center.x - currentRadius, center.y - currentRadius, currentRadius * 2, currentRadius * 2);
        }

        public void stop() {
            if (animationTimer != null) {
                animationTimer.cancel();
            }
        }
    }

}
