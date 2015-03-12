package com.batalgorithm;

import Jama.Matrix;

import static com.batalgorithm.MatrixHelper.*;

public class Main {

    private static final String N_PARAM = "-n";
    private static final String N_GEN_PARAM = "-gen";
    private static final String A_PARAM = "-a";
    private static final String R_PARAM = "-r";

    public static void main(String[] args) {

        int n = 20; // Численность населения, как правило, от 10 до 40
        int N_gen = 1000; // Количество поколений
        double A = 0.5; // Громкость (постоянная или уменьшающаяся)
        double r = 0.5; // Частота (постоянная или уменьшающаяся)

        for (String arg : args) {
            try {
                if (arg.contains(N_PARAM)) {
                    n = Integer.valueOf(arg.replace(N_PARAM, "").trim());
                } else if (arg.contains(N_GEN_PARAM)) {
                    N_gen = Integer.valueOf(arg.replace(N_GEN_PARAM, "").trim());
                } else if (arg.contains(A_PARAM)) {
                    A = Double.valueOf(arg.replace(A_PARAM, "").trim());
                } else if (arg.contains(R_PARAM)) {
                    r = Double.valueOf(arg.replace(R_PARAM, "").trim());
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid parameter: " + arg);
            }
        }
        System.out.println("Base parameters: n = " + n + " N_gen = " + N_gen + " A = " + A + " r = " + r);

        // Этот диапазон частот определяет масштаб. Вы должны изменить эти значения
        int Qmin = 0; // минимальная частота
        int Qmax = 2; // максимальная частота
        System.out.println("Scaling parameters: Qmin = " + Qmin + "Qmax = " + Qmax);

        // Итерационные параметры
        int N_iter = 0; // Общее количество вычисления функции

        // Размер поисковых переменных
        int d = 10; // Количество измерений
        // Нижний предел / оценки / вектор
        Matrix Lb = MatlabSubstitute.ones(1, d).times(-2);
        // Верхний предел / оценки / вектор
        Matrix Ub = MatlabSubstitute.ones(1, d).times(2);
        // Инициализация массивов
        Matrix Q = MatlabSubstitute.zeros(n, 1); // Частота
        Matrix v = MatlabSubstitute.zeros(n, d); // Скорость
        System.out.println("Searching parameters: d = " + d + "\nLb = " + MatrixHelper.toString(Lb) +
                "\nUb = " + MatrixHelper.toString(Ub) + "\nQ = " + MatrixHelper.toString(Q) +
                "\nv = " + MatrixHelper.toString(v));

        // Инициализация населения / решений
        Matrix Sol = new Matrix(n, d);
        Matrix Fitness = new Matrix(1, n);

        for (int row = 0; row < n; row++) {
            Matrix currSolRow = null;
            Matrix rand = MatlabSubstitute.rand(1, d);
            for (int c = 0; c < d; c++) {
                currSolRow = Lb.plus(rand.times(Ub.minus(Lb).get(0, c)));
            }
            setRow(Sol, currSolRow, row);
        }

        for (int c = 0; c < n; c++) {
            Matrix row = getRow(Sol, c);
            row = MatlabSubstitute.pow(row, 2);
            double sum = MatlabSubstitute.sum(row);
            Fitness.set(0, c, sum);
        }
        System.out.println("Initialize the population/solutions: " +
                "\nSol = " + MatrixHelper.toString(Sol) +
                "\nFitness = " + MatrixHelper.toString(Fitness));


        // Находим начальное лучшее решение
        Matrix min = MatlabSubstitute.min(Fitness);
        Matrix fmin = getRow(min, 0);
        Matrix I = getRow(min, 1);
        int bestIndex = (int) I.get(0, 0) - 1;
        Matrix best = getRow(Sol, bestIndex);
        System.out.println("Find the initial best solution: " + "\nfmin = " + MatrixHelper.toString(fmin) + "\nI = " +
                MatrixHelper.toString(I) + "\nbest = " + MatrixHelper.toString(best));

        /*
            Примечание: Поскольку это демо, здесь мы не реализовали
            Снижение громкости и увеличение интенсивности излучения
            Заинтересованные читатели могут сделать некоторые параметрические исследования
            и также реализовать различные изменения A и r и т.д.
        */

        // Начинаем итерации алгоритма (важная часть)
        Matrix S = new Matrix(n, d);
        for (int t = 0; t < N_gen; t++) {
            // Цикл по всем летучим мышам / решениям
            for (int i = 0; i < n; i++) {
                setForAllInRow(Q, i, Qmin + Qmin - Qmax * MatlabSubstitute.rand());

                Matrix vRow = getRow(v, i).plus((getRow(Sol, i).minus(best)).times(getRow(Q, i).get(0, 0)));
                setRow(v, vRow, i);

                setRow(S, getRow(Sol, i).plus(getRow(v, i)), i);

                // Применяем простые оценки / ограничения
                setRow(Sol, simpleBounds(getRow(Sol, i), Lb, Ub), i);

                // Частота пульса, импульса.. (Pulse rate)
                if (MatlabSubstitute.rand() > r) {
                    // Коэффициент 0,001 ограничивает размеры шагов случайных перемещений
                    setRow(S, best.plus(MatlabSubstitute.randn(1, d).times(0.001)), i);
                }

                // Оценим новые решения
                Matrix Fnew = MatlabSubstitute.Fun(getRow(Sol, i));

                // Обновить, если решение лучше или не слишком громко
                if ((Fnew.get(0, 0) <= Fitness.get(0, i)) && (MatlabSubstitute.rand() < A)) {
                    setRow(Sol, getRow(S, i), i);
                }

                // Обновим текущее лучшее решение
                if (Fnew.get(0, 0) <= fmin.get(0, 0)) {
                    setRow(best, getRow(S, i), 0);
                    fmin = Fnew;
                }
            }
            N_iter = N_iter + n;
        }

        // Вывод результата
        System.out.println("Number of evaluations: " + N_iter);
        System.out.println("Best = " + MatrixHelper.toString(best));
        System.out.println("fmin = " + fmin.get(0, 0));
    }

    // Применение простых ограничений
    private static Matrix simpleBounds(Matrix s, Matrix Lb, Matrix Ub) {
        // Применим нижнюю границу
        Matrix ns_tmp = s.copy();
        Matrix I = MatlabSubstitute.isLowerThan(ns_tmp, Lb);
        for (int r = 0; r < I.getRowDimension(); r++) {
            for (int c = 0; c < I.getColumnDimension(); c++) {
                double value = I.get(r, c);
                if (value == 1) {
                    ns_tmp.set(r, c, Lb.get(r, c));
                }
            }
        }

        // Применим верхнюю границу
        Matrix J = MatlabSubstitute.isGreaterThan(ns_tmp, Ub);
        for (int r = 0; r < J.getRowDimension(); r++) {
            for (int c = 0; c < J.getColumnDimension(); c++) {
                double value = J.get(r, c);
                if (value == 1) {
                    ns_tmp.set(r, c, Ub.get(r, c));
                }
            }
        }

        // Обновим это новое передвижение
        return ns_tmp;
    }
}
