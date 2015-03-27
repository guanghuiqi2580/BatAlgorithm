package com.batalgorithm.main;

public class Rectangle {

    private int centerX;
    private int centerY;
    private int width;
    private int height;

    public Rectangle(int centerX, int centerY, int width, int height) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.width = width;
        this.height = height;
    }

    public Rectangle(Rectangle rectangle) {
        this.centerX = rectangle.getCenterX();
        this.centerY = rectangle.getCenterY();
        this.width = rectangle.getWidth();
        this.height = rectangle.getHeight();
    }

    public int getCenterX() {
        return centerX;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getMinX() {
        return getCenterX() - getWidth() / 2;
    }

    public int getMinY() {
        return getCenterY() - getHeight() / 2;
    }

    public int getMaxX() {
        return getCenterX() + getWidth() / 2;
    }

    public int getMaxY() {
        return getCenterY() + getHeight() / 2;
    }

    public boolean intersects(Rectangle rectangle, int distance) {
        Rectangle safeZone = new Rectangle(this.getCenterX(), this.getCenterY(), this.getWidth() + distance * 2,
                this.getHeight() + distance * 2);
        java.awt.Rectangle safeZoneAwt = new java.awt.Rectangle(safeZone.getMinX(), safeZone.getMinY(),
                safeZone.getWidth(), safeZone.getHeight());
        java.awt.Rectangle rectangleAwt = new java.awt.Rectangle(rectangle.getMinX(), rectangle.getMinY(),
                rectangle.getWidth(), rectangle.getHeight());
        return safeZoneAwt.intersects(rectangleAwt);
    }

    public boolean contains(Rectangle rectangle) {
        java.awt.Rectangle thisRectangleAwt = new java.awt.Rectangle(getMinX(), getMinY(), getWidth(),getHeight());
        java.awt.Rectangle rectangleAwt = new java.awt.Rectangle(rectangle.getMinX(), rectangle.getMinY(),
                rectangle.getWidth(),rectangle.getHeight());
        return thisRectangleAwt.contains(rectangleAwt);
    }

    @Override public String toString() {
        return "Rectangle{" +
                "centerX=" + centerX +
                ", centerY=" + centerY +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
