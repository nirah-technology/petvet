package io.nirahtech.petvet.simulator.land.gui;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

public final class CornerSprite {
    private static final int CORNER_RADIUS = 10;

    private final Point point;

    private boolean isHover = false;
    private boolean isSelected = false;
    private boolean mustDisplayOverlayDisk = false;

    private final Layer layer;


    public CornerSprite(final Point point, final Layer layer) {
        this.layer = layer;
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public boolean isHover(final Point otherPoint) {
        final double distance = Math.sqrt(Math.pow(otherPoint.getX() - this.point.getX(), 2) + Math.pow(otherPoint.getY() - this.point.getY(), 2));
        return distance <= CORNER_RADIUS;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void paintComponent(Graphics graphics) {
        final int x = this.point.x - CORNER_RADIUS;
        final int y = this.point.y - CORNER_RADIUS;
        final int width = CORNER_RADIUS * 2;
        final int height = width;

        graphics.setColor(this.layer.getBorderColor());
        if (this.mustDisplayOverlayDisk) {
            graphics.fillOval(x, y, width, height);
        }
        final Graphics2D graphics2D = (Graphics2D) graphics;
        float thickness = 1.0F;
        if (this.isSelected) {
            thickness = 5.0f;    
        }
        graphics2D.setStroke(new BasicStroke(thickness));

        graphics2D.drawOval(x, y, width, height);
    }

    public void displayOverlayDisk() {
        this.mustDisplayOverlayDisk = true;
    }

    public void hideOverlayDisk() {
        this.mustDisplayOverlayDisk = false;
    }

    public void select() {
        this.isSelected = true;
    }

    public void unselect() {
        this.isSelected = false;
    }
}
