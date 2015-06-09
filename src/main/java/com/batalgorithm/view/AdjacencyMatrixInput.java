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

    private java.util.List<InputField> inputList;
    private int matrixSize;

    public AdjacencyMatrixInput(JFrame owner, int matrixSize, InputCircuitBoardInformation boardInformation)
            throws HeadlessException {
        super(owner, "Adjacency matrix input", true);

        this.matrixSize = matrixSize;

        JPanel matrixPanel = new JPanel(new GridLayout(matrixSize, matrixSize, 5, 5));
        inputList = new ArrayList<>();

//        NumberFormat numberFormat = NumberFormat.getNumberInstance();
//        numberFormat.setGroupingUsed(false);

        for (int row = 0; row < matrixSize; row++) {
            for (int col = 0; col < matrixSize; col++) {
                InputField inputField = new InputField(row, col);
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
        if (adjMatrix != null && adjMatrix.getRowDimension() == matrixSize
                && adjMatrix.getColumnDimension() == matrixSize) {
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
                new ErrorDialog(owner, "Неправильно задан один или более параметров!");
            }
        });
        setMatrixButton.setAlignmentX(CENTER_ALIGNMENT);

        JButton setRandomValueButton = new JButton("Заполнить случайными значениями");
        setRandomValueButton.addActionListener(e -> {
            Runnable runnable = () -> {
                for (InputField inputField : inputList) {
                    int row = inputField.getRow();
                    int col = inputField.getCol();
                    if (col > row) {
                        inputField.setText(Integer.toString((int) (Math.random() * 6)));
                    }
                }
            };
            new Thread(runnable).start();
        });
        setRandomValueButton.setAlignmentX(CENTER_ALIGNMENT);

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        dialogPanel.add(matrixPanel);

        JScrollPane scrollPane = new JScrollPane(dialogPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        setLayout(new BorderLayout());

        add(BorderLayout.CENTER, scrollPane);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(setMatrixButton);
        buttonPanel.add(setRandomValueButton);
        add(BorderLayout.SOUTH, buttonPanel);

        int x = owner.getX() + owner.getWidth() / 2 - 400 / 2;
        int y = owner.getY() + owner.getHeight() / 2 - 400 / 2;
        setLocation(x, y);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();
        setVisible(true);
        setResizable(true);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }

    private class InputField extends JFormattedTextField {

        private int row;
        private int col;

        public InputField(int row, int col) {
//            super(numberFormat);
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
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                }

                public void update() {
                    Runnable runnable = () -> {
                        listener.setText("");
                        listener.setText(thisField.getText());
                    };
                    new Thread(runnable).start();
                }
            });
        }
    }
}