package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.text.DefaultCaret;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.renderer.xy.ClusteredXYBarRenderer;
import org.jfree.chart.renderer.xy.StackedXYBarRenderer;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.util.GradientPaintTransformer;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class StandardXYBarPainter_ESTest extends StandardXYBarPainter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testPaintBarShadowWithValidGraphics() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Arc2D.Float arc = new Arc2D.Float(1);
        BufferedImage image = new BufferedImage(1, 1, 1);
        Graphics2D graphics = image.createGraphics();
        ClusteredXYBarRenderer renderer = new ClusteredXYBarRenderer();
        renderer.setShadowYOffset(279.48324777286);
        RectangleEdge edge = RectangleEdge.LEFT;
        
        painter.paintBarShadow(graphics, renderer, 1, 1, arc, edge, false);
        
        assertTrue(renderer.getDefaultSeriesVisible());
    }

    @Test(timeout = 4000)
    public void testPaintBarShadowWithNullGraphics() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        AffineTransform transform = new AffineTransform();
        FontRenderContext context = new FontRenderContext(transform, false, false);
        Rectangle2D bounds = renderer.DEFAULT_VALUE_LABEL_FONT.getMaxCharBounds(context);
        RectangleEdge edge = RectangleEdge.LEFT;

        try {
            painter.paintBarShadow(null, renderer, 1469, 1361, bounds, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }

    @Test(timeout = 4000)
    public void testPaintBarShadowWithRoundRectangle() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        RoundRectangle2D.Double roundRect = new RoundRectangle2D.Double(5468.77884, 5468.77884, 4367, 0.0, -2043.0, 1135);
        Rectangle2D bounds = roundRect.getBounds2D();
        RectangleEdge edge = RectangleEdge.BOTTOM;

        try {
            painter.paintBarShadow(null, renderer, 1135, 4367, bounds, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }

    @Test(timeout = 4000)
    public void testPaintBarShadowWithRectangle2DFloat() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Rectangle2D.Float rect = new Rectangle2D.Float();
        XYBarRenderer renderer = new XYBarRenderer();
        RectangleEdge edge = RectangleEdge.BOTTOM;
        renderer.setShadowXOffset(0.0F);

        try {
            painter.paintBarShadow(null, renderer, -392, 1, rect, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }

    @Test(timeout = 4000)
    public void testPaintBarShadowWithAffineTransform() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        AffineTransform transform = AffineTransform.getQuadrantRotateInstance(0);
        FontRenderContext context = new FontRenderContext(transform, true, true);
        Rectangle2D bounds = renderer.DEFAULT_VALUE_LABEL_FONT.getMaxCharBounds(context);
        RectangleEdge edge = RectangleEdge.BOTTOM;

        try {
            painter.paintBarShadow(null, renderer, 37, 37, bounds, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }

    @Test(timeout = 4000)
    public void testPaintBarShadowWithTopEdge() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Rectangle2D.Float rect = new Rectangle2D.Float();
        XYBarRenderer renderer = new XYBarRenderer();
        RectangleEdge edge = RectangleEdge.TOP;
        renderer.setShadowYOffset(0.0);

        try {
            painter.paintBarShadow(null, renderer, 0, 0, rect, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }

    @Test(timeout = 4000)
    public void testPaintBarShadowWithFrameFromCenter() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Rectangle2D.Float rect = new Rectangle2D.Float();
        rect.setFrameFromCenter(1469, -84.5, 0.025, 1469);
        XYBarRenderer renderer = new XYBarRenderer();
        RectangleEdge edge = RectangleEdge.TOP;

        try {
            painter.paintBarShadow(null, renderer, 1469, 1469, rect, edge, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }

    @Test(timeout = 4000)
    public void testPaintBarWithStackedRenderer() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Arc2D.Float arc = new Arc2D.Float(1);
        BufferedImage image = new BufferedImage(1, 1, 1);
        Graphics2D graphics = image.createGraphics();
        StackedXYBarRenderer renderer = new StackedXYBarRenderer();
        renderer.setDrawBarOutline(true);
        RectangleEdge edge = RectangleEdge.TOP;

        painter.paintBar(graphics, renderer, -753, 255, arc, edge);

        assertEquals(0.0F, arc.start, 0.01F);
    }

    @Test(timeout = 4000)
    public void testPaintBarWithZeroWidth() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Arc2D.Float arc = new Arc2D.Float(1);
        BufferedImage image = new BufferedImage(37, 1, 1);
        Graphics2D graphics = image.createGraphics();
        StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.0F);
        RectangleEdge edge = RectangleEdge.TOP;

        painter.paintBar(graphics, renderer, 1970, 0, arc, edge);

        assertEquals(0.0F, arc.width, 0.01F);
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameInstance() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        assertTrue(painter.equals(painter));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentInstance() throws Throwable {
        StandardXYBarPainter painter1 = new StandardXYBarPainter();
        StandardXYBarPainter painter2 = new StandardXYBarPainter();
        assertTrue(painter1.equals(painter2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentType() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Arc2D.Float arc = new Arc2D.Float(1);
        assertFalse(painter.equals(arc));
    }

    @Test(timeout = 4000)
    public void testPaintBarShadowWithRectangle() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle rect = new Rectangle();
        RectangleEdge edge = RectangleEdge.RIGHT;

        try {
            painter.paintBarShadow(null, renderer, 0, 0, rect, edge, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }

    @Test(timeout = 4000)
    public void testPaintBarShadowWithNullEdge() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        BufferedImage image = new BufferedImage(37, 1, 1);
        Graphics2D graphics = image.createGraphics();
        StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.0F);
        Rectangle2D.Double rect = new Rectangle2D.Double();

        painter.paintBarShadow(graphics, renderer, 1886, -37, rect, null, false);

        assertEquals(0.0, renderer.getBase(), 0.01);
    }

    @Test(timeout = 4000)
    public void testPaintBarShadowWithLeftEdge() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        Rectangle rect = new Rectangle();
        RectangleEdge edge = RectangleEdge.LEFT;

        try {
            painter.paintBarShadow(null, renderer, 0, 0, rect, edge, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }

    @Test(timeout = 4000)
    public void testPaintBarShadowWithRightEdge() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        Arc2D.Float arc = new Arc2D.Float(1);
        BufferedImage image = new BufferedImage(1, 1, 1);
        Graphics2D graphics = image.createGraphics();
        StackedXYBarRenderer renderer = new StackedXYBarRenderer(0.0F);
        RectangleEdge edge = RectangleEdge.RIGHT;

        painter.paintBarShadow(graphics, renderer, 0, 0, arc, edge, false);

        assertTrue(renderer.getShadowsVisible());
    }

    @Test(timeout = 4000)
    public void testPaintBarShadowWithDefaultCaret() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        XYBarRenderer renderer = new XYBarRenderer();
        RectangleEdge edge = RectangleEdge.BOTTOM;
        DefaultCaret caret = new DefaultCaret();

        try {
            painter.paintBarShadow(null, renderer, 0, 0, caret, edge, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }

    @Test(timeout = 4000)
    public void testPaintBarShadowWithStackedRenderer() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        StackedXYBarRenderer renderer = new StackedXYBarRenderer();
        Rectangle rect = new Rectangle();
        RectangleEdge edge = RectangleEdge.TOP;

        try {
            painter.paintBarShadow(null, renderer, 0, 0, rect, edge, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }

    @Test(timeout = 4000)
    public void testPaintBarWithNullGraphics() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        StackedXYBarRenderer renderer = new StackedXYBarRenderer();
        Rectangle rect = new Rectangle();
        renderer.setGradientPaintTransformer(null);
        RectangleEdge edge = RectangleEdge.RIGHT;

        try {
            painter.paintBar(null, renderer, 0, 0, rect, edge);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }

    @Test(timeout = 4000)
    public void testHashCode() throws Throwable {
        StandardXYBarPainter painter = new StandardXYBarPainter();
        painter.hashCode();
    }
}