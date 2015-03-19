package com.batalgorithm.main;

import java.awt.*;

public class CircuitBoard extends Rectangle {

    public CircuitBoard(int width, int height) {
        super(0, 0, width, height);
    }

    @Override
    public String toString() {
        return "CircuitBoard{" +
                "x=" + getX() +
                ", y=" + getY() +
                ", a=" + getWidth() +
                ", b=" + getHeight() +
                '}';
    }

    public int getArea() {
        return (int) (getWidth() * getHeight());
    }
}
