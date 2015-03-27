package com.batalgorithm.view;

import com.batalgorithm.main.CircuitBoard;
import com.batalgorithm.main.Element;
import com.batalgorithm.main.RestrictedArea;

import javax.swing.*;
import java.awt.*;

/**
 * Окно приложения для схематического отображения размещения элементов на плате.
 */
public class BoardWindow extends JFrame {

    private static int ZOOM = 3;
    private static Label zoomLabel;
    private int width;
    private int height;

    public BoardWindow(CircuitBoard circuitBoard, RestrictedArea restrictedArea, final java.util.List<Element>
            elementList) {
        width = circuitBoard.getWidth();
        height = circuitBoard.getHeight();
        setSize(ZOOM * width + 30, ZOOM * height + 100);
        setResizable(true);
        BoardPanel boardPanel = new BoardPanel(width, height, restrictedArea, elementList);
        add(BorderLayout.CENTER, boardPanel);
        zoomLabel = new Label("Zoom: x" + ZOOM);
        Button zoomPlus = new Button(" + ");
        Button zoomMinus = new Button(" - ");
        zoomPlus.addActionListener(e -> {
            if (e.getActionCommand().equals(" + ")) {
                if (ZOOM < 10) {
                    ZOOM++;
                } else {
                    ZOOM = 1;
                }
                zoomLabel.setText("Zoom: x" + ZOOM);
                setSize(ZOOM * width + 30, ZOOM * height + 100);
                repaint();
            }
        });
        zoomMinus.addActionListener(e -> {
            if (e.getActionCommand().equals(" - ")) {
                if (ZOOM > 1) {
                    ZOOM--;
                } else {
                    ZOOM = 1;
                }
                zoomLabel.setText("Zoom: x" + ZOOM);
                setSize(ZOOM * width + 30, ZOOM * height + 100);
                repaint();
            }
        });
        JPanel zoomPanel = new JPanel();
        zoomPanel.add(zoomLabel);
        zoomPanel.add(zoomPlus);
        zoomPanel.add(zoomMinus);
        add(BorderLayout.SOUTH, zoomPanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    class BoardPanel extends JPanel {

        private int width;
        private int height;
        private RestrictedArea restrictedArea;
        private java.util.List<Element> elementList;

        public BoardPanel(int width, int height, RestrictedArea restrictedArea, java.util.List<Element> elementList) {
            this.width = width;
            this.height = height;
            this.restrictedArea = restrictedArea;
            this.elementList = elementList;
            setSize(ZOOM * width + 10, ZOOM * height + 10);
            setVisible(true);
        }

        @Override
        public void paint(Graphics g) {
            int offsetX = 5;
            int offsetY = 5;
            g.setColor(Color.BLACK);
            g.drawRect(offsetX, offsetY, ZOOM * width, ZOOM * height);
            g.setColor(Color.RED);
            int startX = offsetX + ZOOM * (restrictedArea.getMinX());
            int startY = offsetY + ZOOM * (restrictedArea.getMinY());
            g.fillRect(startX, startY, ZOOM * restrictedArea.getWidth(), ZOOM * restrictedArea.getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(startX, startY, ZOOM * restrictedArea.getWidth(), ZOOM * restrictedArea.getHeight());
            for (Element e : elementList) {
                int startElementX = offsetX + ZOOM * (e.getMinX());
                int startElementY = offsetY + ZOOM * (e.getMinY());
                g.setColor(Color.GREEN);
                g.fillRect(startElementX, startElementY, ZOOM * e.getWidth(), ZOOM * e.getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(startElementX, startElementY, ZOOM * e.getWidth(), ZOOM * e.getHeight());
            }
            // После отрисовки всех элементов отрисовываем подписи к ним
            for (Element e : elementList) {
                g.drawString("#" + e.getNumber() + ", x=" + e.getCenterX() + ", y=" + e.getCenterY(),
                        ZOOM * e.getCenterX(), ZOOM * (e.getCenterY()));
            }
            g.drawString("x=" + restrictedArea.getMinX() + ", y=" + restrictedArea.getMinY(), startX + ZOOM * 5,
                    startY + ZOOM * 5);
        }
    }
}
