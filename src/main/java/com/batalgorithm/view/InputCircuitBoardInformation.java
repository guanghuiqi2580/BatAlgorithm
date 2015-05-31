package com.batalgorithm.view;

import Jama.Matrix;
import com.batalgorithm.main.CircuitBoardInformation;
import com.batalgorithm.main.Element;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Окно, предназначенное для ввода информации об элементах, размерах платы, размерах запретной зоны и т.д.
 */
public class InputCircuitBoardInformation extends JPanel {

    private static final int DEFAULT_MATRIX_SIZE = 3;

    private JTextField inputCircuitSizeA;
    private JTextField inputCircuitSizeB;
    private JTextField inputRestrictedAreaX;
    private JTextField inputRestrictedAreaY;
    private JTextField inputRestrictedAreaA;
    private JTextField inputRestrictedAreaB;
    private JTextField inputAdjMatrixSize;
    private JTextField inputMinDistance;

    private Matrix adjMatrix;
    private java.util.List<Element> elementList;

    public InputCircuitBoardInformation(JFrame owner) {


//        elementList = new ArrayList<>();
//        for (int i = 0; i < DEFAULT_MATRIX_SIZE; i++) {
//            elementList.add(new Element(i, 0, 0, 10, 10));
//        }
//
//        adjMatrix = new Matrix(DEFAULT_MATRIX_SIZE, DEFAULT_MATRIX_SIZE);
//        for (int row = 0; row < DEFAULT_MATRIX_SIZE; row++) {
//            for (int col = 0; col < DEFAULT_MATRIX_SIZE; col++) {
//                if (row == col) {
//                    adjMatrix.set(row, col, 0);
//                } else {
//                    adjMatrix.set(row, col, 1);
//                }
//            }
//        }

        JPanel mainPanel = new JPanel();
        JLabel header = new JLabel("Информация об элементах и монтажной плате: ");
        header.setAlignmentX(CENTER_ALIGNMENT);
        mainPanel.add(header);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        JPanel inputFieldsPanel = new JPanel();
        inputFieldsPanel.setBorder(new LineBorder(Color.gray, 1));
        inputFieldsPanel.setLayout(new GridLayout(3, 2));

        initializeInputs();
        setDefaultValuesIntoInputs();

        JPanel inputCircuitSizePanel = new JPanel();
        inputCircuitSizePanel.setAlignmentX(LEFT_ALIGNMENT);
        inputCircuitSizePanel.setLayout(new BoxLayout(inputCircuitSizePanel, BoxLayout.Y_AXIS));
        JLabel circuitSize = new JLabel("Размеры монтажной платы");
        circuitSize.setAlignmentX(CENTER_ALIGNMENT);
        inputCircuitSizePanel.add(circuitSize);
        JPanel inputCircuitSizeFieldsPanel = new JPanel(new FlowLayout());
        JLabel circuitSizeA = new JLabel("A: ");
        inputCircuitSizeFieldsPanel.add(circuitSizeA);
        inputCircuitSizeFieldsPanel.add(inputCircuitSizeA);
        JLabel circuitSizeB = new JLabel("B: ");
        inputCircuitSizeFieldsPanel.add(circuitSizeB);
        inputCircuitSizeFieldsPanel.add(inputCircuitSizeB);
        inputCircuitSizePanel.add(inputCircuitSizeFieldsPanel);

        JPanel inputRestrictedAreaPanel = new JPanel();
        inputRestrictedAreaPanel.setLayout(new BoxLayout(inputRestrictedAreaPanel, BoxLayout.Y_AXIS));
        JLabel restrictedArea = new JLabel("Координаты центра запретной зоны");
        restrictedArea.setAlignmentX(CENTER_ALIGNMENT);
        inputRestrictedAreaPanel.add(restrictedArea);
        JPanel inputRestrictedAreaFieldsPanel = new JPanel(new FlowLayout());
        JLabel restrictedAreaX = new JLabel("X: ");
        inputRestrictedAreaFieldsPanel.add(restrictedAreaX);
        inputRestrictedAreaFieldsPanel.add(inputRestrictedAreaX);
        JLabel restrictedAreaY = new JLabel("Y: ");
        inputRestrictedAreaFieldsPanel.add(restrictedAreaY);
        inputRestrictedAreaFieldsPanel.add(inputRestrictedAreaY);
        inputRestrictedAreaPanel.add(inputRestrictedAreaFieldsPanel);

        JPanel inputRestrictedAreaSizePanel = new JPanel();
        inputRestrictedAreaSizePanel.setLayout(new BoxLayout(inputRestrictedAreaSizePanel, BoxLayout.Y_AXIS));
        JLabel restrictedAreaSize = new JLabel("Размеры запретной зоны");
        restrictedAreaSize.setAlignmentX(CENTER_ALIGNMENT);
        inputRestrictedAreaSizePanel.add(restrictedAreaSize);
        JPanel inputRestrictedAreaSizeFieldsPanel = new JPanel(new FlowLayout());
        JLabel restrictedAreaA = new JLabel("C: ");
        inputRestrictedAreaSizeFieldsPanel.add(restrictedAreaA);
        inputRestrictedAreaSizeFieldsPanel.add(inputRestrictedAreaA);
        JLabel restrictedAreaB = new JLabel("D: ");
        inputRestrictedAreaSizeFieldsPanel.add(restrictedAreaB);
        inputRestrictedAreaSizeFieldsPanel.add(inputRestrictedAreaB);
        inputRestrictedAreaSizePanel.add(inputRestrictedAreaSizeFieldsPanel);

        JPanel inputAdjMatrixSizePanel = new JPanel();
        inputAdjMatrixSizePanel.setLayout(new BoxLayout(inputAdjMatrixSizePanel, BoxLayout.Y_AXIS));
        JLabel adjacencyMatrixSize = new JLabel("Размер матрицы смежности: ");
        adjacencyMatrixSize.setAlignmentX(CENTER_ALIGNMENT);
        inputAdjMatrixSizePanel.add(adjacencyMatrixSize);
        JPanel inputAdjMatrixSizeInputPanel = new JPanel(new FlowLayout());
        inputAdjMatrixSizeInputPanel.add(inputAdjMatrixSize);
        JButton inputAdjMatrixButton = new JButton("Ввести матрицу");
        InputCircuitBoardInformation thisInput = this;
        inputAdjMatrixButton.addActionListener(e -> {
            try {
                Integer adjMatrixSize1 = Integer.valueOf(inputAdjMatrixSize.getText());
                if (adjMatrixSize1 > 0) {
                    new AdjacencyMatrixInput(owner, adjMatrixSize1, thisInput);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException exception) {
                new ErrorDialog(owner, "Неправильно задан размер матрицы смежности!");
            }
        });
        inputAdjMatrixSizeInputPanel.add(inputAdjMatrixButton);
        inputAdjMatrixSizePanel.add(inputAdjMatrixSizeInputPanel);

        JPanel inputSizeOfElementsPanel = new JPanel(new FlowLayout());
        JButton inputSizeOfElementsButton = new JButton("Ввести размеры элементов");
        inputSizeOfElementsButton.addActionListener(e -> {
            try {
                Integer adjMatrixSize1 = Integer.valueOf(inputAdjMatrixSize.getText());
                if (adjMatrixSize1 > 0) {
                    new ElementsSizeInput(owner, adjMatrixSize1, thisInput);
                } else {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException exception) {
                new ErrorDialog(owner, "Неправильно задан размер матрицы смежности!");
            }
        });
        inputSizeOfElementsPanel.add(inputSizeOfElementsButton);

        JPanel inputMinDistanceBetweenElementsPanel = new JPanel(new FlowLayout());
        JLabel minDistance = new JLabel("Минимальная дистанция:");
        inputMinDistanceBetweenElementsPanel.add(minDistance);
        inputMinDistanceBetweenElementsPanel.add(inputMinDistance);

        inputFieldsPanel.add(inputCircuitSizePanel);
        inputFieldsPanel.add(inputRestrictedAreaPanel);
        inputFieldsPanel.add(inputAdjMatrixSizePanel);
        inputFieldsPanel.add(inputRestrictedAreaSizePanel);
        inputFieldsPanel.add(inputSizeOfElementsPanel);
        inputFieldsPanel.add(inputMinDistanceBetweenElementsPanel);

        mainPanel.add(inputFieldsPanel);
        add(mainPanel);
    }

    public void setDefaultValuesIntoInputs() {
        inputCircuitSizeA.setText("140");
        inputCircuitSizeB.setText("150");
        inputRestrictedAreaX.setText("33");
        inputRestrictedAreaY.setText("33");
        inputRestrictedAreaA.setText("50");
        inputRestrictedAreaB.setText("50");
        inputAdjMatrixSize.setText(String.valueOf(DEFAULT_MATRIX_SIZE));
        inputMinDistance.setText("5");
    }

    private void initializeInputs() {
        NumberFormat numberInstance = NumberFormat.getNumberInstance();
        numberInstance.setGroupingUsed(false);
        inputCircuitSizeA = new JFormattedTextField(numberInstance);
        inputCircuitSizeB = new JFormattedTextField(numberInstance);
        inputRestrictedAreaX = new JFormattedTextField(numberInstance);
        inputRestrictedAreaY = new JFormattedTextField(numberInstance);
        inputRestrictedAreaA = new JFormattedTextField(numberInstance);
        inputRestrictedAreaB = new JFormattedTextField(numberInstance);
        inputAdjMatrixSize = new JFormattedTextField(numberInstance);
        inputMinDistance = new JFormattedTextField(numberInstance);

        inputCircuitSizeA.setColumns(10);
        inputCircuitSizeB.setColumns(10);
        inputRestrictedAreaX.setColumns(10);
        inputRestrictedAreaY.setColumns(10);
        inputRestrictedAreaA.setColumns(10);
        inputRestrictedAreaB.setColumns(10);
        inputAdjMatrixSize.setColumns(10);
        inputMinDistance.setColumns(10);
    }

    public Matrix getAdjMatrix() {
        return adjMatrix;
    }

    public void setAdjMatrix(Matrix adjMatrix) {
        this.adjMatrix = adjMatrix;
    }

    public List<Element> getElementList() {
        return elementList;
    }

    public void setElementList(List<Element> elementList) {
        this.elementList = elementList;
    }

    public CircuitBoardInformation getCircuitBoardInformation() {
        CircuitBoardInformation circuitBoardInformation = new CircuitBoardInformation();
        circuitBoardInformation.setCircuitBoardA(Integer.valueOf(inputCircuitSizeA.getText()));
        circuitBoardInformation.setCircuitBoardB(Integer.valueOf(inputCircuitSizeB.getText()));
        circuitBoardInformation.setRestrictedAreaX(Integer.valueOf(inputRestrictedAreaX.getText()));
        circuitBoardInformation.setRestrictedAreaY(Integer.valueOf(inputRestrictedAreaY.getText()));
        circuitBoardInformation.setRestrictedAreaA(Integer.valueOf(inputRestrictedAreaA.getText()));
        circuitBoardInformation.setRestrictedAreaB(Integer.valueOf(inputRestrictedAreaB.getText()));
        circuitBoardInformation.setAdjMatrix(getAdjMatrix());
        circuitBoardInformation.setElementList(elementList);
        circuitBoardInformation.setMinDistance(Integer.valueOf(inputMinDistance.getText()));
        return circuitBoardInformation;
    }
}
