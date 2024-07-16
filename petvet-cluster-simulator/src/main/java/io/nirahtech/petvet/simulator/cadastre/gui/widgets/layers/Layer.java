package io.nirahtech.petvet.simulator.cadastre.gui.widgets.layers;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;
import java.util.Optional;

import io.nirahtech.petvet.simulator.cadastre.domain.Surface;

public interface Layer {
    boolean isVisible();
    void hide();
    void display();


    boolean isLocked();
    void lock();
    void unlock();

    Color getBorderColor();
    Color getFillColor();
    LinkedList<Point> getPoints();

    void selectPoint(final Point point);
    Optional<Point> getSelectedPoint();
    void unselectPoint();

    int getOrder();
    void setOrder(int newOrder);

    Surface getSurface();
    void setSurface(Surface surface);
}
