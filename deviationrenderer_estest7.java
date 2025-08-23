package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.axis.CategoryAnchor;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.DatasetRenderingOrder;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.DefaultHighLowDataset;
import org.jfree.data.xy.DefaultIntervalXYDataset;
import org.jfree.data.xy.MatrixSeries;
import org.jfree.data.xy.MatrixSeriesCollection;
import org.junit.runner.RunWith;

public class DeviationRenderer_ESTestTest7 extends DeviationRenderer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        DeviationRenderer deviationRenderer0 = new DeviationRenderer();
        Date[] dateArray0 = new Date[3];
        TimeZone timeZone0 = TimeZone.getDefault();
        Locale locale0 = Locale.PRC;
        DateAxis dateAxis0 = new DateAxis("", timeZone0, locale0);
        dateArray0[0] = dateAxis0.DEFAULT_ANCHOR_DATE;
        dateArray0[1] = dateAxis0.DEFAULT_ANCHOR_DATE;
        dateArray0[2] = dateArray0[1];
        double[] doubleArray0 = new double[5];
        doubleArray0[1] = (-1.0);
        DefaultHighLowDataset defaultHighLowDataset0 = new DefaultHighLowDataset(deviationRenderer0.ZERO, dateArray0, doubleArray0, doubleArray0, doubleArray0, doubleArray0, doubleArray0);
        deviationRenderer0.findRangeBounds(defaultHighLowDataset0);
        assertTrue(deviationRenderer0.getDrawSeriesLineAsPath());
        assertEquals(0.5F, deviationRenderer0.getAlpha(), 0.01F);
    }
}
