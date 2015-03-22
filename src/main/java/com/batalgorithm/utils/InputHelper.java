package com.batalgorithm.utils;

import java.util.Scanner;

/**
 * Помощник по вводу данных от пользователя в консоли.
 */
public class InputHelper {

    private static final String INPUT_POSITIVE_INT_ERROR = "Input error! Expected number from 0 to " + Integer
            .MAX_VALUE + ". Try again.";

    private static final String INPUT_POSITIVE_DOUBLE_ERROR = "Input error! Expected number from 0 to " + Double
            .MAX_VALUE + ". Try again.";

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Читает следующее положительное целое число.
     *
     * @return Положительное целое число, введенное пользователем.
     */
    public static int inputPositiveInt() {
        do {
            if (scanner.hasNextInt()) {
                int currInt = scanner.nextInt();
                if (0 < currInt) {
                    return currInt;
                } else {
                    System.out.println(INPUT_POSITIVE_INT_ERROR);
                    scanner.next();
                }
            } else {
                System.out.println(INPUT_POSITIVE_INT_ERROR);
                scanner.next();
            }
        } while (true);
    }

    /**
     * Читает следующее положительное целое число или ноль.
     *
     * @return Положительное целое число или ноль, введенное пользователем.
     */
    public static int inputPositiveIntOrZero() {
        do {
            if (scanner.hasNextInt()) {
                int currInt = scanner.nextInt();
                if (0 <= currInt) {
                    return currInt;
                } else {
                    System.out.println(INPUT_POSITIVE_INT_ERROR);
                    scanner.next();
                }
            } else {
                System.out.println(INPUT_POSITIVE_INT_ERROR);
                scanner.next();
            }
        } while (true);
    }

    /**
     * Читает следующее положительное вещественное число.
     *
     * @return Положительное вещественное число, введенное пользователем.
     */
    public static double inputPositiveDouble() {
        do {
            if (scanner.hasNextDouble()) {
                double currInt = scanner.nextDouble();
                if (0 < currInt) {
                    return currInt;
                } else {
                    System.out.println(INPUT_POSITIVE_DOUBLE_ERROR);
                    scanner.next();
                }
            } else {
                System.out.println(INPUT_POSITIVE_DOUBLE_ERROR);
                scanner.next();
            }
        } while (true);
    }
}
