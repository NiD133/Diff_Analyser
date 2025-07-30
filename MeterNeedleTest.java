package org.jfree.chart.plot.compass;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Stroke;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the {@link MeterNeedle} class.
 */
public class MeterNeedleTest {

    /**
     * Tests the equals() method of the MeterNeedle class to ensure
     * it correctly distinguishes between different field values.
     */
    @Test
    public void testEquals() {
        // Create two identical LineNeedle instances
        MeterNeedle needle1 = new LineNeedle();
        MeterNeedle needle2 = new LineNeedle();
        
        // Initially, both needles should be equal
        assertEquals(needle1, needle2);

        // Test fill paint
        GradientPaint fillPaint = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.BLUE);
        needle1.setFillPaint(fillPaint);
        assertNotEquals(needle1, needle2);
        needle2.setFillPaint(fillPaint);
        assertEquals(needle1, needle2);

        // Test outline paint
        GradientPaint outlinePaint = new GradientPaint(5.0f, 6.0f, Color.RED, 7.0f, 8.0f, Color.BLUE);
        needle1.setOutlinePaint(outlinePaint);
        assertNotEquals(needle1, needle2);
        needle2.setOutlinePaint(outlinePaint);
        assertEquals(needle1, needle2);

        // Test highlight paint
        GradientPaint highlightPaint = new GradientPaint(9.0f, 0.0f, Color.RED, 1.0f, 2.0f, Color.BLUE);
        needle1.setHighlightPaint(highlightPaint);
        assertNotEquals(needle1, needle2);
        needle2.setHighlightPaint(highlightPaint);
        assertEquals(needle1, needle2);

        // Test outline stroke
        Stroke outlineStroke = new BasicStroke(1.23f);
        needle1.setOutlineStroke(outlineStroke);
        assertNotEquals(needle1, needle2);
        needle2.setOutlineStroke(outlineStroke);
        assertEquals(needle1, needle2);

        // Test rotation x-coordinate
        double rotateX = 1.23;
        needle1.setRotateX(rotateX);
        assertNotEquals(needle1, needle2);
        needle2.setRotateX(rotateX);
        assertEquals(needle1, needle2);

        // Test rotation y-coordinate
        double rotateY = 4.56;
        needle1.setRotateY(rotateY);
        assertNotEquals(needle1, needle2);
        needle2.setRotateY(rotateY);
        assertEquals(needle1, needle2);

        // Test size
        int size = 11;
        needle1.setSize(size);
        assertNotEquals(needle1, needle2);
        needle2.setSize(size);
        assertEquals(needle1, needle2);
    }
}