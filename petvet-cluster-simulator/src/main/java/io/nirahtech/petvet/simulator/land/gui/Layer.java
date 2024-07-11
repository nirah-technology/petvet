package io.nirahtech.petvet.simulator.land.gui;

import java.awt.Color;
import java.awt.Point;
import java.util.LinkedList;

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

}
