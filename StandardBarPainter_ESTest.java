/*
 * ======================================================
 * JFreeChart : a chart library for the Java(tm) platform
 * ======================================================
 *
 * (C) Copyright 2000-present, by David Gilbert and Contributors.
 *
 * This test suite is a refactored version of an auto-generated test case.
 * It focuses on improving readability and maintainability.
 */

package org.jfree.chart.renderer.category;

import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.junit.Before;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

/**
 * A set of tests for the {@link StandardBarPainter} class, focusing on clarity and correctness.
 */
public class StandardBarPainterTest {

    private StandardBarPainter painter;
    private BarRenderer renderer;
    private Rectangle2D.Double bar;

    @Before
    public void setUp() {
        painter = new StandardBarPainter();
        renderer = new BarRenderer();
        bar = new Rectangle2D.Double(10.0, 20.0, 30.0, 40.0);
    }

    /**
     * Creates a basic Graphics2D context for running paint tests.
     * @return A valid Graphics2D object.
     */
    private Graphics2D createTestGraphics() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        return image.createGraphics();
    }

    //region Equals and HashCode Tests

    @Test
    public void equals_sameInstance_returnsTrue() {
        assertTrue("A painter must be equal to itself.", painter.equals(painter));
    }

    @Test
    public void equals_twoSeparateInstances_returnsTrue() {
        // StandardBarPainter is stateless, so any two instances should be equal.
        StandardBarPainter anotherPainter = new StandardBarPainter();
        assertTrue("Two separate instances of StandardBarPainter should be equal.", painter.equals(anotherPainter));
    }

    @Test
    public void equals_withNull_returnsFalse() {
        assertFalse("A painter should not be equal to null.", painter.equals(null));
    }

    @Test
    public void equals_withDifferentObjectType_returnsFalse() {
        assertFalse("A painter should not be equal to an object of a different type.", painter.equals(new Object()));
    }

    @Test
    public void hashCode_isConsistentForEqualObjects() {
        StandardBarPainter anotherPainter = new StandardBarPainter();
        assertEquals("Hash codes should be the same for equal objects.", painter.hashCode(), anotherPainter.hashCode());
    }

    //endregion

    //region Exception Tests

    @Test(expected = NullPointerException.class)
    public void paintBar_withNullGraphics_throwsNullPointerException() {
        painter.paintBar(null, renderer, 0, 0, bar, RectangleEdge.BOTTOM);
    }

    @Test(expected = NullPointerException.class)
    public void paintBarShadow_withNullGraphics_throwsNullPointerException() {
        painter.paintBarShadow(null, renderer, 0, 0, bar, RectangleEdge.BOTTOM, true);
    }

    //endregion

    //region PaintBar Smoke Tests

    @Test
    public void paintBar_withStandardSettings_paintsWithoutError() {
        Graphics2D g2 = createTestGraphics();
        painter.paintBar(g2, renderer, 0, 0, bar, RectangleEdge.BOTTOM);
        // Test passes if no exception is thrown.
    }

    @Test
    public void paintBar_withBarOutline_paintsWithoutError() {
        Graphics2D g2 = createTestGraphics();
        renderer.setDrawBarOutline(true);
        painter.paintBar(g2, renderer, 0, 0, bar, RectangleEdge.LEFT);
        // Test passes if no exception is thrown.
    }

    @Test
    public void paintBar_withGradientPaint_paintsWithoutError() {
        Graphics2D g2 = createTestGraphics();
        renderer.setGradientPaintTransformer(new StandardGradientPaintTransformer(GradientPaintTransformType.CENTER_VERTICAL));
        painter.paintBar(g2, renderer, 0, 0, bar, RectangleEdge.TOP);
        // Test passes if no exception is thrown.
    }

    //endregion

    //region PaintBarShadow Smoke Tests

    @Test
    public void paintBarShadow_withBottomEdge_paintsWithoutError() {
        Graphics2D g2 = createTestGraphics();
        painter.paintBarShadow(g2, renderer, 0, 0, bar, RectangleEdge.BOTTOM, false);
        // Test passes if no exception is thrown.
    }

    @Test
    public void paintBarShadow_withTopEdge_paintsWithoutError() {
        Graphics2D g2 = createTestGraphics();
        painter.paintBarShadow(g2, renderer, 0, 0, bar, RectangleEdge.TOP, false);
        // Test passes if no exception is thrown.
    }

    @Test
    public void paintBarShadow_withLeftEdge_paintsWithoutError() {
        Graphics2D g2 = createTestGraphics();
        painter.paintBarShadow(g2, renderer, 0, 0, bar, RectangleEdge.LEFT, false);
        // Test passes if no exception is thrown.
    }

    @Test
    public void paintBarShadow_withRightEdge_paintsWithoutError() {
        Graphics2D g2 = createTestGraphics();
        painter.paintBarShadow(g2, renderer, 0, 0, bar, RectangleEdge.RIGHT, false);
        // Test passes if no exception is thrown.
    }

    @Test
    public void paintBarShadow_withPeggedShadow_paintsWithoutError() {
        Graphics2D g2 = createTestGraphics();
        // The 'pegShadow' parameter affects the shadow's position.
        painter.paintBarShadow(g2, renderer, 0, 0, bar, RectangleEdge.BOTTOM, true);
        // Test passes if no exception is thrown.
    }

    //endregion
}