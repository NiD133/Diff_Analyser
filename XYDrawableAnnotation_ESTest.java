package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.annotations.XYDrawableAnnotation;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.legend.LegendTitle;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.ScatterRenderer;
import org.jfree.chart.title.TextTitle;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class XYDrawableAnnotationTest {

    private static final double DELTA = 0.01;

    @Test(timeout = 4000)
    public void testHashCodeConsistency() {
        TextTitle title = new TextTitle("(Sq");
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(-2648.3, -2648.3, -1549.15850542798, 2.0, title);
        annotation.hashCode();
        assertEquals(-2648.3, annotation.getX(), DELTA);
        assertEquals(-2648.3, annotation.getY(), DELTA);
        assertEquals(-1549.15850542798, annotation.getDisplayWidth(), DELTA);
        assertEquals(2.0, annotation.getDisplayHeight(), DELTA);
        assertEquals(1.0, annotation.getDrawScaleFactor(), DELTA);
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentDrawScaleFactor() {
        BlockContainer container = new BlockContainer();
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(-2674.765041096609, -2674.765041096609, 2.0, -2674.765041096609, container);
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(-2674.765041096609, -2674.765041096609, 2.0, -2674.765041096609, -888.093, container);
        assertFalse(annotation1.equals(annotation2));
        assertEquals(-888.093, annotation2.getDrawScaleFactor(), DELTA);
    }

    @Test(timeout = 4000)
    public void testEqualityWithDifferentDisplayHeight() {
        TextTitle title = new TextTitle("");
        XYDrawableAnnotation annotation1 = new XYDrawableAnnotation(0.0, 0.0, 0.0, -1062.3, title);
        ScatterRenderer renderer = new ScatterRenderer();
        LegendTitle legend = new LegendTitle(renderer);
        XYDrawableAnnotation annotation2 = new XYDrawableAnnotation(0.0, 0.0, 0.0, 26.840400407963966, 26.840400407963966, legend);
        assertFalse(annotation1.equals(annotation2));
    }

    @Test(timeout = 4000)
    public void testDrawMethodWithNullRangeAxis() {
        BlockContainer container = new BlockContainer();
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(-3258.2894942095554, -3258.2894942095554, -3258.2894942095554, -3258.2894942095554, -3258.2894942095554, container);
        XYPlot plot = new XYPlot();
        Rectangle2D.Float rectangle = new Rectangle2D.Float(0.0F, 0.0F, 10, 1.0F);
        NumberAxis axis = new NumberAxis(0.0F);
        ChartRenderingInfo chartInfo = new ChartRenderingInfo();
        PlotRenderingInfo plotInfo = new PlotRenderingInfo(chartInfo);
        BufferedImage image = new BufferedImage(10, 10, 10);
        Graphics2D graphics = image.createGraphics();
        try {
            annotation.draw(graphics, plot, rectangle, axis, null, 10, plotInfo);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.annotations.XYDrawableAnnotation", e);
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullDrawable() {
        try {
            new XYDrawableAnnotation(738.4170062070756, 0.0F, 0.0F, 489.8514, null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    @Test(timeout = 4000)
    public void testCloneMethod() throws CloneNotSupportedException {
        TextTitle title = new TextTitle("x");
        XYDrawableAnnotation annotation = new XYDrawableAnnotation(738.4170062070756, 738.4170062070756, 738.4170062070756, 738.4170062070756, title);
        XYDrawableAnnotation clonedAnnotation = (XYDrawableAnnotation) annotation.clone();
        assertTrue(annotation.equals(clonedAnnotation));
    }

    // Additional test cases can be added here following the same pattern
}