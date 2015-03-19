package com.batalgorithm.main;

import Jama.Matrix;
import com.batalgorithm.utils.MatlabSubstitute;

import java.util.ArrayList;
import java.util.List;

import static com.batalgorithm.utils.MatrixHelper.getRow;
import static com.batalgorithm.utils.MatrixHelper.setRow;

public class BatAlgorithm {

    private int n = 20; // Численность населения, как правило, от 10 до 40
    private int N_gen = 1000; // Количество поколений
    private double A = 0.5; // Громкость (постоянная или уменьшающаяся)
    private double r = 0.5; // Частота (постоянная или уменьшающаяся)

    private int Qmin = 0; // минимальная частота
    private int Qmax = 2; // максимальная частота

    private int N_iter; // Общее количество вычисления функции

    private int d; // Количество элементов

    private Matrix Q; // Частота
    private Matrix v; // Скорость

    private Matrix SolX;
    private Matrix SolY;
    private double fmin;
    private int I;
    private Matrix bestX;
    private Matrix bestY;

    private CircuitBoard circuitBoard;
    private RestrictedArea restrictedArea;
    private Matrix adjMatrix;
    private List<Element> elementList;
    private int minDistance;

    public BatAlgorithm(CircuitBoard circuitBoard, RestrictedArea restrictedArea, Matrix adjMatrix, List<Element>
            elementList, int minDistance) {

        this.circuitBoard = circuitBoard;
        this.restrictedArea = restrictedArea;
        this.adjMatrix = adjMatrix;
        this.elementList = elementList;
        this.minDistance = minDistance;

        // Задаем количество элементов
        d = elementList.size();

        // Инициализируем массивы частоты и скорости нулями
        Q = MatlabSubstitute.zeros(n, 1);
        v = MatlabSubstitute.zeros(n, d);

        // Инициализация населения / решений
        SolX = new Matrix(n, d);
        SolY = new Matrix(n, d);

        boolean packed;
        for (int i = 0; i < n; i++) {
            List<Element> packElementList = null;
            try {
                packElementList = move(elementList);
                packed = true;
            } catch (RuntimeException e) {
                packed = false;
            }
            if (packed) {
                Matrix matrixX = convertToXRow(packElementList);
                Matrix matrixY = convertToYRow(packElementList);
                setRow(SolX, matrixX, i);
                setRow(SolY, matrixY, i);
            }
        }

        // Находим начальное лучшее решение
        fmin = findMinLength(SolX, SolY, adjMatrix);
        I = findMinIndex(SolX, SolY, adjMatrix);
        bestX = getRow(SolX, I);
        bestY = getRow(SolY, I);
    }

    private int findMinIndex(Matrix solX, Matrix solY, Matrix adjMatrix) {
        int rowDimension = solX.getRowDimension();
        int columnDimension = solX.getColumnDimension();
        double[] lengths = new double[rowDimension];
        for (int r = 0; r < rowDimension; r++) {
            double currLength = 0;
            for (int c = 0; c < columnDimension; c++) {
                double currX = solX.get(r, c);
                double currY = solY.get(r, c);
                for (int cNext = 0; cNext < columnDimension; cNext++) {
                    double numberOfLinks = adjMatrix.get(c, cNext);
                    if (c != 0) {
                        double nextX = solX.get(r, cNext);
                        double nextY = solY.get(r, cNext);
                        double d = Math.sqrt(Math.pow(currX - nextX, 2) + Math.pow(currY - nextY, 2));
                        currLength += d * numberOfLinks;
                    }
                }
            }
            lengths[r] = currLength;
        }
        double min = Double.MAX_VALUE;
        int index = 0;
        for (int i = 0; i < lengths.length; i++) {
            if (lengths[i] < min) {
                min = lengths[i];
                index = i;
            }
        }
        return index;
    }

    private double findMinLength(Matrix solX, Matrix solY, Matrix adjMatrix) {
        int rowDimension = solX.getRowDimension();
        int columnDimension = solX.getColumnDimension();
        double[] lengths = new double[rowDimension];
        for (int r = 0; r < rowDimension; r++) {
            double currLength = 0;
            for (int c = 0; c < columnDimension; c++) {
                double currX = solX.get(r, c);
                double currY = solY.get(r, c);
                for (int cNext = 0; cNext < columnDimension; cNext++) {
                    double numberOfLinks = adjMatrix.get(c, cNext);
                    if (numberOfLinks != 0) {
                        double nextX = solX.get(r, cNext);
                        double nextY = solY.get(r, cNext);
                        double d = Math.sqrt(Math.pow(currX - nextX, 2) + Math.pow(currY - nextY, 2));
                        currLength += d * numberOfLinks;
                    }
                }
            }
            lengths[r] = currLength;
        }
        double min = Double.MAX_VALUE;
        for (double length : lengths) {
            if (length < min) {
                min = length;
            }
        }
        return min;
    }

    private Matrix convertToXRow(List<Element> packElementList) {
        Matrix rowX = new Matrix(1, packElementList.size());
        for (int i = 0; i < packElementList.size(); i++) {
            Element element = packElementList.get(i);
            rowX.set(0, i, element.getCenterX());
        }
        return rowX;
    }

    private Matrix convertToYRow(List<Element> packElementList) {
        Matrix rowY = new Matrix(1, packElementList.size());
        for (int i = 0; i < packElementList.size(); i++) {
            Element element = packElementList.get(i);
            rowY.set(0, i, element.getCenterY());
        }
        return rowY;
    }

    public List<Element> pack() {
        double sumArea = 0;
        for (Element e : elementList) {
            sumArea += e.getArea() + minDistance * e.getWidth() + minDistance * e.getHeight();
        }
        sumArea += restrictedArea.getArea();
        if (sumArea >= circuitBoard.getArea()) {
            throw new IllegalArgumentException("Summary area of all elements or greater than area of circuit board.");
        }
        List<Element> packElementList = new ArrayList<>();
        for (Element e : elementList) {
            Element currElement = new Element(e.getNumber(), (int) e.getX(), (int) e.getY(), (int) e.getWidth(),
                    (int) e.getHeight());
            boolean placed = false;
            boolean rotate = false;
            int x = 0;
            int y = 0;
            do {
                currElement.setX(x);
                currElement.setY(y);

                if (circuitBoard.contains(currElement) && !restrictedArea.intersects(currElement)) {
                    boolean intersectWithElement = false;
                    for (Element packElement : packElementList) {
                        Element safeZone = new Element(packElement.getNumber(),
                                (int) packElement.getX() - minDistance,
                                (int) packElement.getY() - minDistance,
                                (int) packElement.getWidth() + minDistance * 2,
                                (int) packElement.getHeight() + minDistance * 2);
                        if (currElement.intersects(safeZone)) {
                            intersectWithElement = true;
                        }
                    }
                    if (!intersectWithElement) {
                        packElementList.add(currElement);
                        placed = true;
                    }
                }
                if (x < circuitBoard.getWidth()) {
                    x++;
                } else if (y < circuitBoard.getHeight()) {
                    x = 0;
                    y++;
                } else {
                    if (rotate) {
                        throw new RuntimeException("Can not place element: " + currElement);
                    } else {
                        currElement = new Element(currElement.getNumber(), (int) currElement.getX(), (int) currElement
                                .getY(), (int) currElement.getHeight(), (int) currElement.getWidth());
                        x = 0;
                        y = 0;
                        rotate = true;
                    }
                }
            } while (!placed);
        }
        return packElementList;
    }

    private List<Element> move(List<Element> elementList) {
        List<Element> moveElementList = new ArrayList<>();
        for (Element e : elementList) {
            Element currElement = new Element(e.getNumber(), (int) e.getX(), (int) e.getY(), (int) e.getWidth(),
                    (int) e.getHeight());
            boolean placed = false;
            int count = 0;
            do {
                currElement.setX((int) (e.getX() + (int) (Math.random())));
                currElement.setY((int) (e.getX() + (int) (Math.random())));
                if (circuitBoard.contains(currElement) && !restrictedArea.intersects(currElement)) {
                    boolean intersectWithElement = false;
                    for (Element packElement : moveElementList) {
                        Element safeZone = new Element(packElement.getNumber(),
                                (int) packElement.getX() - minDistance,
                                (int) packElement.getY() - minDistance,
                                (int) packElement.getWidth() + minDistance * 2,
                                (int) packElement.getHeight() + minDistance * 2);
                        if (currElement.intersects(safeZone)) {
                            intersectWithElement = true;
                        }
                    }
                    if (!intersectWithElement) {
                        moveElementList.add(currElement);
                        placed = true;
                    }
                }
                count++;
                if (count > 50) {
                    moveElementList.add(e);
                }
            } while (!placed && count < 50);
        }
        return moveElementList;
    }

    public Matrix getBestY() {
        return bestY;
    }

    public Matrix getBestX() {
        return bestX;
    }

    public double getFmin() {
        return fmin;
    }

    public void calculate() {
        //TODO Разместить основную логику поиска лучшего решения здесь
    }
}
