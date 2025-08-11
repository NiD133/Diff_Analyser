package org.jfree.chart.plot.dial;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.chart.plot.pie.PiePlot;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.jfree.data.general.DefaultValueDataset;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class DialBackgroundTest extends DialBackground_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void testDrawDialBackground() throws Throwable {
        // Setup
        DialBackground dialBackground = new DialBackground();
        Rectangle2D.Double viewRectangle = new Rectangle2D.Double(-1620.96, -824.51, -3175.7, -3175.7);
        FastScatterPlot scatterPlot = new FastScatterPlot();
        JFreeChart chart = new JFreeChart("", scatterPlot);
        Rectangle frameRectangle = new Rectangle(10, 1);
        ChartRenderingInfo chartInfo = new ChartRenderingInfo((EntityCollection) null);
        BufferedImage image = chart.createBufferedImage(1, 91, chartInfo);
        Graphics2D graphics = image.createGraphics();
        DefaultValueDataset dataset = new DefaultValueDataset(0.5F);
        DialPlot dialPlot = new DialPlot(dataset);

        // Execute
        dialBackground.draw(graphics, dialPlot, frameRectangle, viewRectangle);

        // Verify
        assertEquals(5.0, frameRectangle.getCenterX(), 0.01);
    }

    @Test(timeout = 4000)
    public void testSetPaintWithNullThrowsException() throws Throwable {
        DialBackground dialBackground = new DialBackground();

        // Verify that setting a null paint throws an IllegalArgumentException
        try {
            dialBackground.setPaint(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testSetGradientPaintTransformerWithNullThrowsException() throws Throwable {
        DialBackground dialBackground = new DialBackground();

        // Verify that setting a null gradient paint transformer throws an IllegalArgumentException
        try {
            dialBackground.setGradientPaintTransformer(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullPaintThrowsException() throws Throwable {
        // Verify that constructing with a null paint throws an IllegalArgumentException
        try {
            new DialBackground(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testEqualsAndHashCode() throws Throwable {
        DialBackground dialBackground1 = new DialBackground();
        DialBackground dialBackground2 = new DialBackground();

        // Verify equality and hash code
        assertTrue(dialBackground1.equals(dialBackground2));
        assertEquals(dialBackground1.hashCode(), dialBackground2.hashCode());

        // Modify one object and verify inequality
        GradientPaintTransformType transformType = GradientPaintTransformType.CENTER_VERTICAL;
        StandardGradientPaintTransformer transformer = new StandardGradientPaintTransformer(transformType);
        dialBackground2.setGradientPaintTransformer(transformer);
        assertFalse(dialBackground1.equals(dialBackground2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentPaint() throws Throwable {
        DialBackground defaultBackground = new DialBackground();
        Color defaultLabelBackgroundPaint = (Color) PiePlot.DEFAULT_LABEL_BACKGROUND_PAINT;
        DialBackground customBackground = new DialBackground(defaultLabelBackgroundPaint);

        // Verify that two backgrounds with different paints are not equal
        assertFalse(defaultBackground.equals(customBackground));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentObjectType() throws Throwable {
        DialBackground dialBackground = new DialBackground();
        GradientPaintTransformType transformType = GradientPaintTransformType.CENTER_HORIZONTAL;
        StandardGradientPaintTransformer transformer = new StandardGradientPaintTransformer(transformType);

        // Verify that a DialBackground is not equal to a StandardGradientPaintTransformer
        assertFalse(dialBackground.equals(transformer));
    }

    @Test(timeout = 4000)
    public void testEqualsWithSameInstance() throws Throwable {
        DialBackground dialBackground = new DialBackground();

        // Verify that a DialBackground is equal to itself
        assertTrue(dialBackground.equals(dialBackground));
    }

    @Test(timeout = 4000)
    public void testClone() throws Throwable {
        DialBackground dialBackground = new DialBackground();
        Object clonedObject = dialBackground.clone();

        // Verify that the cloned object is not the same instance but is equal
        assertNotSame(clonedObject, dialBackground);
        assertEquals(clonedObject, dialBackground);
    }

    @Test(timeout = 4000)
    public void testDefaultGradientPaintTransformer() throws Throwable {
        DialBackground dialBackground = new DialBackground();
        StandardGradientPaintTransformer transformer = (StandardGradientPaintTransformer) dialBackground.getGradientPaintTransformer();

        // Verify the default gradient paint transformer type
        assertEquals(GradientPaintTransformType.VERTICAL, transformer.getType());
    }

    @Test(timeout = 4000)
    public void testIsClippedToWindow() throws Throwable {
        DialBackground dialBackground = new DialBackground();

        // Verify that the background is clipped to the window by default
        assertTrue(dialBackground.isClippedToWindow());
    }

    @Test(timeout = 4000)
    public void testGetDefaultPaint() throws Throwable {
        DialBackground dialBackground = new DialBackground();
        Color paintColor = (Color) dialBackground.getPaint();

        // Verify the default paint color
        assertEquals(Color.WHITE, paintColor);
    }
}