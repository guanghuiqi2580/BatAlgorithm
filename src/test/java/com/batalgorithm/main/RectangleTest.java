package com.batalgorithm.main;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RectangleTest {

    private int centerX = 100;
    private int centerY = 80;
    private int width = 200;
    private int height = 40;
    private Rectangle rectangle;

    @Before
    public void setUp() throws Exception {
        rectangle = new Rectangle(centerX, centerY, width, height);
    }

    @Test
    public void testGetMinX() throws Exception {
        Assert.assertEquals(rectangle.getMinX(), centerX - width / 2);
    }

    @Test
    public void testGetMinY() throws Exception {
        Assert.assertEquals(rectangle.getMinY(), centerY - height / 2);
    }

    @Test
    public void testGetMaxX() throws Exception {
        Assert.assertEquals(rectangle.getMaxX(), centerX + width / 2);
    }

    @Test
    public void testGetMaxY() throws Exception {
        Assert.assertEquals(rectangle.getMaxY(), centerY + height / 2);
    }

    @Test
    public void testIntersects() throws Exception {
        Rectangle intersectRectangle = new Rectangle(rectangle.getMinX(), rectangle.getMinY(), 10, 20);
        Assert.assertTrue(rectangle.intersects(intersectRectangle, 0));

        int distance = 10;
        Rectangle notIntersectRectangle = new Rectangle(rectangle.getCenterX(),
                rectangle.getCenterY() + rectangle.getHeight() + distance, rectangle.getWidth(), rectangle.getHeight());
        Assert.assertFalse(rectangle.intersects(notIntersectRectangle, 0));
        Assert.assertFalse(rectangle.intersects(notIntersectRectangle, distance));
        Assert.assertTrue(rectangle.intersects(notIntersectRectangle, distance + 1));

        Rectangle equalsRectangle = new Rectangle(rectangle);
        Assert.assertTrue(rectangle.intersects(equalsRectangle, 0));

        Rectangle containsRectangle = new Rectangle(rectangle.getCenterX(), rectangle.getCenterY(), 10, 10);
        Assert.assertTrue(rectangle.intersects(containsRectangle, 0));
    }

    @Test
    public void testContains() throws Exception {
        Rectangle containsRectangle = new Rectangle(rectangle.getCenterX(), rectangle.getCenterY(), 10, 10);
        Assert.assertTrue(rectangle.contains(containsRectangle));
        Rectangle notContainsRectangle = new Rectangle(rectangle.getCenterX() + 1000, rectangle.getCenterY() + 1000,
                10, 10);
        Assert.assertFalse(rectangle.contains(notContainsRectangle));
    }
}