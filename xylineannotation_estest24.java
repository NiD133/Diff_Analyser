package org.jfree.chart.annotations;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.image.BufferedImage;
import java.time.chrono.ChronoLocalDate;
import javax.swing.text.DefaultCaret;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CyclicNumberAxis;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.PeriodAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.CombinedRangeXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.chart.plot.WaferMapPlot;
import org.jfree.chart.plot.pie.PiePlot;
import org.jfree.chart.renderer.WaferMapRenderer;
import org.jfree.chart.renderer.xy.SamplingXYLineRenderer;
import org.jfree.data.time.Day;
import org.junit.runner.RunWith;

public class XYLineAnnotation_ESTestTest24 extends XYLineAnnotation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        XYPointerAnnotation xYPointerAnnotation0 = new XYPointerAnnotation("", (-349.265651518481), 2569.775065, (-349.265651518481));
        Stroke stroke0 = xYPointerAnnotation0.getOutlineStroke();
        XYLineAnnotation xYLineAnnotation0 = new XYLineAnnotation(4758.2062432043, (-1125.1681310373674), 2569.775065, 1350.0, stroke0, xYPointerAnnotation0.DEFAULT_PAINT);
        boolean boolean0 = xYLineAnnotation0.equals(xYPointerAnnotation0);
        assertFalse(boolean0);
        assertEquals((-1125.1681310373674), xYLineAnnotation0.getY1(), 0.01);
        assertEquals(2569.775065, xYLineAnnotation0.getX2(), 0.01);
        assertEquals(1350.0, xYLineAnnotation0.getY2(), 0.01);
        assertEquals(4758.2062432043, xYLineAnnotation0.getX1(), 0.01);
    }
}
