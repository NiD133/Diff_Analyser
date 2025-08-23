package org.jfree.chart.plot;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.entity.StandardEntityCollection;
import org.junit.runner.RunWith;

public class PlotRenderingInfo_ESTestTest3 extends PlotRenderingInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        ChartRenderingInfo chartRenderingInfo0 = new ChartRenderingInfo();
        PlotRenderingInfo plotRenderingInfo0 = chartRenderingInfo0.getPlotInfo();
        PlotRenderingInfo plotRenderingInfo1 = new PlotRenderingInfo(chartRenderingInfo0);
        plotRenderingInfo0.addSubplotInfo(plotRenderingInfo1);
        PlotRenderingInfo plotRenderingInfo2 = plotRenderingInfo0.getSubplotInfo(0);
        assertFalse(plotRenderingInfo2.equals((Object) plotRenderingInfo0));
    }
}
