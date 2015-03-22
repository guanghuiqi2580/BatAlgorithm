package com.batalgorithm.utils;

import com.batalgorithm.main.Element;

import java.util.Comparator;

public class ElementDecreaseAreaComparator implements Comparator<Element> {
    @Override
    public int compare(Element o1, Element o2) {
        if (o1.getArea() > o2.getArea()) {
            return 1;
        } else if (o1.getArea() < o2.getArea()) {
            return -1;
        }
        return 0;
    }
}
