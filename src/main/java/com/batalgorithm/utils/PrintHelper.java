package com.batalgorithm.utils;

/**
 * Помощник по выводу информации в консоль.
 */
public class PrintHelper {

    public static final int WIDTH = 80;
    public static final String DELIMITER_SYMBOL = "=";

    /**
     * Возвращает некоторый разделитель для строк.
     *
     * @return Строка - разделитель.
     */
    public static String getDelimiter() {
        StringBuilder delimiter = new StringBuilder();
        for (int i = 0; i < WIDTH; i++) {
            delimiter.append(DELIMITER_SYMBOL);
        }
        return delimiter.toString();
    }

    /**
     * Разбивает входную строку на строки заданной длины.
     *
     * @param input - входная строка
     * @return Строка, разбитая на подстроки.
     */
    public static String divide(String input) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (i % 80 == 0 && i != 0) {
                result.append(System.lineSeparator());
            }
            result.append(input.charAt(i));
        }
        return result.toString();
    }
}
