package com.batalgorithm;

import Jama.Matrix;
import com.batalgorithm.utils.MatlabSubstitute;
import junit.framework.TestCase;
import org.junit.Assert;

public class MatlabSubstituteTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    /*
     * Пример:
     * M =
     * 8 1 6
     * 3 5 7
     * 4 9 2
     *
     * Результат:
     * [y, I] = min(M)
     * y = 3   1   2
     * I = 2   1   3
     */
    public void testMin() throws Exception {

        Matrix matrix = new Matrix(3, 3);
        matrix.set(0, 0, 8);
        matrix.set(0, 1, 1);
        matrix.set(0, 2, 6);

        matrix.set(1, 0, 3);
        matrix.set(1, 1, 5);
        matrix.set(1, 2, 7);

        matrix.set(2, 0, 4);
        matrix.set(2, 1, 9);
        matrix.set(2, 2, 2);

        Matrix min = MatlabSubstitute.min(matrix);
        Assert.assertTrue(min.get(0, 0) == 3);
        Assert.assertTrue(min.get(0, 1) == 1);
        Assert.assertTrue(min.get(0, 2) == 2);

        Assert.assertTrue(min.get(1, 0) == 2);
        Assert.assertTrue(min.get(1, 1) == 1);
        Assert.assertTrue(min.get(1, 2) == 3);
    }
}