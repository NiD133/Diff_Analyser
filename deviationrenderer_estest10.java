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

public class DeviationRenderer_ESTestTest10 extends DeviationRenderer_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        DeviationRenderer deviationRenderer0 = new DeviationRenderer(true, true);
        boolean boolean0 = deviationRenderer0.equals(deviationRenderer0);
        assertEquals(0.5F, deviationRenderer0.getAlpha(), 0.01F);
        assertTrue(deviationRenderer0.getDrawSeriesLineAsPath());
        assertTrue(boolean0);
    }
}
