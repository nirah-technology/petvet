package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import io.nirahtech.petvet.simulator.electronicalcard.Cluster;
import io.nirahtech.petvet.simulator.electronicalcard.ElectronicCard;

public class ClusterLandPanel extends JPanel {


    private static final int WIFI_MAX_COVERAGE_IN_CENTIMETERS = 20000 / 100;
    private static final int BLUETOOTH_MAX_COVERAGE_IN_CENTIMETERS = 1000 / 100;

    private static final Color UNSELECTED_CHIP_BOARD_COLOR = new Color(4, 99, 7);
    private static final Color SELECTED_CHIP_BOARD_COLOR = new Color(106, 207, 101);

    private static final Color WIFI_MAX_SIGNAL_COLOR = new Color(0, 123, 255);
    private static final Color WIFI_COVERAGE_COLOR = new Color(0, 123, 255, 32);

    private static final Color BLUETOOTH_MAX_SIGNAL_COLOR = new Color(0, 114, 188);
    private static final Color BLUETOOTH_COVERAGE_COLOR = new Color(0, 114, 188, 32);


    private final Map<ElectronicCard, Point> electronicCardLocation = new HashMap<>();

    private ElectronicCard selectedChipBoard = null;
    private boolean isMousePressedOnChipBoard = false;
    private final Cluster cluster;


    ClusterLandPanel(final Cluster cluster) {
        super(new BorderLayout());
        this.setBackground(Color.BLACK);
        this.cluster = cluster;

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                final Point clickedPoint = event.getPoint();
                final Optional<ElectronicCard> potentialSelectedChipBoard = retriveSelectedChipBoard(clickedPoint);
                if (potentialSelectedChipBoard.isPresent()) {
                    selectedChipBoard = potentialSelectedChipBoard.get();
                } else {
                    try {
                        ElectronicCard newNode = cluster.generateNode();
                        electronicCardLocation.put(newNode, clickedPoint);
                    } catch (UnknownHostException e) {
                        
                    }
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

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent event) {
                if (Objects.nonNull(selectedChipBoard) && isMousePressedOnChipBoard) {
                    electronicCardLocation.get(selectedChipBoard).setLocation(event.getPoint());
                    repaint();
                }
            }
        });


        this.setFocusable(true);
    }

    private Optional<ElectronicCard> retriveSelectedChipBoard(Point clickedPoint) {
        Optional<ElectronicCard> potentialSelectedChipBoard = Optional.empty();
        for (Map.Entry<ElectronicCard, Point> chipBoardSprite : this.electronicCardLocation.entrySet()) {
            final Point point = chipBoardSprite.getValue();
            final ElectronicCard chipBoard = chipBoardSprite.getKey();
            final int zoneX = (int) ((point.x - (chipBoard.getWidth() / 2)));
            final int zoneY = (int) ((point.y - (chipBoard.getHeight() / 2)));
            final Rectangle collisionArea = new Rectangle(zoneX, zoneY, (int) (chipBoard.getWidth()),
                    (int) (chipBoard.getHeight()));
            if (collisionArea.contains(clickedPoint)) {
                potentialSelectedChipBoard = Optional.of(chipBoardSprite.getKey());
                break;
            }
        }

        return potentialSelectedChipBoard;
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        setBackground(Color.BLACK);

        final Graphics2D graphics2D = (Graphics2D) graphics.create();

        for (Map.Entry<ElectronicCard, Point> sprites : this.electronicCardLocation.entrySet()) {
            final Point point = sprites.getValue();
            final ElectronicCard controller = sprites.getKey();

            // Draw the WiFi
            drawWiFiSignal(graphics2D, point);

            // Draw the Bluetooth
            drawBluetoothSignal(graphics2D, point);

            // Draw the board
            drawElectronicalChipBoard(graphics2D, point, controller);
        }

        // if (Objects.nonNull(radarEcho)) {
        //     radarEcho.draw(graphics2D);
        // }

        graphics2D.dispose();
    }

    private void drawElectronicalChipBoard(Graphics graphics, final Point point, final ElectronicCard controller) {
        int boardLeft = (int) ((point.x - (controller.getWidth() / 2)));
        int boardTop = (int) ((point.y - (controller.getHeight() / 2)));
        int boardWidth = (int) (controller.getWidth());
        int boardHeight = (int) (controller.getHeight());
        if (controller.equals(this.selectedChipBoard)) {
            graphics.setColor(SELECTED_CHIP_BOARD_COLOR);
        } else {
            graphics.setColor(UNSELECTED_CHIP_BOARD_COLOR);
        }
        graphics.fillRect(boardLeft, boardTop, boardWidth, boardHeight);
    }

    private void drawBluetoothSignal(Graphics graphics, final Point point) {
        int bluetoothLeft = (int) ((point.x - (BLUETOOTH_MAX_COVERAGE_IN_CENTIMETERS / 2)));
        int bluetoothTop = (int) ((point.y - (BLUETOOTH_MAX_COVERAGE_IN_CENTIMETERS / 2)));
        int bluetoothDiameter = (int) (BLUETOOTH_MAX_COVERAGE_IN_CENTIMETERS);
        graphics.setColor(BLUETOOTH_COVERAGE_COLOR);
        graphics.fillOval(bluetoothLeft, bluetoothTop, bluetoothDiameter, bluetoothDiameter);

        graphics.setColor(BLUETOOTH_MAX_SIGNAL_COLOR);
        graphics.drawOval(bluetoothLeft, bluetoothTop, bluetoothDiameter, bluetoothDiameter);
    }

    private void drawWiFiSignal(Graphics graphics, final Point point) {
        int wifiLeft = (int) ((point.x - (WIFI_MAX_COVERAGE_IN_CENTIMETERS / 2)));
        int wifiTop = (int) ((point.y - (WIFI_MAX_COVERAGE_IN_CENTIMETERS / 2)));
        int wifiDiameter = (int) (WIFI_MAX_COVERAGE_IN_CENTIMETERS);
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

            // animationTimer.scheduleAtFixedRate(animationTask, 0, 10);
            animationTimer.schedule(animationTask, 0);
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
