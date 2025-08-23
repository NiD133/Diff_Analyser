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

public class ChartRenderingInfo_ESTestTest7 extends ChartRenderingInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        ChartRenderingInfo chartRenderingInfo0 = new ChartRenderingInfo();
        Rectangle2D.Double rectangle2D_Double0 = new Rectangle2D.Double();
        rectangle2D_Double0.setFrameFromDiagonal(2626.9361169318868, 2868.898929, 0.0, 2868.898929);
        chartRenderingInfo0.setChartArea(rectangle2D_Double0);
        Rectangle2D rectangle2D0 = chartRenderingInfo0.getChartArea();
        assertEquals(0.0, rectangle2D0.getMinX(), 0.01);
    }
}
