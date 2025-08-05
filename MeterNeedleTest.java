/* 
 * JFreeChart : a chart library for the Java(tm) platform
 * =====================================================
 * 
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 * 
 * Project Info: https://www.jfree.org/jfreechart/index.html
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation; either version 2.1 of the License, or 
 * (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY 
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public 
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with this library; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, 
 * USA.
 * 
 * [Oracle and Java are registered trademarks of Oracle and/or its affiliates. 
 * Other names may be trademarks of their respective owners.]
 * 
 * --------------------
 * MeterNeedleTest.java
 * --------------------
 * (C) Copyright 2005-present, by David Gilbert and Contributors.
 * 
 * Original Author: David Gilbert;
 * Contributor(s): -;
 */

package org.jfree.chart.plot.compass;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Stroke;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link MeterNeedle} class.
 */
public class MeterNeedleTest {

    private MeterNeedle needle1;
    private MeterNeedle needle2;

    @BeforeEach
    public void setUp() {
        needle1 = new LineNeedle();
        needle2 = new LineNeedle();
    }

    @Test
    public void testEqualAfterConstruction() {
        assertEquals(needle1, needle2);
    }

    @Test
    public void testFillPaint() {
        GradientPaint paint = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.BLUE);
        needle1.setFillPaint(paint);
        assertNotEquals(needle1, needle2);
        needle2.setFillPaint(paint);
        assertEquals(needle1, needle2);
    }

    @Test
    public void testOutlinePaint() {
        GradientPaint paint = new GradientPaint(5.0f, 6.0f, Color.RED, 7.0f, 8.0f, Color.BLUE);
        needle1.setOutlinePaint(paint);
        assertNotEquals(needle1, needle2);
        needle2.setOutlinePaint(paint);
        assertEquals(needle1, needle2);
    }

    @Test
    public void testHighlightPaint() {
        GradientPaint paint = new GradientPaint(9.0f, 0.0f, Color.RED, 1.0f, 2.0f, Color.BLUE);
        needle1.setHighlightPaint(paint);
        assertNotEquals(needle1, needle2);
        needle2.setHighlightPaint(paint);
        assertEquals(needle1, needle2);
    }

    @Test
    public void testOutlineStroke() {
        Stroke stroke = new BasicStroke(1.23f);
        needle1.setOutlineStroke(stroke);
        assertNotEquals(needle1, needle2);
        needle2.setOutlineStroke(stroke);
        assertEquals(needle1, needle2);
    }

    @Test
    public void testRotateX() {
        double value = 1.23;
        needle1.setRotateX(value);
        assertNotEquals(needle1, needle2);
        needle2.setRotateX(value);
        assertEquals(needle1, needle2);
    }

    @Test
    public void testRotateY() {
        double value = 4.56;
        needle1.setRotateY(value);
        assertNotEquals(needle1, needle2);
        needle2.setRotateY(value);
        assertEquals(needle1, needle2);
    }

    @Test
    public void testSize() {
        int size = 11;
        needle1.setSize(size);
        assertNotEquals(needle1, needle2);
        needle2.setSize(size);
        assertEquals(needle1, needle2);
    }
}