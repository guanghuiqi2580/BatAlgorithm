package com.batalgorithm.view;

import javax.swing.*;
import java.awt.*;

/**
 * Диалоговое окно для ввода информации о размерах элементов.
 */
public class ElementsSizeInput extends JDialog {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    public ElementsSizeInput(JFrame owner) throws HeadlessException {
        super(owner, "Input size of elements", true);
        setSize(WIDTH, HEIGHT);
        int x = owner.getX() + owner.getWidth() / 2 - WIDTH / 2;
        int y = owner.getY() + owner.getHeight() / 2 - HEIGHT / 2;
        setLocation(x, y);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setVisible(true);
    }
}