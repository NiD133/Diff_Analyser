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

public class DeviationRenderer_ESTestTest18 extends DeviationRenderer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test17() throws Throwable {
        DeviationRenderer deviationRenderer0 = new DeviationRenderer();
        Rectangle2D.Double rectangle2D_Double0 = new Rectangle2D.Double((double) deviationRenderer0.ZERO, 90.0, 3048.34637057, 0.0);
        XYPlot<CategoryAnchor> xYPlot0 = new XYPlot<CategoryAnchor>();
        SimpleTimeZone simpleTimeZone0 = new SimpleTimeZone(10, "w@^qxE!G 6g");
        TimeSeriesCollection<CategoryAnchor> timeSeriesCollection0 = new TimeSeriesCollection<CategoryAnchor>(simpleTimeZone0);
        ChartRenderingInfo chartRenderingInfo0 = new ChartRenderingInfo();
        PlotRenderingInfo plotRenderingInfo0 = new PlotRenderingInfo(chartRenderingInfo0);
        XYItemRendererState xYItemRendererState0 = deviationRenderer0.initialise((Graphics2D) null, rectangle2D_Double0, xYPlot0, timeSeriesCollection0, plotRenderingInfo0);
        assertTrue(deviationRenderer0.getDrawSeriesLineAsPath());
        assertEquals(0.5F, deviationRenderer0.getAlpha(), 0.01F);
        assertFalse(xYItemRendererState0.getProcessVisibleItemsOnly());
    }
}
