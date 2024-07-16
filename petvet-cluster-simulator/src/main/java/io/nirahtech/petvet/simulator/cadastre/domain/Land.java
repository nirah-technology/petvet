package io.nirahtech.petvet.simulator.cadastre.domain;

import java.awt.Point;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class Land implements Surface, BuiltSurface {

    private final Collection<Segment> sides;
    private final Collection<Point> vertices;
    private final Collection<Building> buildings;

    public Land() {
        this.sides = new HashSet<>();
        this.vertices = new HashSet<>();
        this.buildings = new HashSet<>();
    }

    public Land(final Segment[] sides, final Collection<Point> vertices) {
        this();
        for (Segment side : sides) {
            this.sides.add(side);
        }
        this.vertices.addAll(vertices);
    }

    public void setSides(Collection<Segment> sides) {
        this.sides.addAll(sides);
    }
    public void setVertices(Collection<Point> vertices) {
        this.vertices.addAll(vertices);
    }

    public Collection<Building> getBuildings() {
        return Collections.unmodifiableCollection(this.buildings);
    }

    public Collection<Segment> getSides() {
        return Collections.unmodifiableCollection(this.sides);
    }

    public Collection<Point> getVertices() {
        return Collections.unmodifiableCollection(this.vertices);
    }


    public void addBuilding(final Building building) {
        if (Objects.nonNull(building)) {
            this.buildings.add(building);
        }
    }

    public void removeBuilding(final Building building) {
        if (Objects.nonNull(building) && this.buildings.contains(building)) {
            this.buildings.remove(building);
        }
    }

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
