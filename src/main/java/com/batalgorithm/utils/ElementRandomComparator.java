package com.batalgorithm.utils;

import com.batalgorithm.main.Element;

import java.util.Comparator;

/**
 * Предназначен для сортировки элементов в случайном порядке.
 */
public class ElementRandomComparator implements Comparator<Element> {
    @Override
    public int compare(Element o1, Element o2) {
        double rnd = Math.random();
        if (rnd < 0.3) {
            return -1;
        } else if (rnd >= 0.3 && rnd < 0.6) {
            return 1;
        } else {
            return 0;
        }
    }
}
