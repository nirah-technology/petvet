package io.nirahtech.petvet.simulator.electronicalcard.gui.leftpanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class StatusIcon implements Icon {

    private static final int ICON_WIDTH = 10;
    private static final int ICON_HEIGHT = ICON_WIDTH;

    private final boolean isEnabled;

    public StatusIcon(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    @Override
    public void paintIcon(Component component, Graphics graphics, int x, int y) {
        Color color = isEnabled ? Color.GREEN : Color.GRAY;
        graphics.setColor(color);
        graphics.fillOval(x, y, getIconWidth(), getIconHeight());
    }

    @Override
    public int getIconWidth() {
        return ICON_WIDTH;
    }

    @Override
    public int getIconHeight() {
        return ICON_HEIGHT;
    }
}
