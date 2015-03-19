package com.batalgorithm;

import Jama.Matrix;
import com.batalgorithm.utils.MatrixHelper;
import junit.framework.TestCase;
import org.junit.Assert;

public class MatrixHelperTest extends TestCase {

    private int rowNumber = 2;
    private int colNumber = 3;
    private Matrix m1;
    private Matrix m2;

    public void setUp() throws Exception {


        m1 = new Matrix(rowNumber, colNumber);
        m2 = new Matrix(rowNumber, colNumber);

        for (int r = 0; r < rowNumber; r++) {
            for (int c = 0; c < colNumber; c++) {
                m1.set(r, c, Math.random());
                m2.set(r, c, Math.random());
            }
        }
    }

    public void testGetRow() throws Exception {
        int r = (int) (Math.random() * rowNumber);
        Matrix row = MatrixHelper.getRow(m1, r);
        for (int c = 0; c < colNumber; c++) {
            Assert.assertTrue(m1.get(r, c) == row.get(0, c));
        }
        row = MatrixHelper.getRow(m2, r);
        for (int c = 0; c < colNumber; c++) {
            Assert.assertFalse(m1.get(r, c) == row.get(0, c));
        }
    }

    public void testGetCol() throws Exception {
        int c = (int) (Math.random() * colNumber);
        Matrix col = MatrixHelper.getCol(m1, c);
        for (int r = 0; r < rowNumber; r++) {
            Assert.assertTrue(m1.get(r, c) == col.get(r, 0));
        }
        col = MatrixHelper.getCol(m2, c);
        for (int r = 0; r < rowNumber; r++) {
            Assert.assertFalse(m1.get(r, c) == col.get(r, 0));
        }
    }

    public void testSetRow() throws Exception {
        int r = (int) (Math.random() * rowNumber);
        Matrix row = MatrixHelper.getRow(m1, r);
        MatrixHelper.setRow(m2, row, r);
        for (int c = 0; c < colNumber; c++) {
            Assert.assertTrue(m2.get(r, c) == row.get(0, c));
        }
    }

    public void testSetCol() throws Exception {
        int c = (int) (Math.random() * colNumber);
        Matrix col = MatrixHelper.getCol(m1, c);
        MatrixHelper.setCol(m2, col, c);
        for (int r = 0; r < rowNumber; r++) {
            Assert.assertTrue(m2.get(r, c) == col.get(r, 0));
        }
    }

    public void testIsEquals() throws Exception {
        Assert.assertTrue(MatrixHelper.isEquals(m1, m1));
        Assert.assertTrue(MatrixHelper.isEquals(m2, m2));
        Assert.assertFalse(MatrixHelper.isEquals(m1, m2));
        Assert.assertFalse(MatrixHelper.isEquals(m2, m1));
    }
}