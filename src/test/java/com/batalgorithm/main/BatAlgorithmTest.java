package com.batalgorithm.main;

import Jama.Matrix;
import com.batalgorithm.view.BoardWindow;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.LogManager;

public class BatAlgorithmTest {

    @Test
    @Ignore
    public void test() throws Exception {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            LogManager.getLogManager().readConfiguration(
                    new FileInputStream(classLoader.getResource("logging.properties").getFile()));
            CircuitBoard circuitBoard = new CircuitBoard(200, 100);
            RestrictedArea restrictedArea = new RestrictedArea(30, 40, 10, 50);
            Element element0 = new Element(0, 100, 50, 20, 30);
            Element element1 = new Element(1, 100, 50, 10, 10);
            Element element2 = new Element(2, 100, 50, 5, 5);
            Element element3 = new Element(3, 100, 50, 10, 15);
            Element element4 = new Element(4, 100, 50, 20, 30);
            Element element5 = new Element(5, 100, 50, 20, 30);
            Element element6 = new Element(6, 100, 50, 20, 30);
            Element element7 = new Element(7, 100, 50, 20, 30);
            List<Element> elementList = new ArrayList<>();
            elementList.add(element0);
            elementList.add(element1);
            elementList.add(element2);
            elementList.add(element3);
            elementList.add(element4);
            elementList.add(element5);
            elementList.add(element6);
            elementList.add(element7);
            int size = elementList.size();
            Matrix adjacencyMatrix = new Matrix(size, size);
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    adjacencyMatrix.set(i, j, 0); //(int) (Math.random() * 10));
                }
            }
            adjacencyMatrix.set(0, 1, 1);
            adjacencyMatrix.set(1, 0, 1);
            adjacencyMatrix.set(0, 3, 1);
            adjacencyMatrix.set(3, 0, 1);
            int distance = 5;
            BatAlgorithm batAlgorithm = new BatAlgorithm(circuitBoard, restrictedArea, adjacencyMatrix,
                    elementList, distance);
            batAlgorithm.calculate(10, 5000, 0.5, 0.5, 0, 2, 5);
            new BoardWindow(circuitBoard, restrictedArea, batAlgorithm.getBest());
            Thread.sleep(60 * 1000);
        } catch (IOException e) {
            System.err.println("Could not setup logger configuration: " + e.toString());
        }
    }
}