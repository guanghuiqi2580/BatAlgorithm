package com.batalgorithm.main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Окно приложения для схематического отображения размещения элементов на плате.
 */
public class BoardWindow extends JFrame {

    private static int ZOOM = 5;
    private static Label zoomLabel;
    private int width;
    private int height;

    public BoardWindow(CircuitBoard circuitBoard, RestrictedArea restrictedArea, final java.util.List<Element>
            elementList) {
        width = (int) circuitBoard.getWidth();
        height = (int) circuitBoard.getHeight();
        setSize(ZOOM * width + 30, ZOOM * height + 100);
        setResizable(true);
        BoardPanel boardPanel = new BoardPanel(width, height, restrictedArea, elementList);
        add(BorderLayout.CENTER, boardPanel);
        zoomLabel = new Label("Zoom: x" + ZOOM);
        Button zoomPlus = new Button(" + ");
        Button zoomMinus = new Button(" - ");
        zoomPlus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });
        zoomMinus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });
        JPanel zoomPanel = new JPanel();
        zoomPanel.add(zoomLabel);
        zoomPanel.add(zoomPlus);
        zoomPanel.add(zoomMinus);
        add(BorderLayout.SOUTH, zoomPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            int startX = offsetX + ZOOM * (int) (restrictedArea.getX());
            int startY = offsetY + ZOOM * (int) (restrictedArea.getY());
            g.fillRect(startX, startY, ZOOM * (int) restrictedArea.getWidth(), ZOOM * (int) restrictedArea.getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(startX, startY, ZOOM * (int) restrictedArea.getWidth(), ZOOM * (int) restrictedArea.getHeight());
            g.drawString("x=" + (int) restrictedArea.getX() + ", y=" + (int) restrictedArea.getY(), startX + ZOOM * 5,
                    startY + ZOOM * 5);
            for (Element e : elementList) {
                int startElementX = offsetX + ZOOM * (int) (e.getX());
                int startElementY = offsetY + ZOOM * (int) (e.getY());
                g.setColor(Color.GREEN);
                g.fillRect(startElementX, startElementY, ZOOM * (int) e.getWidth(), ZOOM * (int) e.getHeight());
                g.setColor(Color.BLACK);
                g.drawRect(startElementX, startElementY, ZOOM * (int) e.getWidth(), ZOOM * (int) e.getHeight());
                g.drawString("#" + e.getNumber() + ", x=" + (int) e.getCenterX() + ", y=" + (int) e.getCenterY(), ZOOM *
                        (int) e
                                .getCenterX(), ZOOM * (int) (e.getCenterY()));
            }
        }
    }
}
