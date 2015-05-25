package com.batalgorithm.view;

import javax.swing.*;
import java.awt.*;

/**
 * Диалоговое окно для ввода матрицы смежности.
 */
public class AdjacencyMatrixInput extends JDialog {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    public AdjacencyMatrixInput(JFrame owner, int matrixSize) throws HeadlessException {
        super(owner, "Adjacency matrix input", true);
        add(new Label("Matrix size is " + matrixSize));
        setSize(WIDTH, HEIGHT);
        int x = owner.getX() + owner.getWidth() / 2 - WIDTH / 2;
        int y = owner.getY() + owner.getHeight() / 2 - HEIGHT / 2;
        setLocation(x, y);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setVisible(true);
    }
}