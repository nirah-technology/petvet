package io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Optional;

import io.nirahtech.petvet.simulator.cadastre.domain.Surface;

abstract class AbstractLayer implements Layer {
    protected final LinkedList<Point> points;
    protected Point selectedPoint;
    protected final Color borderColor;
    protected final Color fillColor;

    private Surface surface;
    
    private boolean isLocked = false;
    private boolean isVisible = true;
    private int order;


    protected AbstractLayer(int order, final Color borderColor, final Color fillColor) {
        this.borderColor = borderColor;
        this.fillColor = fillColor;
        this.order = order;
        this.points = new LinkedList<>();
    }

    @Override
    public final Color getBorderColor() {
        return this.borderColor;
    }

    @Override
    public final Color getFillColor() {
        return this.fillColor;
    }

    @Override
    public LinkedList<Point> getPoints() {
        return this.points;
    }

    @Override
    public void display() {
        this.isVisible = true;
    }

    @Override
    public void hide() {
        this.isVisible = false;
    }

    @Override
    public boolean isLocked() {
        return this.isLocked;
    }

    @Override
    public boolean isVisible() {
        return this.isVisible;
    }

    @Override
    public void lock() {
        this.isLocked = true;
    }

    @Override
    public void unlock() {
        this.isLocked = false;
    }

    @Override
    public int getOrder() {
        return this.order;
    }
    @Override
    public void setOrder(int newOrder) {
        this.order = newOrder;
    }

    @Override
    public void selectPoint(Point point) {
        if (this.points.contains(point)) {
            this.selectedPoint = point;
        }
    }

    @Override
    public void unselectPoint() {
        this.selectedPoint = null;
    }

    @Override
    public Optional<Point> getSelectedPoint() {
        return Optional.ofNullable(this.selectedPoint);
    }

    @Override
    public Surface getSurface() {
        return this.surface;
    }

    @Override
    public void setSurface(Surface surface) {
        this.surface = surface;
    }
    
}
