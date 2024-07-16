package io.nirahtech.petvet.simulator.cadastre.domain;

import java.awt.Point;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

public record Land(
        Segment[] sides,
        Collection<Point> vertices,
        Building... buildings) implements Surface, BuiltSurface {

    @Override
    public double calculateArea() {
        double area = 0.0D;
        final int totalVertices = this.vertices.size();
        final Point[] points = vertices.toArray(new Point[0]);
        for (int index = 0; index < totalVertices; index++) {
            Point p1 = points[index];
            Point p2 = points[(index + 1) % totalVertices];
            area += p1.x * p2.y - p2.x * p1.y;
        }
        return Math.abs(area) / 2.0;

    }

    @Override
    public double calculatePerimeter() {
        double perimeter = 0.0D;
        for (Segment segment : sides) {
            perimeter += segment.length();
        }
        return perimeter;
    }

    @Override
    public double calculateBuiltSurface() {
        double builtSurface = 0.0;
        for (Building building : buildings) {
            builtSurface += building.calculateArea();
        }
        return builtSurface;

    }

    @Override
    public double calculateBuiltPerimeter() {
        double builtPerimeter = 0.0;
        // Liste pour suivre les segments déjà comptés pour éviter de les compter
        // plusieurs fois
        Set<Segment> countedSegments = new HashSet<>();

        // Parcourir les côtés du terrain
        for (Segment segment : sides) {
            boolean segmentBelongsToBuilding = false;

            // Vérifier si le segment appartient à un des bâtiments
            for (Building building : buildings) {
                for (Segment buildingSegment : building.sides()) {
                    if (segment.equals(buildingSegment) && !countedSegments.contains(segment)) {
                        segmentBelongsToBuilding = true;
                        countedSegments.add(segment);
                        break;
                    }
                }
                if (segmentBelongsToBuilding) {
                    break;
                }
            }

            // Si le segment appartient à un bâtiment, ajouter sa longueur au périmètre
            // construit
            if (segmentBelongsToBuilding) {
                builtPerimeter += segment.length();
            }
        }

        return builtPerimeter;
    }
}
