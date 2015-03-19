package com.batalgorithm.main;

import java.util.Comparator;

/**
 * Предназнаяен для сортировки элементов по убыванию площади.
 */
public class ElementsDecreaseComparator implements Comparator<Element> {

    @Override
    public int compare(Element o1, Element o2) {
        if (o1.getArea() > o2.getArea()) {
            return -1;
        } else if (o1.getArea() < o2.getArea()) {
            return 1;
        }
        return 0;
    }
}
