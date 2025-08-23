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
import org.jfree.chart.util.GradientPaintTransformer;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.jfree.data.general.DefaultValueDataset;
import org.junit.runner.RunWith;

public class DialBackground_ESTestTest1 extends DialBackground_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test00() throws Throwable {
        DialBackground dialBackground0 = new DialBackground();
        Rectangle2D.Double rectangle2D_Double0 = new Rectangle2D.Double((-1620.962466736), (-824.5089), (-3175.7), (-3175.7));
        FastScatterPlot fastScatterPlot0 = new FastScatterPlot();
        JFreeChart jFreeChart0 = new JFreeChart("", fastScatterPlot0);
        Rectangle rectangle0 = new Rectangle(10, 1);
        ChartRenderingInfo chartRenderingInfo0 = new ChartRenderingInfo((EntityCollection) null);
        BufferedImage bufferedImage0 = jFreeChart0.createBufferedImage(1, 91, chartRenderingInfo0);
        Graphics2D graphics2D0 = bufferedImage0.createGraphics();
        DefaultValueDataset defaultValueDataset0 = new DefaultValueDataset((Number) 0.5F);
        DialPlot dialPlot0 = new DialPlot(defaultValueDataset0);
        dialBackground0.draw(graphics2D0, dialPlot0, rectangle0, rectangle2D_Double0);
        assertEquals(5.0, rectangle0.getCenterX(), 0.01);
    }
}
