package com.batalgorithm.main;

public class Element extends Rectangle {

    private int number;

    public Element(int number, int centerX, int centerY, int width, int height) {
        super(centerX, centerY, width, height);
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getArea() {
        return getWidth() * getHeight();
    }

    protected Element copy() {
        return new Element(getNumber(), getCenterX(), getCenterY(), getWidth(), getHeight());
    }
}
