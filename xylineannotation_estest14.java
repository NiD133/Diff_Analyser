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

public class XYLineAnnotation_ESTestTest14 extends XYLineAnnotation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        SamplingXYLineRenderer samplingXYLineRenderer0 = new SamplingXYLineRenderer();
        XYLineAnnotation xYLineAnnotation0 = new XYLineAnnotation((double) samplingXYLineRenderer0.ZERO, (double) samplingXYLineRenderer0.ZERO, (-1423.7460088225328), (double) samplingXYLineRenderer0.ZERO, samplingXYLineRenderer0.DEFAULT_OUTLINE_STROKE, samplingXYLineRenderer0.DEFAULT_OUTLINE_PAINT);
        double double0 = xYLineAnnotation0.getX1();
        assertEquals((-1423.7460088225328), xYLineAnnotation0.getX2(), 0.01);
        assertEquals(0.0, double0, 0.01);
        assertEquals(0.0, xYLineAnnotation0.getY1(), 0.01);
        assertEquals(0.0, xYLineAnnotation0.getY2(), 0.01);
    }
}
