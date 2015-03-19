package com.batalgorithm.main;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class RestrictedAreaTest {

    private RestrictedArea restrictedArea;
    private int A = 7;
    private int B = 13;
    private int X = 20;
    private int Y = 43;

    @Before
    public void setUp() throws Exception {
        restrictedArea = new RestrictedArea(A, B, X, Y);
    }

    @Test
    public void testContains() throws Exception {
        Assert.assertFalse(restrictedArea.contains(0, 0));
        Assert.assertTrue(restrictedArea.contains(20, 43));
        Assert.assertTrue(restrictedArea.contains(13.5, 39.5));
        Assert.assertTrue(restrictedArea.contains(26.5, 46.5));
    }
}