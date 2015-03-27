package com.batalgorithm.main;

import Jama.Matrix;
import com.batalgorithm.utils.InputHelper;
import com.batalgorithm.utils.MatrixHelper;
import com.batalgorithm.utils.PrintHelper;
import com.batalgorithm.view.BoardWindow;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

public class Main {

    public static void main(String[] args) {

        try {
            File logPropertiesFile = null;
            URL resource = Main.class.getClassLoader().getResource("logging.properties");
            if (resource != null) {
                logPropertiesFile = new File(resource.getFile());
            }
            if (logPropertiesFile != null && !logPropertiesFile.canRead()) {
                logPropertiesFile = new File("C:\\bat_algorithm\\logging.properties");
            }
            if (logPropertiesFile != null) {
                LogManager.getLogManager().readConfiguration(new FileInputStream(logPropertiesFile));
            }
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }

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
                    int currValue = InputHelper.inputPositiveIntOrZero();
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
        System.out.println("Base information: ");
        System.out.println("Circuit board size: A = " + circuitBoardA + ", B = " + circuitBoardB);
        System.out.println("Coordinates of the restricted area center: X = " + restrictedAreaX + ", Y = "
                + restrictedAreaY);
        System.out.println("Size of the restricted area: C = " + restrictedAreaA + ", D = " + restrictedAreaB);
        System.out.println("Adjacency matrix: " + MatrixHelper.toString(adjMatrix));
        System.out.println("Elements size: [a, b] = " + MatrixHelper.sizeToString(elementList));
        System.out.println("Minimum distance between the elements: " + minDistance);
        System.out.println(PrintHelper.getDelimiter());

        System.out.println(PrintHelper.getDelimiter());
        System.out.println("Input algorithm parameters: ");
        System.out.print("Input bat population size (~10-40): ");
        int n = InputHelper.inputPositiveInt();
        System.out.print("Input bat generations (~1000-5000): ");
        int N_gen = InputHelper.inputPositiveInt();
        System.out.print("Input volume (0 < volume < 1): ");
        double A = InputHelper.inputPositiveDoubleFromZeroToOne();
        System.out.print("Input rate (0 < rate < 1): ");
        double r = InputHelper.inputPositiveDoubleFromZeroToOne();
        System.out.print("Input minimum frequency: ");
        int Qmin = InputHelper.inputPositiveIntOrZero();
        System.out.print("Input maximum frequency: ");
        int Qmax = InputHelper.inputPositiveIntOrZero();
        System.out.print("Input maximum searching step: ");
        int maxStep = InputHelper.inputPositiveInt();
        System.out.println(PrintHelper.getDelimiter());

        System.out.println(PrintHelper.getDelimiter());
        System.out.println("Algorithm parameters: ");
        System.out.println("Bat population size (~10-40): " + n);
        System.out.println("Bat generations (~1000-5000): " + N_gen);
        System.out.println("Volume: " + A);
        System.out.println("Rate: " + r);
        System.out.println("Minimum frequency: " + Qmin);
        System.out.println("Maximum frequency: " + Qmax);
        System.out.println("Maximum searching step: " + maxStep);
        System.out.println(PrintHelper.getDelimiter());

        CircuitBoard circuitBoard = new CircuitBoard(circuitBoardA / 2, circuitBoardB / 2, circuitBoardA, circuitBoardB);
        RestrictedArea restrictedArea = new RestrictedArea(restrictedAreaX, restrictedAreaY,
                restrictedAreaA, restrictedAreaB);
        BatAlgorithm batAlgorithm = new BatAlgorithm(circuitBoard, restrictedArea, adjMatrix,
                elementList, minDistance);
        batAlgorithm.calculate(n, N_gen, A, r, Qmin, Qmax, maxStep);
        List<Element> bestElementPlaced = batAlgorithm.getBest();

        System.out.println(PrintHelper.getDelimiter());
        System.out.println("Solution information: ");
        System.out.println("The number of function evaluation: " + batAlgorithm.getIter());
        System.out.println("Best found coordinates for elements: [x,y] = " +
                MatrixHelper.coordinatesToString(bestElementPlaced));
        System.out.println("Minimum found L(G): " + batAlgorithm.getMinLength());
        System.out.println("Drawing the scheme in the separate window...");
        System.out.println(PrintHelper.getDelimiter());

        new BoardWindow(circuitBoard, restrictedArea, bestElementPlaced);
    }
}