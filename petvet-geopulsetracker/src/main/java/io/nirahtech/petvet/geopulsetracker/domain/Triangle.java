package io.nirahtech.petvet.geopulsetracker.domain;

import java.awt.Point;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class Triangle {
    private final Map<ElectronicChipBoard, Point> vertices = new HashMap<>();

    private Vertex a;
    private Vertex b;
    private Vertex c;

    private Triangle(final Distance sideAB, final Distance sideBC, final Distance sideCA, final Point startPoint) {
        this.computeDistances(List.of(sideAB, sideBC, sideCA), startPoint);
    }

    /**
     * @return the a
     */
    public Vertex getA() {
        return a;
    }

    /**
     * @return the b
     */
    public Vertex getB() {
        return b;
    }
    /**
     * @return the c
     */
    public Vertex getC() {
        return c;
    }



    /**
     * Vérifie si trois longueurs peuvent former un triangle, en tenant compte
     * d'un seuil de tolérance pour la différence acceptable entre les côtés.
     *
     * @param sideA     Longueur du premier côté du triangle.
     * @param sideB     Longueur du deuxième côté du triangle.
     * @param sideC     Longueur du troisième côté du triangle.
     * @param tolerance Tolérance pour la différence acceptable entre les côtés (en
     *                  unité spécifiée).
     * @return true si les longueurs sideA, sideB et sideC respectent l'inégalité
     *         triangulaire avec la tolérance donnée,
     *         false sinon.
     */
    private static final boolean checkTriangleInequality(final double sideA, final double sideB, final double sideC,
            final double tolerance) {
        // Trouver le côté le plus long
        double maxSide = Math.max(Math.max(sideA, sideB), sideC);

        // Calculer la somme des deux côtés les plus courts et la tolérance
        double sumOfShorterSides = sideA + sideB + sideC - maxSide;

        // Vérifier l'inégalité triangulaire avec la tolérance
        return sumOfShorterSides + tolerance > maxSide;
    }

    private void computeDistances(final List<Distance> segments, final Point startPoint) {
        final double AB = segments.get(0).computeDistanceInMeters();
        final double BC = segments.get(1).computeDistanceInMeters();
        final double CA = segments.get(2).computeDistanceInMeters();

        final Point A = new Point(startPoint.x, startPoint.y);
        this.vertices.put(segments.get(0).getFrom(), A);
        this.a = new Vertex(segments.get(0), A, segments.get(0).getFrom());

        final Point B = new Point(startPoint.x + (int) AB, startPoint.y);
        this.vertices.put(segments.get(1).getFrom(), B);
        this.b = new Vertex(segments.get(1), B, segments.get(1).getFrom());

        final double Cx = (Math.pow(CA, 2) - Math.pow(BC, 2) + Math.pow(AB, 2)) / (2 * AB);
        final double Cy = Math.sqrt(Math.pow(CA, 2) - Math.pow(Cx, 2));

        final Point C = new Point(startPoint.x + (int) Cx, startPoint.y - (int) Cy);
        this.vertices.put(segments.get(2).getFrom(), C);
        this.c = new Vertex(segments.get(2), C, segments.get(2).getFrom());

        System.out.println(String.format("A=%s, B=%s, C=%s", A, B, C));
    }

    /**
     * @return the vertices
     */
    public final Map<ElectronicChipBoard, Point> getVertices() {
        return Collections.unmodifiableMap(this.vertices);
    }

    public static final Triangle of(final Distance sideAB, final Distance sideBC, final Distance sideCA,
            final Point startPoint) {
        final double AB = sideAB.computeDistanceInMeters();
        final double BC = sideBC.computeDistanceInMeters();
        final double CA = sideCA.computeDistanceInMeters();
        // if (!checkTriangleInequality(AB, BC, CA, DecibelDistanceConverter.wifiSignalToMeters(-5))) {
        //     throw new IllegalArgumentException("Not a valid triangle");
        // }
        return new Triangle(sideAB, sideBC, sideCA, startPoint);
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[Triangle ")
                .append("vertices=")
                .append(vertices.entrySet().stream()
                        .map(entry -> String.format("point=%s(%s)",
                                ((ESP32) entry.getKey()).getWifi().getMacAddress(),
                                entry.getValue()))
                        .collect(Collectors.joining(",")))
                .append("]")
                .toString();

    }

    public record Vertex(
        Distance distance,
        Point point, 
        ElectronicChipBoard electronicChipBoard
    ) {
    }

}
