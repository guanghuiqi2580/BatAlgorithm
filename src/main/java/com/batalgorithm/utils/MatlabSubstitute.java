package com.batalgorithm.utils;

import Jama.Matrix;

import java.util.Date;
import java.util.Random;

/**
 * Преднанаяен для работы с математическими операциями. Большая часть методов являются аналогами подобных операций в
 * среде Matlab.
 */
public class MatlabSubstitute {

    /**
     * Формирует массив размера m х n, элементами которого являются случайные величины,
     * распределенные по нормальному закону с математическим ожиданием 0 и среднеквадратическим отклонением 1.
     * Аналог randn(m, n) в Matlab.
     *
     * @param m - количество строк
     * @param n - количество столбцов
     * @return Результирующая матрица случайных величин размера m х n.
     */
    public static Matrix randn(int m, int n) {
        Random random = new Random(new Date().getTime());
        Matrix matrix = new Matrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix.set(i, j, random.nextGaussian());
            }
        }
        return matrix;
    }

    /**
     * Функция [Y, I] = min(X) кроме самих минимальных элементов возвращает вектор-строку индексов этих элементов в
     * данном столбце.
     *
     * @param matrix - входная матрица
     * @return Матрица, в первой строке который минимальные элементы в каждом из столбцов, а во второй - индексы этих
     * элементов.
     */
    public static Matrix min(Matrix matrix) {
        int rowDimension = matrix.getRowDimension();
        int columnDimension = matrix.getColumnDimension();
        Matrix minValue;
        Matrix minValueIndex;
        Matrix result;
        if (rowDimension == 1) {
            result = new Matrix(2, 1);
            minValue = new Matrix(1, 1);
            minValueIndex = new Matrix(1, 1);
            double min = Double.MAX_VALUE;
            int minIndex = 0;
            for (int c = 0; c < columnDimension; c++) {
                double currValue = matrix.get(0, c);
                if (currValue < min) {
                    min = currValue;
                    minIndex = c;
                }
            }
            minValue.set(0, 0, min);
            minValueIndex.set(0, 0, minIndex + 1);
        } else {
            result = new Matrix(2, columnDimension);
            minValue = new Matrix(1, columnDimension);
            minValueIndex = new Matrix(1, columnDimension);
            for (int c = 0; c < columnDimension; c++) {
                double min = Double.MAX_VALUE;
                int minIndex = 0;
                for (int r = 0; r < rowDimension; r++) {
                    double currValue = matrix.get(r, c);
                    if (currValue < min) {
                        min = currValue;
                        minIndex = r;
                    }
                }
                minValue.set(0, c, min);
                minValueIndex.set(0, c, minIndex + 1);
            }
        }
        MatrixHelper.setRow(result, minValue, 0);
        MatrixHelper.setRow(result, minValueIndex, 1);
        return result;
    }

    /**
     * Формирует матрицу размера m х n, элементами которого являются случайные величины, распределенные по равномерному
     * закону в интервале (0,1). Аналог rand(m, n) в Matlab.
     *
     * @param m - количество строк
     * @param n - количество столбцов
     * @return Результирующая матрица случайных величин размера m х n.
     */
    public static Matrix rand(int m, int n) {
        Random random = new Random();
        Matrix matrix = new Matrix(m, n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                matrix.set(i, j, random.nextDouble());
            }
        }
        return matrix;
    }

    /**
     * Формирует матрицу, содержащую нули, размером m х n. Аналог zeros(m, n) в Matlab.
     *
     * @param n - количество строк
     * @param m - количество столбцов
     * @return Результирующая матрица нулей величин размера m х n.
     */
    public static Matrix zeros(int m, int n) {
        return fill(m, n, 0);
    }

    /**
     * Формирует матрицу единиц размера m х n. Аналог ones(m, n) в Matlab.
     *
     * @param n - количество строк
     * @param m - количество столбцов
     * @return Результирующая матрица единиц размера m х n.
     */
    public static Matrix ones(int m, int n) {
        return fill(m, n, 1);
    }

    /**
     * Заполняет матрицу заданным значением.
     *
     * @param n     - количество строк
     * @param m     - количество столбцов
     * @param value значение, которое будет присвоенно каждому элементу матрицы
     * @return Результирующая матрица, содержащее только заданное значение, размера m х n.
     */
    public static Matrix fill(int n, int m, double value) {
        Matrix matrix = new Matrix(n, m);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                matrix.set(i, j, value);
            }
        }
        return matrix;
    }

    /**
     * Возводит все элементы матрицы в заданную степень.
     *
     * @param matrix - входная матрица
     * @param pow    - степень
     * @return Результирующая матрица.
     */
    public static Matrix pow(Matrix matrix, double pow) {
        int rowDimension = matrix.getRowDimension();
        int columnDimension = matrix.getColumnDimension();
        Matrix result = new Matrix(rowDimension, columnDimension);
        for (int r = 0; r < rowDimension; r++) {
            for (int c = 0; c < columnDimension; c++) {
                result.set(r, c, Math.pow(matrix.get(r, c), pow));
            }
        }
        return result;
    }

    /**
     * Суммирует все элементы заданной матрицы.
     *
     * @param matrix - входная матрица
     * @return Сумма элементов матрицы.
     */
    public static double sum(Matrix matrix) {
        double sum = 0;
        for (int r = 0; r < matrix.getRowDimension(); r++) {
            for (int c = 0; c < matrix.getColumnDimension(); c++) {
                sum += matrix.get(r, c);
            }
        }
        return sum;
    }

    /**
     * формирует одно случайное число, подчиняющееся равномерному закону распределения в интервале (0, 1), которое
     * изменяется при каждом последующем вызове.
     *
     * @return Случайное число.
     */
    public static double rand() {
        Random random = new Random();
        return random.nextDouble();
    }

    /**
     * Сравнивает элементы входной матрицы и матрицы границ поочереди и если условие (элемент матрицы для сравнения
     * меньше, чем соответсвующий элемент из матрицы границ) выпоняется возвращает 1 в
     * соответсвующей позиции результирующей матрице, иначе - 0.
     *
     * @param matrix - входная матрица (к сравнению)
     * @param bound  - матрица границ
     * @return Результирующая матрица сравнений.
     */
    public static Matrix isLowerThan(Matrix matrix, Matrix bound) {
        int mColumnDimension = matrix.getColumnDimension();
        int mRowDimension = matrix.getRowDimension();
        int bColumnDimension = bound.getColumnDimension();
        int bRowDimension = bound.getRowDimension();
        if ((mColumnDimension == bColumnDimension) && (mRowDimension == bRowDimension)) {
            Matrix result = new Matrix(mRowDimension, mColumnDimension);
            for (int r = 0; r < mRowDimension; r++) {
                for (int c = 0; c < mColumnDimension; c++) {
                    double matrixValue = matrix.get(r, c);
                    double boundValue = bound.get(r, c);
                    if (matrixValue < boundValue) {
                        result.set(r, c, 1);
                    } else {
                        result.set(r, c, 0);
                    }
                }
            }
            return result;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Сравнивает элементы входной матрицы и матрицы границ поочереди и если условие (элемент матрицы для сравнения
     * больше, чем соответсвующий элемент из матрицы границ) выпоняется, то возвращает 1 в
     * соответсвующей позиции результирующей матрице, иначе - 0.
     *
     * @param matrix - входная матрица (к сравнению)
     * @param bound  - матрица границ
     * @return Результирующая матрица сравнений.
     */
    public static Matrix isGreaterThan(Matrix matrix, Matrix bound) {
        int mColumnDimension = matrix.getColumnDimension();
        int mRowDimension = matrix.getRowDimension();
        int bColumnDimension = bound.getColumnDimension();
        int bRowDimension = bound.getRowDimension();
        if ((mColumnDimension == bColumnDimension) && (mRowDimension == bRowDimension)) {
            Matrix result = new Matrix(mRowDimension, mColumnDimension);
            for (int r = 0; r < mRowDimension; r++) {
                for (int c = 0; c < mColumnDimension; c++) {
                    double matrixValue = matrix.get(r, c);
                    double boundValue = bound.get(r, c);
                    if (matrixValue > boundValue) {
                        result.set(r, c, 1);
                    } else {
                        result.set(r, c, 0);
                    }
                }
            }
            return result;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Предназначен для генерации случайного знака для выражения.
     *
     * @return Возращает -1 или 1 с равной вероятностью.
     */
    public static int getRandomSign() {
        return (Math.random() > 0.5) ? -1 : 1;
    }
}
