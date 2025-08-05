package org.jfree.chart.renderer.xy;

import org.jfree.chart.api.RectangleEdge;
import org.junit.Before;
import org.junit.Test;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import static org.junit.Assert.*;

/**
 * Unit tests for the {@link StandardXYBarPainter} class.
 */
public class StandardXYBarPainterTest {

    private StandardXYBarPainter painter;
    private Graphics2D graphics;
    private BufferedImage image;

    @Before
    public void setUp() {
        painter = new StandardXYBarPainter();
        // Create a dummy graphics context for rendering tests that don't need to verify output
        image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
    }

    //region Equals and HashCode Tests

    @Test
    public void equals_shouldAdhereToContract() {
        StandardXYBarPainter painter1 = new StandardXYBarPainter();
        StandardXYBarPainter painter2 = new StandardXYBarPainter();

        // Reflexive: an object must equal itself
        assertEquals(painter1, painter1);

        // Symmetric: if a.equals(b) is true, then b.equals(a) must be true
        assertEquals(painter1, painter2);
        assertEquals(painter2, painter1);

        // Inequality with different object types and null
        assertNotEquals(painter1, null);
        assertNotEquals(painter1, new Object());
    }

    @Test
    public void hashCode_shouldBeConsistentForEqualObjects() {
        StandardXYBarPainter painter1 = new StandardXYBarPainter();
        StandardXYBarPainter painter2 = new StandardXYBarPainter();

        assertEquals("Equal objects must have equal hash codes", painter1.hashCode(), painter2.hashCode());
    }

    //endregion

    //region paintBar() Tests

    /**
     * The paintBar method should throw a NullPointerException if the graphics context is null.
     */
    @Test(expected = NullPointerException.class)
    public void paintBar_withNullGraphics_shouldThrowNPE() {
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D.Double bar = new Rectangle2D.Double(10, 20, 30, 40);
        painter.paintBar(null, renderer, 0, 0, bar, RectangleEdge.BOTTOM);
    }

    /**
     * Verifies that the painter can render a bar with an outline enabled without error.
     */
    @Test
    public void paintBar_withOutlineEnabled_shouldPaintWithoutError() {
        StackedXYBarRenderer renderer = new StackedXYBarRenderer();
        renderer.setDrawBarOutline(true);
        Rectangle2D.Double bar = new Rectangle2D.Double(10, 20, 30, 40);

        // The test passes if no exception is thrown
        painter.paintBar(graphics, renderer, 0, 0, bar, RectangleEdge.TOP);
    }

    /**
     * Verifies that the painter can render a bar for all possible base edge positions without error.
     */
    @Test
    public void paintBar_withAllRectangleEdges_shouldPaintWithoutError() {
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D.Double bar = new Rectangle2D.Double(10, 20, 30, 40);

        // The test passes if no exceptions are thrown for any edge
        painter.paintBar(graphics, renderer, 0, 0, bar, RectangleEdge.TOP);
        painter.paintBar(graphics, renderer, 0, 0, bar, RectangleEdge.BOTTOM);
        painter.paintBar(graphics, renderer, 0, 0, bar, RectangleEdge.LEFT);
        painter.paintBar(graphics, renderer, 0, 0, bar, RectangleEdge.RIGHT);
    }

    //endregion

    //region paintBarShadow() Tests

    /**
     * The paintBarShadow method should throw a NullPointerException if the graphics context is null.
     */
    @Test(expected = NullPointerException.class)
    public void paintBarShadow_withNullGraphics_shouldThrowNPE() {
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D.Double bar = new Rectangle2D.Double(10, 20, 30, 40);
        painter.paintBarShadow(null, renderer, 0, 0, bar, RectangleEdge.BOTTOM, false);
    }

    /**
     * Verifies that the painter can render a bar's shadow for all possible base edge positions without error.
     */
    @Test
    public void paintBarShadow_withAllRectangleEdges_shouldPaintWithoutError() {
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        renderer.setShadowVisible(true);
        Rectangle2D.Double bar = new Rectangle2D.Double(10, 20, 30, 40);

        // The test passes if no exceptions are thrown for any edge
        painter.paintBarShadow(graphics, renderer, 0, 0, bar, RectangleEdge.TOP, false);
        painter.paintBarShadow(graphics, renderer, 0, 0, bar, RectangleEdge.BOTTOM, false);
        painter.paintBarShadow(graphics, renderer, 0, 0, bar, RectangleEdge.LEFT, false);
        painter.paintBarShadow(graphics, renderer, 0, 0, bar, RectangleEdge.RIGHT, false);
    }

    /**
     * Checks that the painter handles a null RectangleEdge gracefully without throwing an exception.
     * This is an edge case derived from the original test suite.
     */
    @Test
    public void paintBarShadow_withNullRectangleEdge_shouldPaintWithoutError() {
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D.Double bar = new Rectangle2D.Double(10, 20, 30, 40);

        // The test passes if no exception is thrown
        painter.paintBarShadow(graphics, renderer, 0, 0, bar, null, false);
    }

    /**
     * Verifies that a "pegged" shadow (one attached to the bar's base) can be rendered without error.
     */
    @Test
    public void paintBarShadow_withPeggedShadow_shouldPaintWithoutError() {
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle2D.Double bar = new Rectangle2D.Double(10, 20, 30, 40);

        // The test passes if no exception is thrown
        painter.paintBarShadow(graphics, renderer, 0, 0, bar, RectangleEdge.BOTTOM, true);
    }

    //endregion
}