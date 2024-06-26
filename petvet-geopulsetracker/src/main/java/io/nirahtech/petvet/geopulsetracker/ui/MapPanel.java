package io.nirahtech.petvet.geopulsetracker.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

import io.nirahtech.petvet.geopulsetracker.domain.Cluster;
import io.nirahtech.petvet.geopulsetracker.domain.ESP32;
import io.nirahtech.petvet.messaging.util.MacAddress;

public class MapPanel extends JPanel {
    private final DistanceRegistry distanceRegistry = new DistanceRegistry();

    private final Map<MacAddress, ESP32Sprite> esp32Sprites = new ConcurrentHashMap<>();
    private final Cluster<ESP32> cluster;

    public MapPanel(Cluster<ESP32> cluster) {
        this.setBackground(Color.BLACK);
        this.cluster = cluster;
        this.setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        setBackground(Color.BLACK);

        final Graphics2D graphics2D = (Graphics2D) graphics.create();
        this.esp32Sprites.values().forEach(sprite -> sprite.draw(graphics2D));
        graphics2D.dispose();
    }

    public ESP32Sprite useAsSprite(ESP32 esp32) {
        final MacAddress wifiBSSID = esp32.getWifi().getMacAddress();
        final MacAddress bluetoothBSSID = esp32.getBluetooth().getMacAddress();
        ESP32Sprite sprite = null;
        if (this.esp32Sprites.containsKey(wifiBSSID) && this.esp32Sprites.containsKey(bluetoothBSSID)) {
            if (this.esp32Sprites.get(bluetoothBSSID).equals(this.esp32Sprites.get(wifiBSSID))) {
                sprite = this.esp32Sprites.get(wifiBSSID);
            }
        }
        if (Objects.isNull(sprite)) {
            sprite = new ESP32Sprite(esp32, this::repaint);
            this.esp32Sprites.put(esp32.getWifi().getMacAddress(), sprite);
            this.esp32Sprites.put(esp32.getBluetooth().getMacAddress(), sprite);
        }
        return sprite;
    }

    public final void saveDistances(ESP32Sprite scannerSprite, Map<MacAddress, Float> scanReportResults) {
        scanReportResults.entrySet().forEach(scanReport -> {
            final MacAddress to = scanReport.getKey();
            final ESP32 node = ESP32.getOrCreateWithWiFiMacAddress(to);
            distanceRegistry.register(scannerSprite.getEsp32(), node, scanReport.getValue());
            distanceRegistry.register(node, scannerSprite.getEsp32(), scanReport.getValue());
            if (!this.cluster.contains(node)) {
                this.cluster.add(node);
            }
        });

    }

    public final void calculatePosition(ESP32Sprite scannerSprite) {
        
        final Set<Distance> allDistancesFromScanner = distanceRegistry.getFrom(scannerSprite.getEsp32().getWifi().getMacAddress());
        if (allDistancesFromScanner.isEmpty()) {
            if (Objects.isNull(scannerSprite.getCenter())) {
                scannerSprite.setCenter(new Point((int) getWidth()/2, (int) getHeight()/2));
            }
        } else {
            if (Objects.isNull(scannerSprite.getCenter())) {
                scannerSprite.setCenter(new Point((int) getWidth()/2, (int) getHeight()/2));
            }
            this.esp32Sprites
                    .values()
                    .stream()
                    .filter(sprite -> !sprite.equals(scannerSprite))
                    .forEach(nodeSprite -> {
                        if (Objects.isNull(nodeSprite.getCenter())) {
                            distanceRegistry.getFor(scannerSprite.getEsp32(), nodeSprite.getEsp32())
                                    .ifPresent(distance -> {
                                        nodeSprite.setCenter(new Point((int)(scannerSprite.getCenter().x + distance.computeDistanceInMeters()), scannerSprite.getCenter().y));
                                    });
                        }
                    });
        }
    }

    public void triggerPulse(ESP32Sprite sprite) {
        sprite.triggerPulse();
        repaint();

    }
}
