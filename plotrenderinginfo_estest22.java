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

public class PlotRenderingInfo_ESTestTest22 extends PlotRenderingInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test21() throws Throwable {
        ChartRenderingInfo chartRenderingInfo0 = new ChartRenderingInfo();
        PlotRenderingInfo plotRenderingInfo0 = new PlotRenderingInfo(chartRenderingInfo0);
        Point point0 = new Point(7, 0);
        Dimension dimension0 = new Dimension();
        Rectangle rectangle0 = new Rectangle(point0, dimension0);
        plotRenderingInfo0.setDataArea(rectangle0);
        PlotRenderingInfo plotRenderingInfo1 = chartRenderingInfo0.getPlotInfo();
        boolean boolean0 = plotRenderingInfo0.equals(plotRenderingInfo1);
        assertFalse(boolean0);
    }
}
