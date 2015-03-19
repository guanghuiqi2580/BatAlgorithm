package com.batalgorithm.main;

import java.awt.*;

/**
 * Объектное отображение элемента, предназначенного для размещения на монтажной плате.
 */
public class Element extends Rectangle {

    private int number;

    public Element(int number, int x, int y, int width, int height) {
        super(x, y, width, height);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public int getArea() {
        return width * height;
    }

    @Override
    public String toString() {
        return "Element{" +
                "number=" + number +
                ", x=" + getX() +
                ", y=" + getY() +
                ", width=" + getWidth() +
                ", height=" + getHeight() +
                '}';
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
