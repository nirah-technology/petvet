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
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.JPanel;

import io.nirahtech.petvet.geopulsetracker.domain.ESP32;
import io.nirahtech.petvet.geopulsetracker.domain.ElectronicChipBoard;
import io.nirahtech.petvet.messaging.util.MacAddress;

public class MapPanel extends JPanel {

    private static final int WIFI_MAX_COVERAGE_IN_CENTIMETERS = 20000 / 100;
    private static final int BLUETOOTH_MAX_COVERAGE_IN_CENTIMETERS = 1000 / 100;

    private static final Color UNSELECTED_CHIP_BOARD_COLOR = new Color(4, 99, 7);
    private static final Color SELECTED_CHIP_BOARD_COLOR = new Color(106, 207, 101);

    private static final Color WIFI_MAX_SIGNAL_COLOR = new Color(0, 123, 255);
    private static final Color WIFI_COVERAGE_COLOR = new Color(0, 123, 255, 32);

    private static final Color BLUETOOTH_MAX_SIGNAL_COLOR = new Color(0, 114, 188);
    private static final Color BLUETOOTH_COVERAGE_COLOR = new Color(0, 114, 188, 32);
    private static final int VARIATION_TOLERANCE_IN_DBM = 5;

    private final Set<ElectronicChipBoard> electronicChipBoardsWithoutPositions = new HashSet<>();
    private final Set<ElectronicChipBoard> cluster = new HashSet<>();
    private final Map<ElectronicChipBoard, Point> electronicChipBoardsLocations = new ConcurrentHashMap<>();
    private final Map<ElectronicChipBoard, RadarEcho> radarEchos = new ConcurrentHashMap<>();
    private final DistanceRegistry distanceRegistry = new DistanceRegistry();

    private ElectronicChipBoard selectedChipBoard = null;
    private boolean isMousePressedOnChipBoard = false;

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
        if (!this.electronicChipBoardsLocations.containsKey(electronicalCard)) {
            this.electronicChipBoardsWithoutPositions.add(electronicalCard);
            this.cluster.add(electronicalCard);
            System.out.println(this.electronicChipBoardsWithoutPositions.size());
        }
    }

    public MapPanel(Collection<ElectronicChipBoard> cluster) {
        this.setBackground(Color.BLACK);
        cluster.forEach(this::addElectronicChipBoard);

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                // final Point clickedPoint = event.getPoint();
                // final Optional<ElectronicChipBoard> potentialSelectedChipBoard =
                // retriveSelectedChipBoard(clickedPoint);
                // if (potentialSelectedChipBoard.isPresent()) {
                // selectedChipBoard = potentialSelectedChipBoard.get();
                // } else {
                // if (!electronicChipBoardsWithoutPositions.isEmpty()) {
                // ElectronicChipBoard chipBoardWithoutPosition =
                // List.copyOf(electronicChipBoardsWithoutPositions).get(0);
                // electronicChipBoardsLocations.put(chipBoardWithoutPosition, clickedPoint);
                // selectedChipBoard = chipBoardWithoutPosition;
                // electronicChipBoardsWithoutPositions.remove(chipBoardWithoutPosition);
                // }
                // }
                // repaint();
            }

            @Override
            public void mousePressed(MouseEvent event) {
                // retriveSelectedChipBoard(event.getPoint()).ifPresent((electronicalCard) -> {
                // selectedChipBoard = electronicalCard;
                // isMousePressedOnChipBoard = true;
                // repaint();
                // });
            }

            @Override
            public void mouseReleased(MouseEvent event) {
                // if (isMousePressedOnChipBoard) {
                // isMousePressedOnChipBoard = false;
                // repaint();
                // }
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
                // if (Objects.nonNull(selectedChipBoard) && isMousePressedOnChipBoard) {
                // electronicChipBoardsLocations.get(selectedChipBoard).setLocation(event.getPoint());
                // repaint();
                // }
            }
        });

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
                // if (event.getKeyCode() == KeyEvent.VK_DELETE) {
                // if (Objects.nonNull(selectedChipBoard)) {
                // electronicChipBoardsLocations.remove(selectedChipBoard);
                // cluster.remove(selectedChipBoard);
                // electronicChipBoardsWithoutPositions.add(selectedChipBoard);
                // selectedChipBoard = null;
                // repaint();
                // }
                // }
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

        synchronized (this.electronicChipBoardsLocations) {
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
        }

        synchronized (this.radarEchos) {
            try {
                this.radarEchos.values().forEach(radar -> radar.draw(graphics2D));
            } catch (Exception e) {
                // TODO: handle exception
            }
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

    private final class RadarEcho {
        private int currentRadius = 0;
        private final int maxRadius;
        private final Point center;
        private Timer animationTimer;

        public RadarEcho(final Point center, final int maxRadius) {
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
                        stop();
                    }
                    repaint();
                }
            };

            animationTimer.scheduleAtFixedRate(animationTask, 0, 10);
        }

        public void draw(final Graphics2D g2d) {
            g2d.setColor(new Color(0, 255, 0, 128)); // Vert translucide pour l'écho
            g2d.drawOval(center.x - currentRadius, center.y - currentRadius, currentRadius * 2, currentRadius * 2);
        }

        public void stop() {
            if (animationTimer != null) {
                animationTimer.cancel();
                animationTimer = null;
            }
        }
    }

    private final Point computeDefaultScannerPosition(final ESP32 scanner) {
        return electronicChipBoardsLocations.computeIfAbsent(scanner, key -> {
            Point defaultPosition = new Point(getWidth() / 2, getHeight() / 2);
            cluster.add(scanner);
            return defaultPosition;
        });
    }

    private final Optional<ESP32> retrieveESP32FromMacAddress(final MacAddress macAddress) {
        return cluster.stream()
                .filter(ESP32.class::isInstance)
                .map(ESP32.class::cast)
                .filter(esp -> esp.getBluetooth().getMacAddress().equals(macAddress)
                        || esp.getWifi().getMacAddress().equals(macAddress))
                .findFirst();
    }

    private boolean hasSignalsChanged(final ESP32 scanner,
            final Collection<Map.Entry<MacAddress, Float>> otherNodesDistances, boolean mustRegisterChanges) {
        final AtomicBoolean hasChanged = new AtomicBoolean(false);
        otherNodesDistances.forEach(node -> {
            final Optional<Distance> distanceBetweenScannerAndNode = distanceRegistry
                    .getFor(scanner.getWifi().getMacAddress(), node.getKey());
            if (distanceBetweenScannerAndNode.isPresent()) {
                final Distance distance = distanceBetweenScannerAndNode.get();
                if (Math.abs(
                        (double) distance.getSignalStrenghtInDBM() - node.getValue()) > VARIATION_TOLERANCE_IN_DBM) {
                    hasChanged.set(true);
                }
            } else {
                hasChanged.set(true);
                if (mustRegisterChanges) {
                    this.retrieveESP32FromMacAddress(node.getKey()).ifPresent(esp -> {
                        cluster.add(esp);
                        distanceRegistry.register(scanner, esp, node.getValue());
                    });
                }
            }
        });
        return hasChanged.get();
    }

    private Point calculateNeighborPosition(Point center, double radius) {
        double angle = (new Random()).nextDouble() * 2 * Math.PI; // Random angle between 0 and 2*PI

        int x = (int) (center.x + radius * Math.cos(angle));
        int y = (int) (center.y + radius * Math.sin(angle));

        return new Point(x, y);

    }

    private final void setPositionForEachNodeAroundTheScanner(ESP32 scanner,
            Collection<Map.Entry<MacAddress, Float>> otherNodesDistances) {
        final Point scannerPosition = electronicChipBoardsLocations.get(scanner);
        otherNodesDistances.forEach(node -> {
            this.retrieveESP32FromMacAddress(node.getKey()).ifPresent(esp -> {
                final Optional<Distance> distanceBetweenScannerAndNode = distanceRegistry
                        .getFor(scanner.getWifi().getMacAddress(), node.getKey());
                if (distanceBetweenScannerAndNode.isPresent()) {
                    final Point nodePosition = calculateNeighborPosition(scannerPosition,
                            distanceBetweenScannerAndNode.get().computeDistanceInMeters());
                    // final double x = (scannerPosition.getX() +
                    // distanceBetweenScannerAndNode.get().computeDistanceInMeters());
                    // final double y = scannerPosition.getY();
                    // final Point nodePosition = new Point((int)x, (int)y);
                    this.electronicChipBoardsLocations.put(esp, nodePosition);
                }
            });
        });
    }

    private void detectClusterLocation(ESP32 scanner, Collection<Map.Entry<MacAddress, Float>> otherNodesDistances) {
        System.out.println(otherNodesDistances);
        if (this.hasSignalsChanged(scanner, otherNodesDistances, true)) {
            this.computeDefaultScannerPosition(scanner);
            this.setPositionForEachNodeAroundTheScanner(scanner, otherNodesDistances);
        }
    }

    private boolean areNodesAtCorrectDistances(ESP32 scanner, Collection<Map.Entry<MacAddress, Float>> otherNodesDistances) {
        boolean distancesCorrect = true;
        Point scannerPosition = electronicChipBoardsLocations.get(scanner);
    
        for (Map.Entry<MacAddress, Float> entry : otherNodesDistances) {
            MacAddress macAddress = entry.getKey();
            float distanceInMeters = entry.getValue();
    
            Optional<ESP32> optionalESP = retrieveESP32FromMacAddress(macAddress);
            if (optionalESP.isPresent()) {
                ESP32 esp = optionalESP.get();
                Point nodePosition = electronicChipBoardsLocations.get(esp);
    
                // Calculate distance between scanner and node
                double actualDistance = scannerPosition.distance(nodePosition);
    
                // Compare with expected distance
                if (Math.abs(actualDistance - distanceInMeters) > 10) {
                    distancesCorrect = false;
                    break; // Exit early if one distance is incorrect
                }
            }
        }
    
        return distancesCorrect;
    }
    
    private void adjustNodePositionsIfNeeded(ESP32 scanner, Collection<Map.Entry<MacAddress, Float>> otherNodesDistances) {
        if (!areNodesAtCorrectDistances(scanner, otherNodesDistances)) {
            // If distances are not correct, adjust node positions
            setPositionForEachNodeAroundTheScanner(scanner, otherNodesDistances);
        }
    }

    
    public void triggerPulse(ESP32 scanner, Map<MacAddress, Float> scanReportResults) {
        addElectronicChipBoard(scanner);
        if (this.radarEchos.containsKey(scanner)) {
            this.radarEchos.get(scanner).stop();
            this.radarEchos.remove(scanner);
        }
        if (this.electronicChipBoardsLocations.containsKey(scanner)) {
            final Point center = this.electronicChipBoardsLocations.get(scanner);
            final RadarEcho sonar = new RadarEcho(center, WIFI_MAX_COVERAGE_IN_CENTIMETERS / 2);
            this.radarEchos.put(scanner, sonar);
            sonar.start();
        }

        final Collection<Map.Entry<MacAddress, Float>> knownESP32Reports = scanReportResults.entrySet()
                .stream()
                .filter(report -> this.cluster.stream()
                        .filter(ESP32.class::isInstance)
                        .map(ESP32.class::cast)
                        .anyMatch(esp -> esp.getWifi().getMacAddress().equals(report.getKey())
                                || esp.getBluetooth().getMacAddress().equals(report.getKey())))
                .toList();
        detectClusterLocation(scanner, knownESP32Reports);
        repaint();

    }
}
