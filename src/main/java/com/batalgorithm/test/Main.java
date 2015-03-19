package com.batalgorithm.test;

import com.batalgorithm.main.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        CircuitBoard circuitBoard = new CircuitBoard(180, 70);
        RestrictedArea restrictedArea = new RestrictedArea(100, 5, 20, 50);
        List<Element> elementList = new ArrayList<>();
        elementList.add(new Element(0, 0, 0, 40, 15));
        elementList.add(new Element(1, 0, 0, 40, 35));
        elementList.add(new Element(2, 0, 0, 34, 59));
        elementList.add(new Element(3, 0, 0, 40, 35));
        elementList.add(new Element(4, 0, 0, 14, 15));
        elementList.add(new Element(5, 0, 0, 14, 19));
        elementList.add(new Element(6, 0, 0, 14, 29));
        elementList.add(new Element(7, 0, 0, 12, 11));
        elementList.add(new Element(8, 0, 0, 4, 5));
        elementList.add(new Element(10, 0, 0, 12, 10));
        elementList.add(new Element(11, 0, 0, 12, 10));
        elementList.add(new Element(12, 0, 0, 12, 10));
        elementList.add(new Element(13, 0, 0, 12, 10));
        elementList.add(new Element(14, 0, 0, 12, 10));
        elementList.add(new Element(15, 0, 0, 12, 10));
        elementList.add(new Element(16, 0, 0, 12, 10));
        int minDistance = 2;

        List<Element> packElementList;
        try {
            Collections.sort(elementList, new ElementRandomComparator());
            packElementList = pack(circuitBoard, restrictedArea, elementList, minDistance);
            new BoardWindow(circuitBoard, restrictedArea, packElementList);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    private static List<Element> pack(CircuitBoard circuitBoard, RestrictedArea restrictedArea,
                                      List<Element> elementList, int minDistance) {
        double sumArea = 0;
        for (Element e : elementList) {
            sumArea += e.getArea();
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
}