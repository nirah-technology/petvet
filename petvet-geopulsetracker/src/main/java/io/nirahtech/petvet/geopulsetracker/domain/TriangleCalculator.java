package io.nirahtech.petvet.geopulsetracker.domain;

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import io.nirahtech.petvet.messaging.util.MacAddress;

public final class TriangleCalculator {

    private static final Set<Triangle> TRIANGLES_CACHE = new HashSet<>();

    private TriangleCalculator() { }

    private static boolean areLinked(Distance segment1, Distance segment2) {
        final MacAddress from1 = ((ESP32) segment1.getFrom()).getWifi().getMacAddress();
        final MacAddress to1 = ((ESP32) segment1.getTo()).getWifi().getMacAddress();
        final MacAddress from2 = ((ESP32) segment2.getFrom()).getWifi().getMacAddress();
        final MacAddress to2 = ((ESP32) segment2.getTo()).getWifi().getMacAddress();
        // System.out.println(String.format("%s = %s -> %s", from1, to2, from1.equals(to2)));
        // System.out.println(String.format("%s = %s -> %s", to1, from2, to1.equals(from2)));
        // System.out.println(String.format("%s = %s -> %s", to1, to2, to1.equals(to2)));
        // System.out.println(String.format("%s = %s -> %s", to1, from2, from2.equals(from2)));
        return (from1.equals(to2) || to1.equals(from2) || to1.equals(to2) || from1.equals(from2));
    }

    public static final Set<Triangle> asTrianglesSet(Set<Distance> distances) {
        final Set<Triangle> triangles = new HashSet<>();
        final Map<ElectronicChipBoard, Point> knownPoints = new HashMap<>();
        final Map<Distance, Point> knowDistancesAsVertices = new HashMap<>();
        // System.out.println("Distances for the triangles to create...");
        // distances.forEach(System.out::println);

        AB_LOOP: for (Distance sideAB : distances) {
            if (!knowDistancesAsVertices.containsKey(sideAB)) {
                // System.out.println(String.format("AB(%s-%s)=%s", ((ESP32)sideAB.getFrom()).getWifi().getMacAddress(), ((ESP32)sideAB.getTo()).getWifi().getMacAddress(), sideAB.computeDistanceInMeters()));
                BC_LOOP: for (Distance sideBC : distances) {
                    if (!sideBC.equals(sideAB)) {
                        // System.out.println("Check link for AB and BC...");
                        if (areLinked(sideAB, sideBC)) {
                            // System.out.println("AB is linked to BC");
                            // System.out.println(String.format("BC(%s-%s)=%s", ((ESP32)sideBC.getFrom()).getWifi().getMacAddress(), ((ESP32)sideBC.getTo()).getWifi().getMacAddress(), sideBC.computeDistanceInMeters()));
                            CA_LOOP: for (Distance sideCA : distances) {
                                if (!sideCA.equals(sideAB) && !sideCA.equals(sideBC)) {
                                    // System.out.println("Check link for BC and CA...");
                                    if (areLinked(sideBC, sideCA) || areLinked(sideAB, sideCA)) {
                                        // System.out.println("BC is linked to CA");
                                        if (areLinked(sideCA, sideAB) && areLinked(sideAB, sideBC)) { // Je suis persuadé qu'il manque une vérification d'égalité 
                                            // System.out.println(String.format("CA(%s-%s)=%s", ((ESP32)sideCA.getFrom()).getWifi().getMacAddress(), ((ESP32)sideCA.getTo()).getWifi().getMacAddress(), sideCA.computeDistanceInMeters()));
                                            Point startPoint;
                                            if (knownPoints.containsKey(sideAB.getFrom())) {
                                                startPoint = knownPoints.get(sideAB.getFrom());
                                            } else if (knownPoints.containsKey(sideAB.getTo())) {
                                                startPoint = knownPoints.get(sideAB.getTo());
                                            } else if (knownPoints.containsKey(sideBC.getTo())) {
                                                startPoint = knownPoints.get(sideBC.getTo());
                                            } else {
                                                startPoint = new Point(0, 0);
                                            }
                                            try {
                                                searchInCache(sideAB, sideBC, sideCA).ifPresentOrElse(triangles::add, () -> {
                                                    // System.out.println("A new triangle is found !");
                                                    Triangle triangle = Triangle.of(sideAB, sideBC, sideCA, startPoint);
                                                    TRIANGLES_CACHE.add(triangle);
                                                    triangles.add(triangle);
                                                    knownPoints.putAll(triangle.getVertices());
                                                    knowDistancesAsVertices.put(triangle.getA().distance(), triangle.getA().point());
                                                    knowDistancesAsVertices.put(triangle.getB().distance(), triangle.getB().point());
                                                    knowDistancesAsVertices.put(triangle.getC().distance(), triangle.getC().point());
                                                });
                                                break BC_LOOP;
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }   
            }
        }
        
        return triangles;
    }

    private static final Optional<Triangle> searchInCache(final Distance sideA, final Distance sideB, final Distance sideC) {
        return TRIANGLES_CACHE.stream().filter(triangle -> {
            boolean isSelected = false;
            if (sideA.equals(triangle.getA().distance()) && sideB.equals(triangle.getB().distance()) && sideC.equals(triangle.getC().distance())) {
                // System.out.println("Triangle in cache that match is found!!");
                isSelected = true;
            }
            return isSelected;
        }).findFirst();
    }

    public static final Set<Triangle> cache() {
        return Collections.unmodifiableSet(TRIANGLES_CACHE);
    }
}
