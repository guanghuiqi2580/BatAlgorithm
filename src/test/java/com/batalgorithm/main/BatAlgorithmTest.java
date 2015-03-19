package com.batalgorithm.main;

import Jama.Matrix;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BatAlgorithmTest {

    private BatAlgorithm batAlgorithm;

    private CircuitBoard circuitBoard;
    private RestrictedArea restrictedArea;
    private List<Element> elementList;
    private int minDistance = 5;

    @Before
    public void setUp() throws Exception {
        circuitBoard = new CircuitBoard(200, 100);
        restrictedArea = new RestrictedArea(30, 30, 55, 20);
        int adjMatrixSize = 3;
        Matrix adjMatrix = new Matrix(adjMatrixSize, adjMatrixSize);
        for (int i = 0; i < adjMatrixSize; i++) {
            for (int j = i + 1; j < adjMatrixSize; j++) {
                if (i != j) {
                    int currValue = (int) (Math.random() * 5);
                    adjMatrix.set(i, j, currValue);
                    adjMatrix.set(j, i, currValue);
                } else {
                    adjMatrix.set(j, i, 0);
                }
            }
        }

        elementList = new ArrayList<>();
        elementList.add(new Element(0, 0, 0, 54, 62));
        elementList.add(new Element(1, 0, 0, 45, 32));
        elementList.add(new Element(2, 0, 0, 5, 10));
//        elementList.add(new Element(3, 0, 0, 85, 92));
//        elementList.add(new Element(4, 0, 0, 75, 11));
//        elementList.add(new Element(5, 0, 0, 59, 14));

        batAlgorithm = new BatAlgorithm(circuitBoard, restrictedArea, adjMatrix, elementList, minDistance);

        Matrix bestX = batAlgorithm.getBestX();
        Matrix bestY = batAlgorithm.getBestY();
        System.out.println("fmin = " + batAlgorithm.getFmin());
        List<Element> bestPackedElementList = new ArrayList<>();
        for (int r = 0; r < bestX.getRowDimension(); r++) {
            for (int c = 0; c < bestX.getColumnDimension(); c++) {
                Element element = elementList.get(c);
                bestPackedElementList.add(new Element(element.getNumber(), (int) bestX.get(r, c),
                        (int) bestY.get(r, c), (int) element.getWidth(), (int) element.getHeight()));
            }
        }
        new BoardWindow(circuitBoard, restrictedArea, bestPackedElementList);
    }

    @Test
    public void testPack() throws Exception {
        Collections.sort(elementList, new ElementRandomComparator());
        List<Element> packElementList = batAlgorithm.pack();
        System.out.println(circuitBoard);
        System.out.println(restrictedArea);
        System.out.println("Packed elements: ");
        for (Element e : packElementList) {
            System.out.println(e);
        }
//        new BoardWindow(circuitBoard, restrictedArea, packElementList);
        Thread.sleep(60 * 60 * 1000);
    }
}