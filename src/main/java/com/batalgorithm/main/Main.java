package com.batalgorithm.main;

import Jama.Matrix;
import com.batalgorithm.utils.InputHelper;
import com.batalgorithm.utils.MatrixHelper;
import com.batalgorithm.utils.PrintHelper;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        System.out.println(PrintHelper.getDelimiter());
        System.out.println(PrintHelper.divide("Program Batman v0.1 design for deployment of electronic circuit board" +
                " according to the bat inspiring algorithm."));
        System.out.println(PrintHelper.getDelimiter());

        System.out.println("Input circuit board size: ");
        System.out.print("A: ");
        int circuitBoardA = InputHelper.inputPositiveInt();
        System.out.print("B: ");
        int circuitBoardB = InputHelper.inputPositiveInt();

        System.out.println("Input coordinates of the restricted area center: ");
        System.out.print("X: ");
        int restrictedAreaX = InputHelper.inputPositiveInt();
        System.out.print("Y: ");
        int restrictedAreaY = InputHelper.inputPositiveInt();

        System.out.println("Input size of the restricted area: ");
        System.out.print("C: ");
        int restrictedAreaA = InputHelper.inputPositiveInt();
        System.out.print("D: ");
        int restrictedAreaB = InputHelper.inputPositiveInt();

        System.out.print("Input size of adjacency matrix: ");
        int adjMatrixSize = InputHelper.inputPositiveInt();

        System.out.println("Input adjacency matrix: ");
        Matrix adjMatrix = new Matrix(adjMatrixSize, adjMatrixSize);
        for (int i = 0; i < adjMatrixSize; i++) {
            for (int j = i + 1; j < adjMatrixSize; j++) {
                if (i != j) {
                    System.out.print("Value at " + "[" + i + "]" + "[" + j + "]: ");
                    int currValue = InputHelper.inputPositiveInt();
                    adjMatrix.set(i, j, currValue);
                    adjMatrix.set(j, i, currValue);
                } else {
                    adjMatrix.set(j, i, 0);
                }
            }
        }

        List<Element> elementList = new ArrayList<>();
        System.out.println("Input size of elements: ");
        for (int i = 0; i < adjMatrixSize; i++) {
            System.out.println("Element [" + i + "]:");
            System.out.print("A: ");
            int a = InputHelper.inputPositiveInt();
            System.out.print("B: ");
            int b = InputHelper.inputPositiveInt();
            elementList.add(new Element(i, 0, 0, a, b));
        }

        System.out.println("Input the minimum distance between the elements: ");
        int minDistance = InputHelper.inputPositiveInt();

        System.out.println(PrintHelper.getDelimiter());
        System.out.println("Input information: ");
        System.out.println("Circuit board size: A = " + circuitBoardA + ", B = " + circuitBoardB);
        System.out.println("Coordinates of the restricted area center: X = " + restrictedAreaX + ", Y = "
                + restrictedAreaY);
        System.out.println("Size of the restricted area: C = " + restrictedAreaA + ", D = " + restrictedAreaB);
        System.out.println("Adjacency matrix: " + MatrixHelper.toString(adjMatrix));
        System.out.println("Elements size: [a, b] = " + MatrixHelper.toString(elementList));
        System.out.println("Minimum distance between the elements: " + minDistance);
        System.out.println(PrintHelper.getDelimiter());

        CircuitBoard circuitBoard = new CircuitBoard(circuitBoardA, circuitBoardB);
        RestrictedArea restrictedArea = new RestrictedArea(restrictedAreaX, restrictedAreaY,
                restrictedAreaA, restrictedAreaB);

        BatAlgorithm batAlgorithm = new BatAlgorithm(circuitBoard, restrictedArea, adjMatrix,
                elementList, minDistance);

        batAlgorithm.calculate();

        Matrix bestX = batAlgorithm.getBestX();
        Matrix bestY = batAlgorithm.getBestY();
        for (int r = 0; r < bestX.getRowDimension(); r++) {
            for (int c = 0; c < bestX.getColumnDimension(); c++) {
                Element element = elementList.get(c);
                element.setX((int) bestX.get(r, c));
                element.setY((int) bestY.get(r, c));
            }
        }
        new BoardWindow(circuitBoard, restrictedArea, elementList);



//        int n_iter = batAlgorithm.getN_iter();
//        Matrix best = batAlgorithm.getBest();
//        Matrix fmin = batAlgorithm.getFmin();


//        Matrix resultStub = new Matrix(2, adjMatrixSize);
//        for (int i = 0; i < adjMatrixSize; i++) {
//            resultStub.set(0, i, (int) (Math.random() * circuitBoardB));
//            resultStub.set(1, i, (int) (Math.random() * circuitBoardA));
//        }
//
//        double L_G = Math.random() * (circuitBoardA + circuitBoardB) * 10;
//
//        System.out.println("Solution information: ");
//        System.out.println("Best found coordinates for elements: " + MatrixHelper.toString(resultStub));
//        System.out.println("Minimum found L(G) = " + (int) L_G);
    }
}