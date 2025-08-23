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

public class XYDrawableAnnotation_ESTestTest7 extends XYDrawableAnnotation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        BlockContainer blockContainer0 = new BlockContainer();
        XYDrawableAnnotation xYDrawableAnnotation0 = new XYDrawableAnnotation((-2655.096265552367), (-2655.096265552367), (-2655.096265552367), (-2655.096265552367), (-2655.096265552367), blockContainer0);
        XYPlot<PolarAxisLocation> xYPlot0 = new XYPlot<PolarAxisLocation>();
        RoundRectangle2D.Float roundRectangle2D_Float0 = new RoundRectangle2D.Float(0.0F, 0.0F, 2561.058F, 0.0F, 1.0F, 0.0F);
        Rectangle2D rectangle2D0 = roundRectangle2D_Float0.getBounds2D();
        NumberAxis numberAxis0 = new NumberAxis("KpTx6U: [");
        ChartRenderingInfo chartRenderingInfo0 = new ChartRenderingInfo();
        PlotRenderingInfo plotRenderingInfo0 = chartRenderingInfo0.getPlotInfo();
        JFreeChart jFreeChart0 = new JFreeChart(xYPlot0);
        BufferedImage bufferedImage0 = jFreeChart0.createBufferedImage(10, 500, (-2562.43728), 3.0, chartRenderingInfo0);
        Graphics2D graphics2D0 = bufferedImage0.createGraphics();
        xYDrawableAnnotation0.draw(graphics2D0, xYPlot0, rectangle2D0, numberAxis0, numberAxis0, 10, plotRenderingInfo0);
        assertEquals((-2655.096265552367), xYDrawableAnnotation0.getY(), 0.01);
        assertEquals((-2655.096265552367), xYDrawableAnnotation0.getDrawScaleFactor(), 0.01);
        assertEquals((-2655.096265552367), xYDrawableAnnotation0.getDisplayHeight(), 0.01);
    }
}
