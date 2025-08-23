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

public class XYLineAnnotation_ESTestTest6 extends XYLineAnnotation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        CombinedRangeXYPlot<ChronoLocalDate> combinedRangeXYPlot0 = new CombinedRangeXYPlot<ChronoLocalDate>();
        DefaultCaret defaultCaret0 = new DefaultCaret();
        PlotOrientation plotOrientation0 = PlotOrientation.HORIZONTAL;
        combinedRangeXYPlot0.setOrientation(plotOrientation0);
        DateAxis dateAxis0 = new DateAxis();
        XYLineAnnotation xYLineAnnotation0 = new XYLineAnnotation(10, 10.0, 5193.5984, (-1711.1007449), dateAxis0.DEFAULT_AXIS_LINE_STROKE, dateAxis0.DEFAULT_TICK_LABEL_PAINT);
        CyclicNumberAxis cyclicNumberAxis0 = new CyclicNumberAxis(972.9942880626711, 138.7250706690693, (String) null);
        // Undeclared exception!
        try {
            xYLineAnnotation0.draw((Graphics2D) null, combinedRangeXYPlot0, defaultCaret0, cyclicNumberAxis0, dateAxis0, 0, (PlotRenderingInfo) null);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.annotations.XYLineAnnotation", e);
        }
    }
}
