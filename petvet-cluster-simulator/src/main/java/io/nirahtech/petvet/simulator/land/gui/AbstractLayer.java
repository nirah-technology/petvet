package io.nirahtech.petvet.simulator.land.gui;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;

abstract class AbstractLayer implements Layer {
    protected final LinkedList<Point> points;
    protected final Color borderColor;
    protected final Color fillColor;

    private boolean isLocked = false;
    private boolean isVisible = true;


    protected AbstractLayer(final Color borderColor, final Color fillColor) {
        this.borderColor = borderColor;
        this.fillColor = fillColor;
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
    
}
