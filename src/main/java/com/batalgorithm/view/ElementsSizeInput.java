package com.batalgorithm.view;

import com.batalgorithm.main.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * Диалоговое окно для ввода информации о размерах элементов.
 */
public class ElementsSizeInput extends JDialog {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    public ElementsSizeInput(JFrame owner, int elementCount, InputCircuitBoardInformation boardInformation) throws HeadlessException {
        super(owner, "Ввод размеров элементов", true);

        JPanel elementSizePanel = new JPanel();
        elementSizePanel.setLayout(new BoxLayout(elementSizePanel, BoxLayout.Y_AXIS));

        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        numberFormat.setGroupingUsed(false);

        ArrayList<JFormattedTextField> aInputList = new ArrayList<>();
        ArrayList<JFormattedTextField> bInputList = new ArrayList<>();

        java.util.List<Element> elementList = boardInformation.getElementList();
        for (int i = 0; i < elementCount; i++) {
            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new BoxLayout(rowPanel, BoxLayout.X_AXIS));
            JFormattedTextField aInput = new JFormattedTextField(numberFormat);
            aInput.setColumns(10);
            JFormattedTextField bInput = new JFormattedTextField(numberFormat);
            bInput.setColumns(10);

            if (elementList != null) {
                Element element = elementList.get(i);
                aInput.setText(String.valueOf(element.getWidth()));
                bInput.setText(String.valueOf(element.getHeight()));
            }

            aInputList.add(aInput);
            bInputList.add(bInput);

            rowPanel.add(new JLabel("Елемент № " + (i + 1) + " "));
            rowPanel.add(new JLabel("a: "));
            rowPanel.add(aInput);
            rowPanel.add(new JLabel("b: "));
            rowPanel.add(bInput);
            elementSizePanel.add(rowPanel);
        }

        ElementsSizeInput thisDialog = this;
        JButton setElementSizeButton = new JButton("Задать размеры");
        setElementSizeButton.addActionListener(e -> {
            try {
                java.util.List<Element> elementList1 = new ArrayList<>();
                for (int i = 0; i < elementCount; i++) {
                    int a = Integer.valueOf(aInputList.get(i).getText());
                    int b = Integer.valueOf(bInputList.get(i).getText());
                    elementList1.add(new Element(i, 0, 0, a, b));
                }
                boardInformation.setElementList(elementList1);
                thisDialog.dispose();

            } catch (NumberFormatException e1) {
                new Error(owner, "Неправильно задан один или более параметров!");
            }
        });

        add(new JLabel("elementCount: " + elementCount));

        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        dialogPanel.add(elementSizePanel);
        dialogPanel.add(setElementSizeButton);

        setLayout(new BorderLayout());
        add(BorderLayout.CENTER, dialogPanel);

        setSize(WIDTH, HEIGHT);
        int x = owner.getX() + owner.getWidth() / 2 - WIDTH / 2;
        int y = owner.getY() + owner.getHeight() / 2 - HEIGHT / 2;
        setLocation(x, y);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        pack();
        setVisible(true);
        setResizable(false);
    }
}