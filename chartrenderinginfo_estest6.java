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

public class ChartRenderingInfo_ESTestTest6 extends ChartRenderingInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test05() throws Throwable {
        ChartRenderingInfo chartRenderingInfo0 = new ChartRenderingInfo();
        Rectangle rectangle0 = new Rectangle((-521), (-782), 0, (-2457));
        chartRenderingInfo0.setChartArea(rectangle0);
        Rectangle2D rectangle2D0 = chartRenderingInfo0.getChartArea();
        assertEquals((-2010.5), rectangle2D0.getCenterY(), 0.01);
    }
}
