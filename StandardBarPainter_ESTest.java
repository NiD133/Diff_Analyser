package org.jfree.chart.renderer.category;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.text.DefaultCaret;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.GanttRenderer;
import org.jfree.chart.renderer.category.GroupedStackedBarRenderer;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.renderer.category.WaterfallBarRenderer;
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class StandardBarPainter_ESTest extends StandardBarPainter_ESTest_scaffolding {

    // Test that a NullPointerException is thrown when Graphics2D is null in paintBarShadow
    @Test(timeout = 4000)
    public void testPaintBarShadowWithNullGraphics2D() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new BarRenderer();
        renderer.setShadowXOffset(0.0);
        Arc2D.Double arc = new Arc2D.Double(850.406, 0.0, 0.0, 0.0, -1349.214, 0.0, 0);
        RectangleEdge edge = RectangleEdge.LEFT;

        try {
            painter.paintBarShadow(null, renderer, 165, 165, arc, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }

    // Test that a NullPointerException is thrown when Graphics2D is null in paintBarShadow with different parameters
    @Test(timeout = 4000)
    public void testPaintBarShadowWithNullGraphics2DAndDifferentParams() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        BarRenderer renderer = new BarRenderer();
        Arc2D.Double arc = new Arc2D.Double(849.3502527866331, 0.26757577476475475, 0.26757577476475475, -1.003148633737342, -1349.214, 0.0, 0);
        RectangleEdge edge = RectangleEdge.LEFT;

        try {
            painter.paintBarShadow(null, renderer, 165, 165, arc, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }

    // Test that a NullPointerException is thrown when Graphics2D is null in paintBarShadow with GroupedStackedBarRenderer
    @Test(timeout = 4000)
    public void testPaintBarShadowWithGroupedStackedBarRenderer() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        Rectangle2D.Float rect = new Rectangle2D.Float();
        renderer.setShadowXOffset(-2491.15227);
        RectangleEdge edge = RectangleEdge.BOTTOM;

        try {
            painter.paintBarShadow(null, renderer, 37, 873, rect, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }

    // Test that a NullPointerException is thrown when Graphics2D is null in paintBarShadow with StackedBarRenderer
    @Test(timeout = 4000)
    public void testPaintBarShadowWithStackedBarRenderer() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        StackedBarRenderer renderer = new StackedBarRenderer(true);
        Rectangle2D.Float rect = new Rectangle2D.Float(0.0F, 1.0F, 707.9548F, 1.0F);
        RectangleEdge edge = RectangleEdge.BOTTOM;

        try {
            painter.paintBarShadow(null, renderer, -1233, -1795, rect, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }

    // Test that a NullPointerException is thrown when Graphics2D is null in paintBarShadow with LayeredBarRenderer
    @Test(timeout = 4000)
    public void testPaintBarShadowWithLayeredBarRenderer() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        LayeredBarRenderer renderer = new LayeredBarRenderer();
        Ellipse2D.Double ellipse = new Ellipse2D.Double(-2370.0918517736486, -2370.0918517736486, 0.1, 2.0);
        RectangleEdge edge = RectangleEdge.BOTTOM;

        try {
            painter.paintBarShadow(null, renderer, 30, -2371, ellipse, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }

    // Test that a NullPointerException is thrown when Graphics2D is null in paintBarShadow with different offsets
    @Test(timeout = 4000)
    public void testPaintBarShadowWithDifferentOffsets() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        Rectangle2D.Float rect = new Rectangle2D.Float();
        renderer.setShadowYOffset(1562);
        RectangleEdge edge = RectangleEdge.TOP;

        try {
            painter.paintBarShadow(null, renderer, 1562, 1562, rect, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }

    // Test that a NullPointerException is thrown when Graphics2D is null in paintBarShadow with WaterfallBarRenderer
    @Test(timeout = 4000)
    public void testPaintBarShadowWithWaterfallBarRenderer() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        RectangleEdge edge = RectangleEdge.TOP;
        WaterfallBarRenderer renderer = new WaterfallBarRenderer();
        Rectangle2D.Float rect = new Rectangle2D.Float(-2600, -2600, 707.9548F, -2600);

        try {
            painter.paintBarShadow(null, renderer, -2600, -2600, rect, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }

    // Test that paintBarShadow works with valid Graphics2D and StackedBarRenderer
    @Test(timeout = 4000)
    public void testPaintBarShadowWithValidGraphics2D() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        BufferedImage image = new BufferedImage(1, 599, 1);
        Graphics2D graphics = image.createGraphics();
        StackedBarRenderer renderer = new StackedBarRenderer();
        Rectangle2D.Double rect = new Rectangle2D.Double(1, renderer.ZERO, 0.0, 0.0);
        RectangleEdge edge = RectangleEdge.TOP;

        painter.paintBarShadow(graphics, renderer, 1, 255, rect, edge, true);
        assertTrue(renderer.getShadowsVisible());
    }

    // Test that a NullPointerException is thrown when Graphics2D is null in paintBar
    @Test(timeout = 4000)
    public void testPaintBarWithNullGraphics2D() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        StackedBarRenderer renderer = new StackedBarRenderer();
        RectangleEdge edge = RectangleEdge.BOTTOM;

        try {
            painter.paintBar(null, renderer, -1470, 1179, null, edge);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }

    // Test that two StandardBarPainter instances are equal
    @Test(timeout = 4000)
    public void testStandardBarPainterEquality() throws Throwable {
        StandardBarPainter painter1 = new StandardBarPainter();
        StandardBarPainter painter2 = new StandardBarPainter();
        assertTrue(painter1.equals(painter2));
    }

    // Test that a StandardBarPainter instance is equal to itself
    @Test(timeout = 4000)
    public void testStandardBarPainterSelfEquality() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        assertTrue(painter.equals(painter));
    }

    // Test that a StandardBarPainter instance is not equal to null
    @Test(timeout = 4000)
    public void testStandardBarPainterInequalityWithNull() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        assertFalse(painter.equals(null));
    }

    // Test that a NullPointerException is thrown when Graphics2D is null in paintBarShadow with GanttRenderer
    @Test(timeout = 4000)
    public void testPaintBarShadowWithGanttRenderer() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        GanttRenderer renderer = new GanttRenderer();
        DefaultCaret caret = new DefaultCaret();
        RectangleEdge edge = RectangleEdge.RIGHT;

        try {
            painter.paintBarShadow(null, renderer, 0, 0, caret, edge, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }

    // Test that a NullPointerException is thrown when Graphics2D is null in paintBarShadow with null RectangleEdge
    @Test(timeout = 4000)
    public void testPaintBarShadowWithNullRectangleEdge() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        Rectangle2D.Float rect = new Rectangle2D.Float();

        try {
            painter.paintBarShadow(null, renderer, 29, 29, rect, null, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }

    // Test that a NullPointerException is thrown when Graphics2D is null in paintBarShadow with GanttRenderer and false pegShadow
    @Test(timeout = 4000)
    public void testPaintBarShadowWithGanttRendererAndFalsePegShadow() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        GanttRenderer renderer = new GanttRenderer();
        DefaultCaret caret = new DefaultCaret();
        RectangleEdge edge = RectangleEdge.RIGHT;

        try {
            painter.paintBarShadow(null, renderer, 0, 0, caret, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }

    // Test that a NullPointerException is thrown when Graphics2D is null in paintBarShadow with GanttRenderer and RectangleEdge.BOTTOM
    @Test(timeout = 4000)
    public void testPaintBarShadowWithGanttRendererAndBottomEdge() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        GanttRenderer renderer = new GanttRenderer();
        DefaultCaret caret = new DefaultCaret();
        RectangleEdge edge = RectangleEdge.BOTTOM;

        try {
            painter.paintBarShadow(null, renderer, 0, 0, caret, edge, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }

    // Test that paintBar works with valid Graphics2D and GroupedStackedBarRenderer
    @Test(timeout = 4000)
    public void testPaintBarWithValidGraphics2D() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        BufferedImage image = new BufferedImage(1, 1, 1);
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        DefaultCaret caret = new DefaultCaret();
        renderer.setDrawBarOutline(true);
        Graphics2D graphics = image.createGraphics();
        RectangleEdge edge = RectangleEdge.LEFT;

        painter.paintBar(graphics, renderer, 0, 1, caret, edge);
        assertFalse(renderer.getDefaultItemLabelsVisible());
    }

    // Test that paintBar works with valid Graphics2D and GroupedStackedBarRenderer without drawing bar outline
    @Test(timeout = 4000)
    public void testPaintBarWithoutDrawingBarOutline() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        BufferedImage image = new BufferedImage(1, 1, 1);
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        DefaultCaret caret = new DefaultCaret();
        Graphics2D graphics = image.createGraphics();
        RectangleEdge edge = RectangleEdge.LEFT;

        painter.paintBar(graphics, renderer, 0, 1, caret, edge);
        assertFalse(renderer.getAutoPopulateSeriesOutlinePaint());
    }

    // Test that paintBar works with valid Graphics2D and GroupedStackedBarRenderer with GradientPaintTransformer
    @Test(timeout = 4000)
    public void testPaintBarWithGradientPaintTransformer() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        BufferedImage image = new BufferedImage(4, 4, 4);
        GroupedStackedBarRenderer renderer = new GroupedStackedBarRenderer();
        DefaultCaret caret = new DefaultCaret();
        Graphics2D graphics = image.createGraphics();
        renderer.setGradientPaintTransformer(null);
        RectangleEdge edge = RectangleEdge.TOP;

        painter.paintBar(graphics, renderer, 0, 0, caret, edge);
        assertEquals(0, renderer.getColumnCount());
    }

    // Test the hashCode method of StandardBarPainter
    @Test(timeout = 4000)
    public void testStandardBarPainterHashCode() throws Throwable {
        StandardBarPainter painter = new StandardBarPainter();
        painter.hashCode();
    }
}