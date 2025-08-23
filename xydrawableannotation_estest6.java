package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.math.BigInteger;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.Drawable;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CyclicNumberAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.legend.LegendTitle;
import org.jfree.chart.plot.MeterPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.PolarAxisLocation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.category.ScatterRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.title.CompositeTitle;
import org.jfree.chart.title.DateTitle;
import org.jfree.chart.title.ShortTextTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultValueDataset;
import org.junit.runner.RunWith;

public class XYDrawableAnnotation_ESTestTest6 extends XYDrawableAnnotation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        BlockContainer blockContainer0 = new BlockContainer();
        XYDrawableAnnotation xYDrawableAnnotation0 = new XYDrawableAnnotation((-2655.096265552367), (-2655.096265552367), (-2655.096265552367), (-2655.096265552367), (-2655.096265552367), blockContainer0);
        XYDrawableAnnotation xYDrawableAnnotation1 = new XYDrawableAnnotation(0.0F, 0.0F, 0.0F, 0.0F, blockContainer0);
        boolean boolean0 = xYDrawableAnnotation0.equals(xYDrawableAnnotation1);
        assertEquals(1.0, xYDrawableAnnotation1.getDrawScaleFactor(), 0.01);
        assertEquals(0.0, xYDrawableAnnotation1.getDisplayHeight(), 0.01);
        assertFalse(boolean0);
        assertEquals(0.0, xYDrawableAnnotation1.getDisplayWidth(), 0.01);
        assertEquals(0.0, xYDrawableAnnotation1.getY(), 0.01);
        assertEquals(0.0, xYDrawableAnnotation1.getX(), 0.01);
    }
}
