package org.jfree.chart;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Locale;
import java.util.SimpleTimeZone;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.jfree.chart.plot.CombinedRangeCategoryPlot;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.data.xy.XYDatasetTableModel;
import org.junit.runner.RunWith;

public class ChartRenderingInfo_ESTestTest19 extends ChartRenderingInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test18() throws Throwable {
        StandardEntityCollection standardEntityCollection0 = new StandardEntityCollection();
        ChartRenderingInfo chartRenderingInfo0 = new ChartRenderingInfo(standardEntityCollection0);
        PlotRenderingInfo plotRenderingInfo0 = chartRenderingInfo0.getPlotInfo();
        ChartRenderingInfo chartRenderingInfo1 = new ChartRenderingInfo();
        assertTrue(chartRenderingInfo1.equals((Object) chartRenderingInfo0));
        plotRenderingInfo0.addSubplotInfo(plotRenderingInfo0);
        boolean boolean0 = chartRenderingInfo0.equals(chartRenderingInfo1);
        assertFalse(chartRenderingInfo1.equals((Object) chartRenderingInfo0));
        assertFalse(boolean0);
    }
}
