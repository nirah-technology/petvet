package io.nirahtech.petvet.simulator.cadastre.domain;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

public final class Building implements Surface {
    private final Collection<Segment> sides;
    private final Collection<Point> vertices;

    public Building() {
        this.sides = new HashSet<>();
        this.vertices = new ArrayList<>();
    }

    public final void setSides(final Collection<Segment> sides) {
        this.sides.addAll(sides);
    }

    public final void setVertices(final Collection<Point> vertices) {
        this.vertices.addAll(vertices);
    }

    /**
     * @return the sides
     */
    public Collection<Segment> getSides() {
        return Collections.unmodifiableCollection(this.sides);
    }

    /**
     * @return the vertices
     */
    public Collection<Point> getVertices() {
        return Collections.unmodifiableCollection(this.vertices);
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
    
}
