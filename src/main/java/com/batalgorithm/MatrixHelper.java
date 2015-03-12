package com.batalgorithm;

import Jama.Matrix;

public class MatrixHelper {

    public static String toString(Matrix matrix) {
        StringBuilder result = new StringBuilder();
        result.append(System.lineSeparator());
        int rowDimension = matrix.getRowDimension();
        int columnDimension = matrix.getColumnDimension();
        for (int r = 0; r < rowDimension; r++) {
            for (int c = 0; c < columnDimension; c++) {
                result.append(matrix.get(r, c));
                result.append(" ");
            }
            result.append(System.lineSeparator());
        }
        return result.toString();
    }

    public static Matrix getRow(Matrix matrix, int rowNumber) {
        int rowDimension = matrix.getRowDimension();
        if (rowDimension > rowNumber) {
            int columnDimension = matrix.getColumnDimension();
            Matrix result = new Matrix(1, columnDimension);
            for (int c = 0; c < columnDimension; c++) {
                result.set(0, c, matrix.get(rowNumber, c));
            }
            return result;
        } else {
            throw new IllegalArgumentException("Cannot get row: " + rowNumber + ". Matrix row dimension = " +
                    rowDimension);
        }
    }

    public static Matrix getCol(Matrix matrix, int colNumber) {
        int columnDimension = matrix.getColumnDimension();
        if (columnDimension > colNumber) {
            int rowDimension = matrix.getRowDimension();
            Matrix result = new Matrix(rowDimension, 1);
            for (int r = 0; r < rowDimension; r++) {
                result.set(r, 0, matrix.get(r, colNumber));
            }
            return result;
        } else {
            throw new IllegalArgumentException("Cannot get col: " + colNumber + ". Matrix col dimension = " +
                    columnDimension);
        }
    }

    public static void setRow(Matrix matrix, Matrix row, int rowNumber) {
        int rowDimension = matrix.getRowDimension();
        if (rowDimension > rowNumber) {
            int columnDimension = matrix.getColumnDimension();
            for (int c = 0; c < columnDimension; c++) {
                matrix.set(rowNumber, c, row.get(0, c));
            }
        } else {
            throw new IllegalArgumentException("Cannot set row " + rowNumber +
                    ". Matrix row dimension = " + rowDimension);
        }
    }

    public static void setCol(Matrix matrix, Matrix col, int colNumber) {
        int columnDimension = matrix.getColumnDimension();
        if (columnDimension > colNumber) {
            int rowDimension = matrix.getRowDimension();
            for (int r = 0; r < rowDimension; r++) {
                matrix.set(r, colNumber, col.get(r, 0));
            }
        } else {
            throw new IllegalArgumentException("Cannot set col " + colNumber +
                    ". Matrix column dimension = " + columnDimension);
        }
    }

    public static boolean isEquals(Matrix m1, Matrix m2) {
        int m1RowDimension = m1.getRowDimension();
        int m2RowDimension = m2.getRowDimension();
        if (m1RowDimension == m2RowDimension) {
            int m1ColumnDimension = m1.getColumnDimension();
            int m2ColumnDimension = m2.getColumnDimension();
            if (m1ColumnDimension == m2ColumnDimension) {
                boolean equals = true;
                for (int r = 0; r < m1RowDimension; r++) {
                    for (int c = 0; c < m1ColumnDimension; c++) {
                        double m1Value = m1.get(r, c);
                        double m2Value = m2.get(r, c);
                        if (m1Value != m2Value) {
                            equals = false;
                            break;
                        }
                    }
                }
                return equals;
            }
        }
        return false;
    }

    public static void setForAllInRow(Matrix matrix, int rowNumber, double value) {
        int rowDimension = matrix.getRowDimension();
        if (rowNumber < rowDimension) {
            for (int c = 0; c < matrix.getColumnDimension(); c++) {
                matrix.set(rowNumber, c, value);
            }
        } else {
            throw new IllegalArgumentException();
        }
    }
}
