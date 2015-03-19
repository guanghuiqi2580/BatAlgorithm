package com.batalgorithm.main;

import java.awt.*;

/**
 * Предназначен для объектоного отображения запретной зоны.
 */
public class RestrictedArea extends Rectangle {

    public RestrictedArea(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public String toString() {
        return "RestrictedArea{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", a=" + getWidth() +
                ", b=" + getHeight() +
                '}';
    }

    public double getArea() {
        return width * height;
    }
}
