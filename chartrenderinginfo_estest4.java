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

public class ChartRenderingInfo_ESTestTest4 extends ChartRenderingInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        ChartRenderingInfo chartRenderingInfo0 = new ChartRenderingInfo();
        SimpleTimeZone simpleTimeZone0 = new SimpleTimeZone(1, ",:Q.N1exKdJ$");
        Locale locale0 = Locale.JAPANESE;
        DateAxis dateAxis0 = new DateAxis(",:Q.N1exKdJ$", simpleTimeZone0, locale0);
        CombinedRangeCategoryPlot combinedRangeCategoryPlot0 = new CombinedRangeCategoryPlot(dateAxis0);
        JFreeChart jFreeChart0 = new JFreeChart(combinedRangeCategoryPlot0);
        jFreeChart0.createBufferedImage(10, 10, 1749.95538, (double) 2.0F, chartRenderingInfo0);
        EntityCollection entityCollection0 = chartRenderingInfo0.getEntityCollection();
        assertEquals(3, entityCollection0.getEntityCount());
    }
}
