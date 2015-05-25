package com.batalgorithm.view;

import javax.swing.*;
import java.awt.*;

/**
 * Диалоговое окно для вывода сообщений ошибках.
 */
public class Error extends JDialog {

    private Error thisError;

    public Error(JFrame owner, String message) throws HeadlessException {
        super(owner, "Ошибка", true);
        thisError = this;
        setLayout(new BorderLayout());
        JLabel messageLabel = new JLabel(message);
        JPanel panel = new JPanel();
        panel.add(messageLabel);
        panel.setAlignmentX(CENTER_ALIGNMENT);

        add(BorderLayout.CENTER, panel);

        JButton okButton = new JButton("ОК");
        okButton.addActionListener(e -> thisError.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(okButton);
        buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
        add(BorderLayout.SOUTH, buttonPanel);

        pack();
        int x = owner.getX() + owner.getWidth() / 2 - getWidth() / 2;
        int y = owner.getY() + owner.getHeight() / 2 - getHeight() / 2;
        setLocation(x, y);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setVisible(true);
    }
}
