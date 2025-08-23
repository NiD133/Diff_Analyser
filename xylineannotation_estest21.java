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

public class XYLineAnnotation_ESTestTest21 extends XYLineAnnotation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test20() throws Throwable {
        XYLineAnnotation xYLineAnnotation0 = new XYLineAnnotation((-1433.6156858015083), (-1433.6156858015083), (-1433.6156858015083), (-1433.6156858015083));
        CyclicNumberAxis cyclicNumberAxis0 = new CyclicNumberAxis(135.0);
        Stroke stroke0 = cyclicNumberAxis0.getAdvanceLineStroke();
        XYLineAnnotation xYLineAnnotation1 = new XYLineAnnotation((-1433.6156858015083), (-1433.6156858015083), 1505.073663087376, 2995.807896, stroke0, cyclicNumberAxis0.DEFAULT_TICK_LABEL_PAINT);
        boolean boolean0 = xYLineAnnotation0.equals(xYLineAnnotation1);
        assertEquals(2995.807896, xYLineAnnotation1.getY2(), 0.01);
        assertEquals((-1433.6156858015083), xYLineAnnotation1.getX1(), 0.01);
        assertFalse(boolean0);
        assertEquals((-1433.6156858015083), xYLineAnnotation1.getY1(), 0.01);
        assertEquals(1505.073663087376, xYLineAnnotation1.getX2(), 0.01);
    }
}
