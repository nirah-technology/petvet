package io.nirahtech.petvet.simulator.electronicalcard.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import io.nirahtech.petvet.simulator.electronicalcard.ElectronicCard;
import io.nirahtech.petvet.simulator.electronicalcard.Signal;


public final class ElectronicCardSprite {

    private static final Color UNSELECTED_CHIP_BOARD_COLOR = new Color(4, 99, 7);
    private static final Color SELECTED_CHIP_BOARD_COLOR = new Color(106, 207, 101);

    private static final Color ORCHESTRATOR_HALO_COLOR = new Color(255, 150, 0);

    private static final Color WIFI_MAX_SIGNAL_COLOR = new Color(0, 123, 255);
    private static final Color WIFI_COVERAGE_COLOR = new Color(0, 123, 255, 16);

    private static final Color BLUETOOTH_MAX_SIGNAL_COLOR = new Color(0, 114, 188);
    private static final Color BLUETOOTH_COVERAGE_COLOR = new Color(0, 114, 188, 32);

    private static final int VARIATION_TOLERANCE_IN_DBM = 5;


    private final ElectronicCard electronicCard;
    private Point center;
    private RadarEcho radarEcho;
    private boolean isSelected = false;
    private float zoomScale = 1.0F;
    private final Runnable repaint;

    public ElectronicCardSprite(final ElectronicCard electronicCard, Runnable repaint) {
        this.electronicCard = electronicCard;
        this.repaint = repaint;
    }

    public final ElectronicCard getElectronicalCard() {
        return this.electronicCard;
    }

    public Point getCenter() {
        return center;
    }
    public void setCenter(Point center) {
        this.center = center;
    }

    private final void drawElectronicalChipBoard(final Graphics graphics) {
        final int boardLeft = (int) ((center.x - (this.electronicCard.getWidth() / 2)) * zoomScale);
        final int boardTop = (int) ((center.y - (this.electronicCard.getHeight() / 2)) * zoomScale);
        final int boardWidth = (int) (this.electronicCard.getWidth() * zoomScale);
        final int boardHeight = (int) (this.electronicCard.getHeight() * zoomScale);
        if (this.isSelected) {
            graphics.setColor(SELECTED_CHIP_BOARD_COLOR);
        } else {
            graphics.setColor(UNSELECTED_CHIP_BOARD_COLOR);
        }
        graphics.fillRect(boardLeft, boardTop, boardWidth, boardHeight);
    }

    private final void drawBluetoothSignal(final Graphics graphics) {
        final int bluetoothLeft = (int) ((center.x - (Signal.BLUETOOTH_MAX_COVERAGE_IN_CENTIMETERS / 2)) * zoomScale);
        final int bluetoothTop = (int) ((center.y - (Signal.BLUETOOTH_MAX_COVERAGE_IN_CENTIMETERS / 2)) * zoomScale);
        final int bluetoothDiameter = (int) (Signal.BLUETOOTH_MAX_COVERAGE_IN_CENTIMETERS * zoomScale);
        graphics.setColor(BLUETOOTH_COVERAGE_COLOR);
        graphics.fillOval(bluetoothLeft, bluetoothTop, bluetoothDiameter, bluetoothDiameter);

        graphics.setColor(BLUETOOTH_MAX_SIGNAL_COLOR);
        graphics.drawOval(bluetoothLeft, bluetoothTop, bluetoothDiameter, bluetoothDiameter);
    }

    private final void drawWiFiSignal(final Graphics graphics) {
        final int wifiLeft = (int) ((this.center.x - (Signal.WIFI_MAX_COVERAGE_IN_CENTIMETERS / 2)) * this.zoomScale);
        final int wifiTop = (int) ((this.center.y - (Signal.WIFI_MAX_COVERAGE_IN_CENTIMETERS / 2)) * this.zoomScale);
        final int wifiDiameter = (int) (Signal.WIFI_MAX_COVERAGE_IN_CENTIMETERS * this.zoomScale);
        graphics.setColor(WIFI_COVERAGE_COLOR);
        graphics.fillOval(wifiLeft, wifiTop, wifiDiameter, wifiDiameter);

        graphics.setColor(WIFI_MAX_SIGNAL_COLOR);
        graphics.drawOval(wifiLeft, wifiTop, wifiDiameter, wifiDiameter);
    }

    private final void drawOrchestratorMode(final Graphics graphics) {
        final int modeLeft = (int) ((this.center.x - (10)) * this.zoomScale);
        final int modeTop = (int) ((this.center.y - (10)) * this.zoomScale);
        final int modeDiameter = (int) (20 * this.zoomScale);
        graphics.setColor(ORCHESTRATOR_HALO_COLOR);
        graphics.drawOval(modeLeft, modeTop, modeDiameter, modeDiameter);
    }

    public void draw(Graphics graphics) {

        final Graphics2D graphics2D = (Graphics2D) graphics.create();
        if (Objects.nonNull(this.center)) {
            // Draw the WiFi
            drawWiFiSignal(graphics2D);
    
            // Draw the Bluetooth
            drawBluetoothSignal(graphics2D);
    
            // Draw the board
            drawElectronicalChipBoard(graphics2D);

            if (this.electronicCard.getProcess().getMode().isOrchestratorMode()) {
                drawOrchestratorMode(graphics2D);
            }
    
            // Draw the radar echo
            if (Objects.nonNull(this.radarEcho)) {
                this.radarEcho.draw(graphics2D);
            }
        }
        this.repaint.run();

    }

    public void triggerPulse() {
        if (Objects.nonNull(this.center)) {
            this.radarEcho = new RadarEcho(this.center, Signal.WIFI_MAX_COVERAGE_IN_CENTIMETERS / 2);
            this.radarEcho.start();
            this.repaint.run();
        } else {
            this.radarEcho = null;
        }
    }


    private final class RadarEcho {

        private static final Color RADAR_ECHO_COLOR = new Color(0, 255, 0, 128);
        private final Point center;
        private final int maxRadius;
        private int currentRadius = 0;
        private Timer animationTimer;

        private RadarEcho(final Point center, final int maxRadius) {
            this.center = center;
            this.maxRadius = maxRadius;
        }

        private void start() {
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
                    repaint.run();
                }
            };

            animationTimer.scheduleAtFixedRate(animationTask, 0, 10);
        }

        private void draw(final Graphics2D g2d) {
            g2d.setColor(RADAR_ECHO_COLOR); // Vert translucide pour l'écho
            g2d.drawOval(this.center.x - currentRadius, this.center.y - currentRadius, currentRadius * 2, currentRadius * 2);
        }

        private void stop() {
            if (animationTimer != null) {
                animationTimer.cancel();
                animationTimer = null;
            }
        }
    }


}
