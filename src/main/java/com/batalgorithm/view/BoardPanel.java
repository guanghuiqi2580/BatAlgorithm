package com.batalgorithm.view;

import com.batalgorithm.main.Element;
import com.batalgorithm.main.RestrictedArea;

import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JPanel {

    private int zoom;
    private int width;
    private int height;
    private RestrictedArea restrictedArea;
    private java.util.List<Element> elementList;

    public BoardPanel(int width, int height, RestrictedArea restrictedArea, java.util.List<Element> elementList) {
        this.width = width;
        this.height = height;
        this.restrictedArea = restrictedArea;
        this.elementList = elementList;
        setSize(zoom * width + 10, zoom * height + 10);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g) {
        int offsetX = 5;
        int offsetY = 5;
        g.setColor(Color.BLACK);
        g.drawRect(offsetX, offsetY, zoom * width, zoom * height);
        g.setColor(Color.RED);
        int startX = offsetX + zoom * (restrictedArea.getMinX());
        int startY = offsetY + zoom * (restrictedArea.getMinY());
        g.fillRect(startX, startY, zoom * restrictedArea.getWidth(), zoom * restrictedArea.getHeight());
        g.setColor(Color.BLACK);
        g.drawRect(startX, startY, zoom * restrictedArea.getWidth(), zoom * restrictedArea.getHeight());
        for (Element e : elementList) {
            int startElementX = offsetX + zoom * (e.getMinX());
            int startElementY = offsetY + zoom * (e.getMinY());
            g.setColor(Color.GREEN);
            g.fillRect(startElementX, startElementY, zoom * e.getWidth(), zoom * e.getHeight());
            g.setColor(Color.BLACK);
            g.drawRect(startElementX, startElementY, zoom * e.getWidth(), zoom * e.getHeight());
        }
        // После отрисовки всех элементов отрисовываем подписи к ним
        for (Element e : elementList) {
            g.drawString("#" + e.getNumber(),
                    zoom * e.getCenterX(), zoom * (e.getCenterY()));
        }
        g.drawString("Запретная зона", startX + zoom * restrictedArea.getWidth() / 4,
                startY + zoom * restrictedArea.getHeight() / 2);
    }

    public void setZoom(int zoom) {
        this.zoom = zoom;
        repaint();
    }
}