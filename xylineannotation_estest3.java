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

public class XYLineAnnotation_ESTestTest3 extends XYLineAnnotation_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        CombinedRangeXYPlot<ChronoLocalDate> combinedRangeXYPlot0 = new CombinedRangeXYPlot<ChronoLocalDate>();
        XYLineAnnotation xYLineAnnotation0 = new XYLineAnnotation(10, 1.0F, 1.0F, 10, combinedRangeXYPlot0.DEFAULT_OUTLINE_STROKE, combinedRangeXYPlot0.DEFAULT_CROSSHAIR_PAINT);
        Stroke stroke0 = combinedRangeXYPlot0.getDomainMinorGridlineStroke();
        XYLineAnnotation xYLineAnnotation1 = new XYLineAnnotation(10, 1.0F, (-14.36318469711), 180.0, stroke0, combinedRangeXYPlot0.DEFAULT_OUTLINE_PAINT);
        boolean boolean0 = xYLineAnnotation0.equals(xYLineAnnotation1);
        assertEquals(180.0, xYLineAnnotation1.getY2(), 0.01);
        assertEquals(10.0, xYLineAnnotation1.getX1(), 0.01);
        assertFalse(boolean0);
        assertEquals(1.0, xYLineAnnotation1.getY1(), 0.01);
        assertEquals((-14.36318469711), xYLineAnnotation1.getX2(), 0.01);
    }
}
