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

public class XYDrawableAnnotation_ESTestTest35 extends XYDrawableAnnotation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test34() throws Throwable {
        ShortTextTitle shortTextTitle0 = new ShortTextTitle("");
        XYDrawableAnnotation xYDrawableAnnotation0 = new XYDrawableAnnotation(0.0, 0.0, 0.0, (-1062.3), shortTextTitle0);
        double double0 = xYDrawableAnnotation0.getY();
        assertEquals((-1062.3), xYDrawableAnnotation0.getDisplayHeight(), 0.01);
        assertEquals(0.0, double0, 0.01);
        assertEquals(1.0, xYDrawableAnnotation0.getDrawScaleFactor(), 0.01);
        assertEquals(0.0, xYDrawableAnnotation0.getX(), 0.01);
        assertEquals(0.0, xYDrawableAnnotation0.getDisplayWidth(), 0.01);
    }
}
