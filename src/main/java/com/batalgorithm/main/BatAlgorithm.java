package com.batalgorithm.main;

import Jama.Matrix;
import com.batalgorithm.utils.*;

import java.util.*;
import java.util.logging.Logger;

import static com.batalgorithm.utils.MatrixHelper.getRow;
import static com.batalgorithm.utils.MatrixHelper.setRow;

public class BatAlgorithm {

    private static final Logger LOG = Logger.getLogger(BatAlgorithm.class.getName());
    private static final int NUMBER_OF_CYCLE_EXECUTION = 1000;

    private CircuitBoard circuitBoard;
    private RestrictedArea restrictedArea;
    private Matrix adjacencyMatrix;
    private List<Element> elementList;
    private int minDistance;

    private List<Element> best;
    private double minLength;
    private int iter;

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
        for (int t = 0; t < N_gen; t++) {
            // Цикл по всем летучим мышам / решениям
            for (int i = 0; i < n; i++) {
                List<Element> currSolution = solutions.get(i);
                Matrix vXRow = getXRow(currSolution);
                for (int row = 0; row < vXRow.getRowDimension(); row++) {
                    for (int col = 0; col < vXRow.getColumnDimension(); col++) {
                        vXRow.set(row, col, (Qmin + Qmin + Qmax * MatlabSubstitute.rand() * MatlabSubstitute.randSign()));
                    }
                }
                Matrix vYRow = getYRow(currSolution);
                for (int row = 0; row < vYRow.getRowDimension(); row++) {
                    for (int col = 0; col < vYRow.getColumnDimension(); col++) {
                        vYRow.set(row, col, (Qmin + Qmin + Qmax * MatlabSubstitute.rand() * MatlabSubstitute.randSign()));
                    }
                }
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
        adjacencyPack(best);
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
                resultList = adjacencyPack(copyElementsList);
                placed = true;
            } catch (Exception e) {
                e.printStackTrace();
                placed = false;
            }
            if (count++ > NUMBER_OF_CYCLE_EXECUTION) {
                break;
            }
        } while (!placed);
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
            boolean reverse = false;
            int startX = current.getCenterX();
            int startY = current.getCenterY();
            int x = startX;
            int y = startY;
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
                    if (reverse) {
                        if (x > 0) {
                            x--;
                        } else if (y > 0) {
                            x = startX;
                            y--;
                        }
                    } else {
                        if (x < circuitBoard.getWidth()) {
                            x++;
                        } else if (y < circuitBoard.getHeight()) {
                            x = 0;
                            y++;
                        } else if (x == circuitBoard.getWidth() && y == circuitBoard.getHeight()) {
                            x = startX;
                            y = startY;
                            reverse = true;
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

    public int getIter() {
        return iter;
    }

    public List<Element> getBest() {
        return best;
    }

    private List<Element> adjacencyPack(List<Element> elementList) {
        // достаем элемент с макисмальной суммарной связью с другими элементами
        List<Element> tmpElementList = new ArrayList<>();
        for (Element e : elementList) {
            tmpElementList.add(e.copy());
        }
        Map<Element, Integer> elementToSumLink = new HashMap<>();
        for (Element e : elementList) {
            elementToSumLink.put(e, 0);
        }
        for (Element e1 : elementList) {
            for (Element e2 : elementList) {
                if (!e1.equals(e2)) {
                    int link = (int) adjacencyMatrix.get(e1.getNumber(), e2.getNumber());
                    if (link > 0) {
                        elementToSumLink.put(e1, elementToSumLink.get(e1) + link);
                    }
                }
            }
        }
        // заполняем карту, которая будет содержать для элемента связанные с ним элементы
        Map<Element, List<Element>> elementLinkMap = new HashMap<>();
        for (Element e : elementList) {
            elementLinkMap.put(e, new ArrayList<>());
        }
        for (Element e1 : elementList) {
            for (Element e2 : elementList) {
                if (!e1.equals(e2)) {
                    int link = (int) adjacencyMatrix.get(e1.getNumber(), e2.getNumber());
                    if (link > 0) {
                        List<Element> linkList = elementLinkMap.get(e1);
                        linkList.add(e2);
                    }
                }
            }
        }
        List<Element> packElementList = new ArrayList<>();
        Element headElement = findElementWithMaxLink(elementToSumLink);
        // генерируем координаты для первого элемента в верхней левой четверти платы
        int startX = circuitBoard.getWidth() / 4 + (int) (Math.random() * circuitBoard.getWidth() / 2);
        int startY = circuitBoard.getHeight() / 4 + (int) (Math.random() * circuitBoard.getHeight() / 2);
        place(startX, startY, headElement, packElementList);
        removeByNumber(tmpElementList, headElement.getNumber());
        removeByNumber(elementToSumLink, headElement.getNumber());
        while (tmpElementList.size() > 0) {
            List<Element> linkedElements = getByNumber(elementLinkMap, headElement.getNumber());
            if (linkedElements != null) {
                Element prevElement = headElement;
                for (Element e : linkedElements) {
                    startX = prevElement.getMinX() - e.getWidth();
                    startY = prevElement.getMinY() - e.getHeight();
                    place(startX, startY, e, packElementList);
                    removeByNumber(tmpElementList, e.getNumber());
                    removeByNumber(elementToSumLink, e.getNumber());
                    prevElement = e;
                }
                headElement = findElementWithMaxLink(elementToSumLink);
            } else {
                headElement = findElementWithMaxLink(elementToSumLink);
            }
            if (headElement == null) {
                headElement = findElementWithMaxLink(elementToSumLink);
            }
            place(startX, startY, headElement, packElementList);
            removeByNumber(tmpElementList, headElement.getNumber());
            removeByNumber(elementToSumLink, headElement.getNumber());
        }
        return packElementList;
    }

    private void removeByNumber(List<Element> list, int number) {
        Element forRemove = null;
        for (Element e : list) {
            if (e.getNumber() == number) {
                forRemove = e;
                break;
            }
        }
        list.remove(forRemove);
    }

    private void removeByNumber(Map<Element, Integer> map, int number) {
        Element forRemove = null;
        for (Element e : map.keySet()) {
            if (e.getNumber() == number) {
                forRemove = e;
                break;
            }
        }
        map.remove(forRemove);
    }

    private List<Element> getByNumber(Map<Element, List<Element>> map, int number) {
        Set<Element> elements = map.keySet();
        for (Element e : elements) {
            if (e.getNumber() == number) {
                return map.get(e);
            }
        }
        return null;
    }

    private Integer getByNumber(int number, Map<Element, Integer> map) {
        Set<Element> keySet = map.keySet();
        Element findElement = null;
        for (Element e : keySet) {
            if (e.getNumber() == number) {
                findElement = e;
                break;
            }
        }
        return map.get(findElement);
    }

    private Element findElementWithMaxLink(Map<Element, Integer> elementToSumLink) {
        Object[] array = elementToSumLink.values().toArray();
        Arrays.sort(array, new Comparator<Object>() {
            @Override public int compare(Object o1, Object o2) {
                int i1 = (int) o1;
                int i2 = (int) o2;
                if (i1 < i2) {
                    return 1;
                } else if (i2 < i1) {
                    return -1;
                }
                return 0;
            }
        });
        Set<Element> elements = elementToSumLink.keySet();
        Element findElement = null;
        for (int i = 0; i < array.length; i++) {
            for (Element e : elements) {
                if (getByNumber(e.getNumber(), elementToSumLink) == array[i]) {
                    findElement = e;
                    break;
                }
            }
            if (findElement != null) {
                break;
            }
        }
        return findElement;
    }

    private void place(int startX, int startY, Element element, List<Element> packElementList) {
        boolean placed = false;
        boolean rotate = false;
        boolean reverse = false;
        int x = startX;
        int y = startY;
        do {
            element.setCenterX(x);
            element.setCenterY(y);
            if (checkBaseRestrictions(element)) {
                boolean intersectWithElement = false;
                for (Element pack : packElementList) {
                    if (!element.equals(pack) && element.intersects(pack, minDistance)) {
                        intersectWithElement = true;
                    }
                }
                if (!intersectWithElement) {
                    packElementList.add(element);
                    placed = true;
                }
            }
            if (!placed) {
                if (reverse) {
                    if (y > 0) {
                        y--;
                    } else if (x > 0) {
                        y = startY;
                        x--;
                    }
                } else {
                    if (y < circuitBoard.getHeight()) {
                        y++;
                    } else if (x < circuitBoard.getWidth()) {
                        y = startY;
                        x++;
                    } else if (!reverse && x == circuitBoard.getWidth() && y == circuitBoard.getHeight()) {
                        x = startX;
                        y = startY;
                        reverse = true;
                    } else {
                        if (!rotate) {
                            element = new Element(element.getNumber(), 0, 0, element.getHeight(), element.getWidth());
                            x = 0;
                            y = 0;
                            rotate = true;
                        } else {
                            break;
                        }
                    }
                }
            }
        } while (!placed);
    }
}
