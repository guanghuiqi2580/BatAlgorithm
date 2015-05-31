package com.batalgorithm.view;

import com.batalgorithm.main.BatAlgorithm;
import com.batalgorithm.main.CircuitBoard;
import com.batalgorithm.main.RestrictedArea;

import javax.swing.*;
import java.awt.*;

/**
 * Окно приложения для схематического отображения размещения элементов на плате.
 */
public class BoardWindow extends JFrame {

    private int zoom = 3;
    private static Label zoomLabel;
    private int width;
    private int height;

    public BoardWindow(CircuitBoard circuitBoard, RestrictedArea restrictedArea, BatAlgorithm batAlgorithm) {
        width = circuitBoard.getWidth();
        height = circuitBoard.getHeight();
        setSize(zoom * width * 2 + 40, zoom * height + 150);
        BoardPanel beforeBoardPanel = new BoardPanel(width, height, restrictedArea, batAlgorithm.getInitial());
        beforeBoardPanel.setZoom(zoom);
        BoardPanel afterBoardPanel = new BoardPanel(width, height, restrictedArea, batAlgorithm.getBest());
        afterBoardPanel.setZoom(zoom);
        add(BorderLayout.CENTER, afterBoardPanel);
        zoomLabel = new Label("Zoom: x" + zoom);
        Button zoomPlus = new Button(" + ");
        Button zoomMinus = new Button(" - ");
        zoomPlus.addActionListener(e -> {
            if (e.getActionCommand().equals(" + ")) {
                if (zoom < 10) {
                    zoom++;
                } else {
                    zoom = 1;
                }
                beforeBoardPanel.setZoom(zoom);
                afterBoardPanel.setZoom(zoom);
                zoomLabel.setText("Zoom: x" + zoom);
                setSize(zoom * width + 30, zoom * height + 100);
                repaint();
            }
        });
        zoomMinus.addActionListener(e -> {
            if (e.getActionCommand().equals(" - ")) {
                if (zoom > 1) {
                    zoom--;
                } else {
                    zoom = 1;
                }
                beforeBoardPanel.setZoom(zoom);
                afterBoardPanel.setZoom(zoom);
                zoomLabel.setText("Zoom: x" + zoom);
                setSize(zoom * width + 30, zoom * height + 100);
                repaint();
            }
        });
        JPanel zoomPanel = new JPanel();
        zoomPanel.add(zoomLabel);
        zoomPanel.add(zoomPlus);
        zoomPanel.add(zoomMinus);

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayout(2, 2));
        JLabel beforeLabel = new JLabel("До выполнения алгоритма: ");
        JLabel afterLabel = new JLabel("После размещения: ");
        JLabel beforeL = new JLabel("L = " + batAlgorithm.getInitialMinLength());
        JLabel afterL = new JLabel("L = " + batAlgorithm.getMinLength());
        titlePanel.add(beforeLabel);
        titlePanel.add(afterLabel);
        titlePanel.add(beforeL);
        titlePanel.add(afterL);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
        centerPanel.add(beforeBoardPanel);
        centerPanel.add(afterBoardPanel);
        add(BorderLayout.NORTH, titlePanel);
        add(BorderLayout.CENTER, centerPanel);
        add(BorderLayout.SOUTH, zoomPanel);
        setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        setSize(zoom * width * 2 + 40, zoom * height + 150);
    }
}
