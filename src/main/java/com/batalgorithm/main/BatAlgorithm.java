package com.batalgorithm.main;

import Jama.Matrix;
import com.batalgorithm.utils.ElementDecreaseAreaComparator;
import com.batalgorithm.utils.ElementRandomComparator;
import com.batalgorithm.utils.MatlabSubstitute;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.batalgorithm.utils.MatrixHelper.*;

public class BatAlgorithm {

    private static final int NUMBER_OF_CYCLE_EXECUTION = 1000;

    private CircuitBoard circuitBoard;
    private RestrictedArea restrictedArea;
    private Matrix adjacencyMatrix;
    private List<Element> elementList;
    private int minDistance;

    private List<Element> best;
    private double minLength;
    private int iter; // Общее количество вычисления функции

    public BatAlgorithm(CircuitBoard circuitBoard, RestrictedArea restrictedArea, Matrix adjacencyMatrix, List<Element>
            elementList, int minDistance) {
        this.circuitBoard = circuitBoard;
        this.restrictedArea = restrictedArea;
        this.adjacencyMatrix = adjacencyMatrix;
        this.elementList = elementList;
        this.minDistance = minDistance;
    }

    /**
     * @param n     - Численность населения, как правило, от 10 до 40
     * @param N_gen - Количество поколений
     * @param A     - Громкость
     * @param r     - Частота
     * @param Qmin  - минимальная частота (Этот диапазон частот определяет масштаб)
     * @param Qmax  - максимальная частота
     */
    public void calculate(int n, int N_gen, double A, double r, int Qmin, int Qmax, int maxStep) {

        iter = 0; // Счетчик количества выполнения функции (поколений)
        int d = elementList.size(); // Количество измерений = два измерения(x и y) * (количество элементов)

        // Инициализация массивов частоты и скорости нулями
        Matrix Q = MatlabSubstitute.zeros(n, 1); // Частота
        Matrix v = MatlabSubstitute.zeros(n, d); // Скорость

        // Инициализация населения / решений
        Matrix solutionLength = new Matrix(1, n);

        List<List<Element>> solutions = new ArrayList<>();
        for (int row = 0; row < n; row++) {
            // Генерация новых координат
            List<Element> elementListWithNewCoordinates = generateNewCoordinates();
            Collections.sort(elementListWithNewCoordinates, new ElementDecreaseAreaComparator());
            solutions.add(elementListWithNewCoordinates);
        }

        // Находим суммарные длины связей для каждого из решений
        for (int row = 0; row < solutions.size(); row++) {
            solutionLength.set(0, row, calculateLength(solutions.get(row)));
        }

        // Находим начальное лучшее решение
        Matrix min = MatlabSubstitute.min(solutionLength);
        minLength = getRow(min, 0).get(0, 0);
        Matrix I = getRow(min, 1);
        int bestIndex = (int) I.get(0, 0) - 1;
        best = solutions.get(bestIndex);

        // Начинаем итерации алгоритма (важная часть)
        Matrix Sx = new Matrix(n, d);
        Matrix Sy = new Matrix(n, d);
        for (int t = 0; t < N_gen; t++) {
            // Цикл по всем летучим мышам / решениям
            for (int i = 0; i < n; i++) {
                // Генерируем вектор частоты в диапазоне от минимальной до максимальной
                setForAllInRow(Q, i, Qmin + Qmin - Qmax * MatlabSubstitute.rand());
                // Достаем текущее решение из списка всех решений
                List<Element> currSolution = solutions.get(i);
                // Генеррируем новые скорости по осям
                Matrix vXRow = getRow(v, i).plus((getXRow(currSolution).minus(getXRow(best))).
                        times(getRow(Q, i).get(0, 0)));
                Matrix vYRow = getRow(v, i).plus((getYRow(currSolution).minus(getYRow(best))).
                        times(getRow(Q, i).get(0, 0)));
                // Перемещаем центры элеентов в соответсвии со значениями скоростей
                setRow(Sx, getXRow(currSolution).plus(vXRow), i);
                setRow(Sy, getYRow(currSolution).plus(vYRow), i);
                // Применяем ограничения
                solutions.set(i, bounds(currSolution));
                // Частота пульсации
                if (MatlabSubstitute.rand() > r) {
                    // Коэффициент "maxStep" ограничивает размеры шагов случайных перемещений
                    setRow(Sx, getXRow(best).plus(MatlabSubstitute.randn(1, d).times(maxStep)), i);
                    setRow(Sy, getYRow(best).plus(MatlabSubstitute.randn(1, d).times(maxStep)), i);
                }
                // Оценим новые решения
                List<Element> elements = convertToElementList(Sx, Sy, i);
                double Fnew = calculateLength(elements);
                // Обновить, если решение лучше или не слишком громко
                if ((Fnew <= solutionLength.get(0, i)) && (MatlabSubstitute.rand() < A)) {
                    setNewSolution(solutions, getRow(Sx, i), getRow(Sy, i), i);
                }
                // Обновим текущее лучшее решение
                if (Fnew <= minLength) {
                    setNewBest(best, getRow(Sx, i), getRow(Sy, i));
                    minLength = Fnew;
                }
            }
            iter = iter + n;
        }
    }

    private List<Element> convertToElementList(Matrix sx, Matrix sy, int i) {
        int rowXDimension = sx.getRowDimension();
        int columnXDimension = sx.getColumnDimension();
        int rowYDimension = sy.getRowDimension();
        int columnYDimension = sy.getColumnDimension();
        if (rowXDimension != rowYDimension || columnXDimension != columnYDimension) {
            throw new IllegalArgumentException("Input matrix size doesn't match");
        }
        List<Element> elements = new ArrayList<>();
        for (int c = 0; c < columnXDimension; c++) {
            elements.add(new Element(0, (int) sx.get(i, c), (int) sy.get(i, c), 0, 0));
        }
        return elements;
    }

    private void setNewBest(List<Element> best, Matrix xRow, Matrix yRow) {
        for (int i = 0; i < best.size(); i++) {
            Element element = (Element) best.get(i).clone();
            element.setX((int) xRow.get(0, i));
            element.setY((int) yRow.get(0, i));
            best.set(i, element);
        }
    }

    private void setNewSolution(List<List<Element>> solutions, Matrix xRow, Matrix yRow, int solutionNumber) {
        List<Element> elements = solutions.get(solutionNumber);
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            element.setX((int) xRow.get(0, i));
            element.setY((int) yRow.get(0, i));
        }
    }

    private Matrix getYRow(List<Element> currSolution) {
        Matrix yRow = new Matrix(1, currSolution.size());
        for (int i = 0; i < currSolution.size(); i++) {
            yRow.set(0, i, currSolution.get(i).getX());
        }
        return yRow;
    }

    private Matrix getXRow(List<Element> currSolution) {
        Matrix xRow = new Matrix(1, currSolution.size());
        for (int i = 0; i < currSolution.size(); i++) {
            xRow.set(0, i, currSolution.get(i).getX());
        }
        return xRow;
    }

    private double calculateLength(List<Element> elementList) {
        double length = 0;
        for (int i = 0; i < elementList.size(); i++) {
            Element current = elementList.get(i);
            double currX = current.getX();
            double currY = current.getY();
            for (int j = 0; j < elementList.size(); j++) {
                Element next = elementList.get(j);
                double nextX = next.getX();
                double nextY = next.getY();
                double countOfLink = adjacencyMatrix.get(i, j);
                length += countOfLink * Math.sqrt(Math.pow(currX - nextX, 2) + Math.pow(currY - nextY, 2));
            }
        }
        return length;
    }

    private List<Element> generateNewCoordinates() {
        List<Element> copyElementsList = new ArrayList<>();
        for (Element e : elementList) {
            copyElementsList.add(new Element(e.getNumber(), 0, 0, (int) e.getWidth(), (int) e.getHeight()));
        }
        Collections.sort(copyElementsList, new ElementRandomComparator());
        List<Element> resultList = null;
        boolean placed;
        int count = 0;
        do {
            try {
                resultList = pack(copyElementsList);
                placed = true;
            } catch (Exception e) {
                System.out.println(e.getMessage() + " Trying again...");
                placed = false;
            }
            if (count++ > NUMBER_OF_CYCLE_EXECUTION) {
                throw new RuntimeException("Endless loop");
            }
        } while (!placed || count < NUMBER_OF_CYCLE_EXECUTION);
        return resultList;
    }

    // Применение ограничений
    private List<Element> bounds(List<Element> elementList) {
        List<Element> resultList = new ArrayList<>();
        for (int i = 0; i < elementList.size(); i++) {
            Element element = elementList.get(i);
            boolean hasBeenAdded = false;
            int count = 0;
            do {
                if (checkBaseRestrictions(element)) {
                    while (intersects(element, resultList)) {
                        moveToNearEmptySpace(element, resultList);
                    }
                    resultList.add((Element) element.clone());
                    hasBeenAdded = true;
                } else {
                    moveToBoard(element);
                }
                if (count++ > NUMBER_OF_CYCLE_EXECUTION) {
                    throw new RuntimeException("Endless loop");
                }
            } while (!hasBeenAdded);
        }
        return resultList;
    }

    private void moveToNearEmptySpace(Element element, List<Element> elementList) {
        int startX = (int) element.getX();
        int startY = (int) element.getY();
        int x;
        int y;
        int stepSize = 1;
        int count = 0;
        do {
            x = (int) (startX + MatlabSubstitute.getRandomSign() * Math.random() * stepSize);
            y = (int) (startY + MatlabSubstitute.getRandomSign() * Math.random() * stepSize);
            if (count % 10 == 0) {
                stepSize++;
            }
            element.setCenterX(x);
            element.setCenterY(y);
            if (!circuitBoard.contains(element)) {
                moveToBoard(element);
                continue;
            }
            if (count++ > NUMBER_OF_CYCLE_EXECUTION) {
                throw new RuntimeException("Endless loop");
            }
        } while (intersects(element, elementList));
    }

    private void moveToBoard(Element element) {
        int boardX = (int) circuitBoard.getCenterX();
        int boardY = (int) circuitBoard.getCenterY();
        int elementX = (int) element.getCenterX();
        int elementY = (int) element.getCenterY();
        if (boardX == elementX && boardY == elementY) {
            return;
        }
        int count = 0;
        do {
            if (elementX < boardX) {
                elementX++;
            } else if (elementX > boardX) {
                elementX--;
            }
            if (elementY < boardY) {
                elementY++;
            } else if (elementY > boardY) {
                elementY--;
            }
            element.setCenterX(elementX);
            element.setCenterY(elementY);
            if (count++ > NUMBER_OF_CYCLE_EXECUTION) {
                throw new RuntimeException("Endless loop");
            }
        } while (!circuitBoard.contains(element));
    }

    private boolean intersects(Element checkElement, List<Element> elementList) {
        for (Element e : elementList) {
            if (!e.equals(checkElement)) {
                if (e.intersects(checkElement)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Проверяем базовые ограничения: элемент размещен полностью на монтажной плате и элемент не пересекается с
    // запертной зоной
    private boolean checkBaseRestrictions(Element element) {
        return circuitBoard.contains(element) && !restrictedArea.intersects(element);
    }

    public double getMinLength() {
        return minLength;
    }

    public int getIter() {
        return iter;
    }

    public List<Element> getBest() {
        return best;
    }

    private List<Element> pack(List<Element> elementList) {
        List<Element> packElementList = new ArrayList<>();
        for (Element current : elementList) {
            boolean placed = false;
            boolean rotate = false;
            int x = 0; //(int) (Math.random() * circuitBoard.getWidth());
            int y = 0; //(int) (Math.random() * circuitBoard.getHeight());
            do {
                current.setX(x);
                current.setY(y);
                if (checkBaseRestrictions(current)) {
                    boolean intersectWithElement = false;
                    for (Element pack : packElementList) {
                        if (!current.equals(pack)) {
                            Element safeZone = new Element(pack.getNumber(),
                                    (int) pack.getX() - minDistance,
                                    (int) pack.getY() - minDistance,
                                    (int) pack.getWidth() + minDistance * 2,
                                    (int) pack.getHeight() + minDistance * 2);
                            if (current.intersects(safeZone)) {
                                intersectWithElement = true;
                            }
                        }
                    }
                    if (!intersectWithElement) {
                        packElementList.add(current);
                        placed = true;
                    }
                }
                if (!placed) {
                    if (x < circuitBoard.getWidth()) {
                        x++;
                    } else if (y < circuitBoard.getHeight()) {
                        x = 0;
                        y++;
                    } else {
                        if (rotate) {
                            throw new RuntimeException("Can not place element: " + current);
                        } else {
                            current = new Element(current.getNumber(), 0, 0, (int) current.getHeight(),
                                    (int) current.getWidth());
                            x = 0;
                            y = 0;
                            rotate = true;
                        }
                    }
                }
            } while (!placed);
        }
        return packElementList;
    }
}
