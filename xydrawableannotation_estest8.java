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

public class XYDrawableAnnotation_ESTestTest8 extends XYDrawableAnnotation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        BlockContainer blockContainer0 = new BlockContainer();
        XYDrawableAnnotation xYDrawableAnnotation0 = new XYDrawableAnnotation((-3258.2894942095554), (-3258.2894942095554), (-3258.2894942095554), (-3258.2894942095554), (-3258.2894942095554), blockContainer0);
        XYPlot<PolarAxisLocation> xYPlot0 = new XYPlot<PolarAxisLocation>();
        Rectangle2D.Float rectangle2D_Float0 = new Rectangle2D.Float(0.0F, 0.0F, 10, 1.0F);
        CyclicNumberAxis cyclicNumberAxis0 = new CyclicNumberAxis(0.0F);
        ChartRenderingInfo chartRenderingInfo0 = new ChartRenderingInfo();
        PlotRenderingInfo plotRenderingInfo0 = new PlotRenderingInfo(chartRenderingInfo0);
        BufferedImage bufferedImage0 = new BufferedImage(10, 10, 10);
        Graphics2D graphics2D0 = bufferedImage0.createGraphics();
        // Undeclared exception!
        try {
            xYDrawableAnnotation0.draw(graphics2D0, xYPlot0, rectangle2D_Float0, cyclicNumberAxis0, (ValueAxis) null, 10, plotRenderingInfo0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.annotations.XYDrawableAnnotation", e);
        }
    }
}
