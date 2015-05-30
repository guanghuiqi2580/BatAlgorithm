package com.batalgorithm.main;

import com.batalgorithm.view.BoardWindow;

/**
 * Данный класс осуществяет запуск алгоритма на вычисление.
 */
public class AlgorithmRunner {

    public void calculate(CircuitBoardInformation boardInfo, AlgorithmInformation algInfo) {
        CircuitBoard circuitBoard = new CircuitBoard(boardInfo.getCircuitBoardA() / 2, boardInfo.getCircuitBoardB() / 2,
                boardInfo.getCircuitBoardA(), boardInfo.getCircuitBoardA());
        RestrictedArea restrictedArea = new RestrictedArea(boardInfo.getRestrictedAreaX(), boardInfo.getRestrictedAreaY(),
                boardInfo.getRestrictedAreaA(), boardInfo.getRestrictedAreaB());
        BatAlgorithm batAlgorithm = new BatAlgorithm(circuitBoard, restrictedArea,
                boardInfo.getAdjMatrix(), boardInfo.getElementList(), boardInfo.getMinDistance());
        batAlgorithm.calculate(algInfo.getN(), algInfo.getN_gen(), algInfo.getA(), algInfo.getR(), algInfo.getQmin(),
                algInfo.getQmax(), algInfo.getMaxStep());
        new BoardWindow(circuitBoard, restrictedArea, batAlgorithm);
    }
}
