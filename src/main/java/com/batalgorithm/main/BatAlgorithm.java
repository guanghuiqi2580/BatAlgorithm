package com.batalgorithm.main;

import Jama.Matrix;
import com.batalgorithm.utils.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static com.batalgorithm.utils.MatrixHelper.*;

public class BatAlgorithm {

    private static final Logger LOG = Logger.getLogger(BatAlgorithm.class.getName());
    private static final int NUMBER_OF_CYCLE_EXECUTION = 1000;

    private CircuitBoard circuitBoard;
    private RestrictedArea restrictedArea;
    private Matrix adjacencyMatrix;
    private List<Element> elementList;
    private int minDistance;

    private List<Element> initial;
    private List<Element> best;
    private double initialMinLength;
    private double minLength;
    private int iter;
    private long executionTime;

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
        initialMinLength = minLength;
        Matrix I = getRow(min, 1);
        int bestIndex = (int) I.get(0, 0) - 1;
        best = solutions.get(bestIndex);
        initial = new ArrayList<>(best.size());
        for (Element e : best) {
            Element element = new Element(e.getNumber(), e.getCenterX(), e.getCenterY(), e.getWidth(), e.getHeight());
            initial.add(element);
        }

        StringBuilder initialReport = new StringBuilder();
        initialReport.append("Initial solutions (Element number, X, Y): ").append(System.lineSeparator());
        for (int i = 0; i < solutions.size(); i++) {
            List<Element> solution = solutions.get(i);
            initialReport.append("Solution ").append(i).append(": ").
                    append(MatrixHelper.coordinatesToString(solution));
        }
        initialReport.append("Initial best solution number: ").append(bestIndex).append(System.lineSeparator());
        initialReport.append("Initial best solution: ").append(MatrixHelper.coordinatesToString(getBest()));
        initialReport.append("Initial best solution length: ").append(minLength).append(System.lineSeparator());
        initialReport.append(PrintHelper.getDelimiter());
        LOG.info(initialReport.toString());

        // Начинаем итерации алгоритма (важная часть)
        Matrix Sx = new Matrix(n, d);
        Matrix Sy = new Matrix(n, d);
        long startTime = System.currentTimeMillis();
        for (int t = 0; t < N_gen; t++) {
            // Цикл по всем летучим мышам / решениям
            for (int i = 0; i < n; i++) {
                // Генерируем вектор частоты в диапазоне от минимальной до максимальной
                setForAllInRow(Q, i, Qmin + Qmin - Qmax * MatlabSubstitute.rand());
                // Достаем текущее решение из списка всех решений
                List<Element> currSolution = solutions.get(i);
                // Генеррируем новые скорости по осям
                Matrix minusX = getXRow(currSolution).minus(getXRow(best));
                Matrix minusY = getYRow(currSolution).minus(getYRow(best));
                Matrix vXRow = getRow(v, i).plus(minusX.times(getRow(Q, i).get(0, 0)));
                Matrix vYRow = getRow(v, i).plus(minusY.times(getRow(Q, i).get(0, 0)));
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
                boolean foundBest = false;
                if (Fnew < minLength) {
                    setNewBest(getRow(Sx, i), getRow(Sy, i));
                    minLength = Fnew;
                    foundBest = true;
                }
                StringBuilder report = new StringBuilder();
                report.append("Generation number: ").append(t).append(System.lineSeparator());
                report.append("Solution (element) number: ").append(i).append(System.lineSeparator());
                report.append("Velocity X: ").append(MatrixHelper.toIntString(vXRow)).append(System.lineSeparator());
                report.append("Velocity Y: ").append(MatrixHelper.toIntString(vYRow)).append(System.lineSeparator());
                report.append("Element X: ").append(MatrixHelper.toIntString(Sx)).append(System.lineSeparator());
                report.append("Element Y: ").append(MatrixHelper.toIntString(Sy)).append(System.lineSeparator());
                report.append("Best solution has been found: ").append(foundBest).append(System.lineSeparator());
                if (foundBest) {
                    report.append("New best solution: ").append(MatrixHelper.coordinatesToString(getBest())).
                            append(System.lineSeparator());
                    report.append("New min length: ").append(minLength).append(System.lineSeparator());
                }
                report.append(PrintHelper.getDelimiter());
                LOG.info(report.toString());
            }
            iter = iter + n;
        }
        long stopTime = System.currentTimeMillis();
        executionTime = stopTime - startTime;
        SimpleDateFormat formating = new SimpleDateFormat("mm:ss:SSS");
        LOG.info("Algorithm execution time: " + formating.format(executionTime));
    }

    private List<Element> convertToElementList(Matrix sx, Matrix sy, int i) {
        int columnXDimension = sx.getColumnDimension();
        List<Element> elements = new ArrayList<>();
        for (int c = 0; c < columnXDimension; c++) {
            elements.add(new Element(0, (int) sx.get(i, c), (int) sy.get(i, c), 0, 0));
        }
        return elements;
    }

    private void setNewBest(Matrix xRow, Matrix yRow) {
        for (int i = 0; i < best.size(); i++) {
            Element element = best.get(i).copy();
            element.setCenterX((int) xRow.get(0, i));
            element.setCenterY((int) yRow.get(0, i));
            best.set(i, element);
        }
        bounds(best);
    }

    private void setNewSolution(List<List<Element>> solutions, Matrix xRow, Matrix yRow, int solutionNumber) {
        List<Element> elements = solutions.get(solutionNumber);
        for (int i = 0; i < elements.size(); i++) {
            Element element = elements.get(i);
            element.setCenterX((int) xRow.get(0, i));
            element.setCenterY((int) yRow.get(0, i));
        }
    }

    private Matrix getYRow(List<Element> elements) {
        Matrix yRow = new Matrix(1, elements.size());
        for (int i = 0; i < elements.size(); i++) {
            yRow.set(0, i, elements.get(i).getCenterY());
        }
        return yRow;
    }

    private Matrix getXRow(List<Element> elements) {
        Matrix xRow = new Matrix(1, elements.size());
        for (int i = 0; i < elements.size(); i++) {
            xRow.set(0, i, elements.get(i).getCenterX());
        }
        return xRow;
    }

    private double calculateLength(List<Element> elementList) {
        double length = 0;
        for (int i = 0; i < elementList.size(); i++) {
            Element current = elementList.get(i);
            double currX = current.getCenterX();
            double currY = current.getCenterY();
            for (int j = 0; j < elementList.size(); j++) {
                Element next = elementList.get(j);
                double nextX = next.getCenterX();
                double nextY = next.getCenterY();
                double countOfLink = adjacencyMatrix.get(i, j);
                length += countOfLink * Math.sqrt(Math.pow(currX - nextX, 2) + Math.pow(currY - nextY, 2));
            }
        }
        return length;
    }

    private List<Element> generateNewCoordinates() {
        List<Element> copyElementsList = new ArrayList<>(elementList.size());
        for (Element e : elementList) {
            copyElementsList.add(e.copy());
        }
        Collections.sort(copyElementsList, new ElementRandomComparator());
        List<Element> resultList = null;
        boolean placed;
        int count = 0;
        do {
            try {
                resultList = randomPack(copyElementsList);
                placed = true;
            } catch (Exception e) {
                placed = false;
            }
            if (count++ > NUMBER_OF_CYCLE_EXECUTION) {
                break;
            }
        } while (!placed || count < NUMBER_OF_CYCLE_EXECUTION);
        return resultList;
    }

    /**
     * Применение ограничений для заданного списка элементов.
     *
     * @param elementList - список элементов
     * @return Список элементов, к которым применены ограничения.
     */
    private List<Element> bounds(List<Element> elementList) {
        List<Element> resultList = new ArrayList<>();
        for (Element current : elementList) {
            boolean placed = false;
            boolean rotate = false;
            boolean startAgain = false;
            int x = current.getCenterX();
            int y = current.getCenterY();
            do {
                if (checkBaseRestrictions(current)) {
                    boolean intersectWithElement = false;
                    for (Element pack : resultList) {
                        if (!current.equals(pack)) {
                            if (current.intersects(pack, minDistance)) {
                                intersectWithElement = true;
                            }
                        }
                    }
                    if (!intersectWithElement) {
                        resultList.add(current);
                        placed = true;
                    }
                }
                if (!placed) {
                    if (x < circuitBoard.getWidth()) {
                        x++;
                    } else if (y < circuitBoard.getHeight()) {
                        x = 0;
                        y++;
                    } else if (!startAgain && x == circuitBoard.getWidth() && y == circuitBoard.getHeight()) {
                        x = 0;
                        y = 0;
                        startAgain = true;
                    } else {
                        if (!rotate) {
                            current = new Element(current.getNumber(), 0, 0, current.getHeight(), current.getWidth());
                            x = 0;
                            y = 0;
                            rotate = true;
                        } else {
                            break;
                        }
                    }
                    current.setCenterX(x);
                    current.setCenterY(y);
                }
            } while (!placed);
        }
        return resultList;
    }

    /**
     * Проверяем базовые ограничения: элемент размещен полностью на монтажной плате и элемент не пересекается с
     * запертной зоной.
     *
     * @param element - список элементов
     * @return True - ограничения выполняются, иначе - False.
     */
    private boolean checkBaseRestrictions(Element element) {
        return circuitBoard.contains(element) && !restrictedArea.intersects(element, 0);
    }

    public double getMinLength() {
        return minLength;
    }

    public double getInitialMinLength() {
        return initialMinLength;
    }

    public int getIter() {
        return iter;
    }

    public List<Element> getBest() {
        return best;
    }

    public List<Element> getInitial() {
        return initial;
    }

    private List<Element> randomPack(List<Element> elementList) {
        List<Element> packElementList = new ArrayList<>();
        for (Element current : elementList) {
            boolean placed = false;
            boolean rotate = false;
            boolean startAgain = false;
            int x = (int) (Math.random() * circuitBoard.getWidth());
            int y = (int) (Math.random() * circuitBoard.getHeight());
            do {
                if (checkBaseRestrictions(current)) {
                    boolean intersectWithElement = false;
                    for (Element pack : packElementList) {
                        if (!current.equals(pack) && current.intersects(pack, minDistance)) {
                            intersectWithElement = true;
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
                    } else if (!startAgain && x == circuitBoard.getWidth() && y == circuitBoard.getHeight()) {
                        x = 0;
                        y = 0;
                        startAgain = true;
                    } else {
                        if (!rotate) {
                            current = new Element(current.getNumber(), 0, 0, current.getHeight(), current.getWidth());
                            x = 0;
                            y = 0;
                            rotate = true;
                        } else {
                            break;
                        }
                    }
                    current.setCenterX(x);
                    current.setCenterY(y);
                }
            } while (!placed);
        }
        return packElementList;
    }
}
