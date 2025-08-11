package org.jfree.chart.plot.compass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Stroke;
import org.junit.jupiter.api.Test;

/**
 * Focused, readable tests for MeterNeedle.equals()/hashCode().
 * Each test isolates a single field to make intent and failures clear.
 */
public class MeterNeedleTest {

    // Test fixtures used across tests to avoid magic numbers in method bodies
    private static final GradientPaint FILL_PAINT =
            new GradientPaint(1f, 2f, Color.RED, 3f, 4f, Color.BLUE);
    private static final GradientPaint OUTLINE_PAINT =
            new GradientPaint(5f, 6f, Color.RED, 7f, 8f, Color.BLUE);
    private static final GradientPaint HIGHLIGHT_PAINT =
            new GradientPaint(9f, 0f, Color.RED, 1f, 2f, Color.BLUE);
    private static final Stroke OUTLINE_STROKE = new BasicStroke(1.23f);

    private static MeterNeedle newNeedle() {
        // LineNeedle is a simple concrete MeterNeedle suitable for equality tests
        return new LineNeedle();
    }

    private static void assertEqualAndHash(MeterNeedle a, MeterNeedle b) {
        assertEquals(a, b, "Objects should be equal");
        assertEquals(a.hashCode(), b.hashCode(), "Equal objects must have equal hash codes");
    }

    @Test
    public void equals_whenDefaults_thenEqual() {
        MeterNeedle left = newNeedle();
        MeterNeedle right = newNeedle();

        assertEqualAndHash(left, right);
    }

    @Test
    public void equals_differsOnFillPaint() {
        MeterNeedle left = newNeedle();
        MeterNeedle right = newNeedle();
        assertEqualAndHash(left, right);

        left.setFillPaint(FILL_PAINT);
        assertNotEquals(left, right, "Different fillPaint should make objects unequal");

        right.setFillPaint(FILL_PAINT);
        assertEqualAndHash(left, right);
    }

    @Test
    public void equals_differsOnOutlinePaint() {
        MeterNeedle left = newNeedle();
        MeterNeedle right = newNeedle();
        assertEqualAndHash(left, right);

        left.setOutlinePaint(OUTLINE_PAINT);
        assertNotEquals(left, right, "Different outlinePaint should make objects unequal");

        right.setOutlinePaint(OUTLINE_PAINT);
        assertEqualAndHash(left, right);
    }

    @Test
    public void equals_differsOnHighlightPaint() {
        MeterNeedle left = newNeedle();
        MeterNeedle right = newNeedle();
        assertEqualAndHash(left, right);

        left.setHighlightPaint(HIGHLIGHT_PAINT);
        assertNotEquals(left, right, "Different highlightPaint should make objects unequal");

        right.setHighlightPaint(HIGHLIGHT_PAINT);
        assertEqualAndHash(left, right);
    }

    @Test
    public void equals_differsOnOutlineStroke() {
        MeterNeedle left = newNeedle();
        MeterNeedle right = newNeedle();
        assertEqualAndHash(left, right);

        left.setOutlineStroke(OUTLINE_STROKE);
        assertNotEquals(left, right, "Different outlineStroke should make objects unequal");

        right.setOutlineStroke(OUTLINE_STROKE);
        assertEqualAndHash(left, right);
    }

    @Test
    public void equals_differsOnRotateX() {
        MeterNeedle left = newNeedle();
        MeterNeedle right = newNeedle();
        assertEqualAndHash(left, right);

        left.setRotateX(1.23);
        assertNotEquals(left, right, "Different rotateX should make objects unequal");

        right.setRotateX(1.23);
        assertEqualAndHash(left, right);
    }

    @Test
    public void equals_differsOnRotateY() {
        MeterNeedle left = newNeedle();
        MeterNeedle right = newNeedle();
        assertEqualAndHash(left, right);

        left.setRotateY(4.56);
        assertNotEquals(left, right, "Different rotateY should make objects unequal");

        right.setRotateY(4.56);
        assertEqualAndHash(left, right);
    }

    @Test
    public void equals_differsOnSize() {
        MeterNeedle left = newNeedle();
        MeterNeedle right = newNeedle();
        assertEqualAndHash(left, right);

        left.setSize(11);
        assertNotEquals(left, right, "Different size should make objects unequal");

        right.setSize(11);
        assertEqualAndHash(left, right);
    }
}