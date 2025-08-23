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

public class ChartRenderingInfo_ESTestTest5 extends ChartRenderingInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        ChartRenderingInfo chartRenderingInfo0 = new ChartRenderingInfo((EntityCollection) null);
        Rectangle rectangle0 = new Rectangle(1, 294, (-343), 7);
        chartRenderingInfo0.setChartArea(rectangle0);
        Rectangle2D.Double rectangle2D_Double0 = (Rectangle2D.Double) chartRenderingInfo0.getChartArea();
        assertEquals(294.0, rectangle2D_Double0.y, 0.01);
    }
}
