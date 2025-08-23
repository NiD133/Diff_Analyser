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

public class XYLineAnnotation_ESTestTest7 extends XYLineAnnotation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        CyclicNumberAxis cyclicNumberAxis0 = new CyclicNumberAxis(0.0, (-1.0));
        CombinedRangeXYPlot<ChronoLocalDate> combinedRangeXYPlot0 = new CombinedRangeXYPlot<ChronoLocalDate>(cyclicNumberAxis0);
        Stroke stroke0 = combinedRangeXYPlot0.getRangeGridlineStroke();
        XYLineAnnotation xYLineAnnotation0 = new XYLineAnnotation((-496.0), 1036.4647693410504, 1036.4647693410504, (-1.0), stroke0, cyclicNumberAxis0.DEFAULT_TICK_MARK_PAINT);
        Polygon polygon0 = new Polygon();
        Rectangle rectangle0 = polygon0.getBounds();
        Day day0 = new Day();
        PeriodAxis periodAxis0 = new PeriodAxis("", day0, day0);
        // Undeclared exception!
        try {
            xYLineAnnotation0.draw((Graphics2D) null, combinedRangeXYPlot0, rectangle0, periodAxis0, cyclicNumberAxis0, 0, (PlotRenderingInfo) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.annotations.XYLineAnnotation", e);
        }
    }
}
