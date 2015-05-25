package com.batalgorithm.main;

import Jama.Matrix;

import java.util.List;

/**
 * Параметры, заданные для монтажной платы и ее элементов.
 */
public class CircuitBoardInformation {

    private int circuitBoardA;
    private int circuitBoardB;
    private int restrictedAreaX;
    private int restrictedAreaY;
    private int restrictedAreaA;
    private int restrictedAreaB;
    private int minDistance;
    private List<Element> elementList;
    private Matrix adjMatrix;

    public int getCircuitBoardA() {
        return circuitBoardA;
    }

    public void setCircuitBoardA(int circuitBoardA) {
        this.circuitBoardA = circuitBoardA;
    }

    public int getCircuitBoardB() {
        return circuitBoardB;
    }

    public void setCircuitBoardB(int circuitBoardB) {
        this.circuitBoardB = circuitBoardB;
    }

    public int getRestrictedAreaX() {
        return restrictedAreaX;
    }

    public void setRestrictedAreaX(int restrictedAreaX) {
        this.restrictedAreaX = restrictedAreaX;
    }

    public int getRestrictedAreaY() {
        return restrictedAreaY;
    }

    public void setRestrictedAreaY(int restrictedAreaY) {
        this.restrictedAreaY = restrictedAreaY;
    }

    public int getRestrictedAreaA() {
        return restrictedAreaA;
    }

    public void setRestrictedAreaA(int restrictedAreaA) {
        this.restrictedAreaA = restrictedAreaA;
    }

    public int getRestrictedAreaB() {
        return restrictedAreaB;
    }

    public void setRestrictedAreaB(int restrictedAreaB) {
        this.restrictedAreaB = restrictedAreaB;
    }

    public int getMinDistance() {
        return minDistance;
    }

    public void setMinDistance(int minDistance) {
        this.minDistance = minDistance;
    }

    public List<Element> getElementList() {
        return elementList;
    }

    public void setElementList(List<Element> elementList) {
        this.elementList = elementList;
    }

    public Matrix getAdjMatrix() {
        return adjMatrix;
    }

    public void setAdjMatrix(Matrix adjMatrix) {
        this.adjMatrix = adjMatrix;
    }

    public boolean isValid() {
        return circuitBoardA > 0 && circuitBoardB > 0 && restrictedAreaX > 0 && restrictedAreaY > 0 &&
                restrictedAreaA > 0 && restrictedAreaB > 0 && minDistance > 0 && elementList != null &&
                !elementList.isEmpty() && adjMatrix != null;
    }
}
