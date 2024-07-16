package io.nirahtech.petvet.simulator.cadastre.domain;

import java.awt.Point;
import java.util.Collection;

public record Building(
    Segment[] sides,
    Collection<Point> vertices
) implements Surface {

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
