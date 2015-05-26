package com.batalgorithm.view;

import Jama.Matrix;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Диалоговое окно для ввода матрицы смежности.
 */
public class AdjacencyMatrixInput extends JDialog {

    public AdjacencyMatrixInput(JFrame owner, int matrixSize, InputCircuitBoardInformation boardInformation)
            throws HeadlessException {
        super(owner, "Adjacency matrix input", true);

        JPanel matrixPanel = new JPanel(new GridLayout(matrixSize, matrixSize, 5, 5));
        java.util.List<InputField> inputList = new ArrayList<>();

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);

        for (int row = 0; row < matrixSize; row++) {
            for (int col = 0; col < matrixSize; col++) {
                InputField inputField = new InputField(row, col, numberFormat);
                if (row == col) {
                    inputField.setEditable(false);
                    inputField.setText("0");
                } else if (col < row) {
                    inputField.setEditable(false);
                }
                inputList.add(inputField);
            }
        }

        Matrix adjMatrix = boardInformation.getAdjMatrix();
        if (adjMatrix != null && adjMatrix.getRowDimension() == matrixSize && adjMatrix.getColumnDimension() == matrixSize) {
            for (int row = 0; row < matrixSize; row++) {
                for (int col = 0; col < matrixSize; col++) {
                    for (InputField field : inputList) {
                        if (field.getRow() == row && field.getCol() == col) {
                            field.setText(String.valueOf((int) adjMatrix.get(row, col)));
                            break;
                        }
                    }
                }
            }
        }

        for (int row = 0; row < matrixSize; row++) {
            for (int col = row; col < matrixSize; col++) {
                if (row != col) {
                    InputField field = null;
                    for (InputField baseField : inputList) {
                        if (baseField.getRow() == row && baseField.getCol() == col) {
                            field = baseField;
                            break;
                        }
                    }
                    if (field != null) {
                        for (InputField listenerField : inputList) {
                            if (listenerField.getRow() == col && listenerField.getCol() == row) {
                                field.setListener(listenerField);
                            }
                        }
                    }
                }
            }
        }

        for (int row = 0; row < matrixSize; row++) {
            for (int col = 0; col < matrixSize; col++) {
                for (InputField field : inputList) {
                    if (field.getRow() == row && field.getCol() == col) {
                        matrixPanel.add(field);
                        break;
                    }
                }
            }
        }

        AdjacencyMatrixInput thisDialog = this;
        JButton setMatrixButton = new JButton("Задать матрицу");
        setMatrixButton.addActionListener(e -> {
            try {
                Matrix newAdjMatrix = new Matrix(matrixSize, matrixSize);
                for (InputField inputField : inputList) {
                    newAdjMatrix.set(inputField.getRow(), inputField.getCol(), Integer.valueOf(inputField.getText()));
                }
                boardInformation.setAdjMatrix(newAdjMatrix);
                thisDialog.dispose();
            } catch (NumberFormatException e1) {
                new Error(owner, "Неправильно задан один или более параметров!");
            }
        });
        setMatrixButton.setAlignmentX(CENTER_ALIGNMENT);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        dialogPanel.add(matrixPanel);
        dialogPanel.add(setMatrixButton);

        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, dialogPanel);

        int x = owner.getX() + owner.getWidth() / 2 - getWidth() / 2;
        int y = owner.getY() + owner.getHeight() / 2 - getHeight() / 2;
        setLocation(x, y);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();
        setVisible(true);
    }

    private class InputField extends JFormattedTextField {

        private int row;
        private int col;

        public InputField(int row, int col, NumberFormat numberFormat) {
            super(numberFormat);
            this.setColumns(5);
            this.row = row;
            this.col = col;
        }

        public int getRow() {
            return row;
        }

        public void setRow(int row) {
            this.row = row;
        }

        public int getCol() {
            return col;
        }

        public void setCol(int col) {
            this.col = col;
        }

        public void setListener(JFormattedTextField listener) {
            JFormattedTextField thisField = this;
            thisField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    update();
                }

                public void update() {
                    Runnable runnable = () -> listener.setText(thisField.getText());
                    new Thread(runnable).start();
                }
            });
        }
    }
}