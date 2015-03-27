package com.batalgorithm.main;

public class CircuitBoard extends Rectangle {

    public CircuitBoard(int centerX, int centerY, int width, int height) {
        super(centerX, centerY, width, height);
    }

    public CircuitBoard(int width, int height) {
        super(width / 2, height / 2, width, height);
    }

}
