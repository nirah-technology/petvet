package io.nirahtech.petvet.geopulsetracker.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JPanel;

import io.nirahtech.petvet.geopulsetracker.domain.Cluster;
import io.nirahtech.petvet.geopulsetracker.domain.Distance;
import io.nirahtech.petvet.geopulsetracker.domain.DistanceRegistry;
import io.nirahtech.petvet.geopulsetracker.domain.ESP32;
import io.nirahtech.petvet.geopulsetracker.domain.Triangle;
import io.nirahtech.petvet.geopulsetracker.domain.TriangleCalculator;
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
            this.esp32Sprites.put(wifiBSSID, sprite);
            // this.esp32Sprites.put(esp32.getBluetooth().getMacAddress(), sprite);
        }
        return sprite;
    }

    public final void saveDistances(final ESP32Sprite scannerSprite, final Map<MacAddress, Float> scanReportResults) {
        scanReportResults.entrySet().forEach(scanReport -> {
            final MacAddress to = scanReport.getKey();
            this.cluster.filter((esp) -> {
                return esp.getWifi().getMacAddress().equals(to) || esp.getBluetooth().getMacAddress().equals(to);
            }).findAny().ifPresent((espAsNode) -> {
                // System.out.println("ESP Node found in cluster to save distance.");
                distanceRegistry.getFor(scannerSprite.getEsp32(), espAsNode).ifPresentOrElse((distance) -> {}, () -> {
                    distanceRegistry.register(scannerSprite.getEsp32(), espAsNode, scanReport.getValue());
                    distanceRegistry.register(espAsNode, scannerSprite.getEsp32(), scanReport.getValue());
                });
            });


        });

    }

    private static double calculateDistance(Point a, Point b) {
        return Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2));
    }

    /**
 * Calcule et positionne les sprites en fonction des distances enregistrées.
 */
public final void calculatePosition() {
    final List<Distance> segments = this.distanceRegistry.listUnique();
    // System.out.println("There is/are " + segments.size() + " registerd segments.");
    final List<ESP32Sprite> sprites = Collections.unmodifiableList(new ArrayList<>(this.esp32Sprites.values()));
    // System.out.println("There is/are " + sprites.size() + " sprites that represents ESP.");

    // Vérifier si le nombre de segments et de sprites est cohérent
    if (segments.size() != sprites.size()) {
        return;
    }

    // Pas de segments, placer le premier sprite au centre
    if (segments.isEmpty()) {
        sprites.get(0).setCenter(new Point(getWidth()/2, getHeight()/2));
        return;
    }

    // Un seul segment, placer les deux premiers sprites
    if (segments.size() == 1) {
        final double segmentSize = segments.get(0).computeDistanceInMeters();
        final Point startPoint = new Point((getWidth()/2) - (int)(segmentSize/2), getHeight()/2);
        sprites.get(0).setCenter(startPoint);
        sprites.get(1).setCenter(new Point(startPoint.x + (int)segmentSize, startPoint.y));
        return;
    }

    // Deux segments, calculer et placer les trois premiers sprites
    if (segments.size() == 2) {
        final double AB = segments.get(0).computeDistanceInMeters();
        final double AC = segments.get(1).computeDistanceInMeters();

        final Point A = new Point(getWidth()/2, getHeight()/2);
        final Point B = new Point((int) (A.x+AB), A.y);
        final double angle = 90; // Vous pouvez ajuster l'angle selon vos besoins
        final Point C = new Point((int) (A.x+ (AC * Math.cos(Math.toRadians(angle)))),
                                  (int) (A.y + (AC * Math.sin(Math.toRadians(angle)))));
        // System.out.println("A=" + A);
        // System.out.println("B=" + B);
        // System.out.println("C=" + C);
        // System.out.println("[AB]=" + segments.get(0).computeDistanceInMeters());
        // System.out.println("[BC]=" + segments.get(1).computeDistanceInMeters());
        // System.out.println("/!\\ [CA]=" + segments.get(2).computeDistanceInMeters());
        // System.out.println("/!\\ [CA]=" + calculateDistance(A, C));
        sprites.get(0).setCenter(A);
        sprites.get(1).setCenter(B);
        sprites.get(2).setCenter(C);
        return;
    }



    // System.out.println("Triangle computation must be executed...");
    final Set<Triangle> triangles = TriangleCalculator.asTrianglesSet(new HashSet<>(segments));
    // System.out.println("There is/are " + triangles.size() + " created triangles.");

    sprites.forEach(sprite -> {
        // System.out.println("Must refresh the sprite for: " + sprite.getEsp32().getWifi().getMacAddress());
        triangles.forEach(triangle -> {
            triangle.getVertices().forEach((esp, point) -> {
                if (esp.equals(sprite.getEsp32())) {
                    if (Objects.nonNull(sprite.getCenter())) {
                        if (sprite.getCenter().equals(point)) {
                            segments.stream().filter(segment -> segment.getFrom().equals(sprite.getEsp32())).findFirst().ifPresent((segment) -> {
                                // System.out.println("Point has move for:" + sprite.getEsp32().getWifi().getMacAddress() + " ("+segment.getSignalStrenghtInDBM()+" dBm)");
                            });
                        }
                    }
                    sprite.setCenter(point);
                }
            });
        });
    });
}

    public void triggerPulse(ESP32Sprite sprite) {
        sprite.triggerPulse();
        repaint();

    }
}
