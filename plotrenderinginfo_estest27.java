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

public class PlotRenderingInfo_ESTestTest27 extends PlotRenderingInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test26() throws Throwable {
        Point2D.Double point2D_Double0 = new Point2D.Double(1994.172678354, (-1.0));
        Line2D.Float line2D_Float0 = new Line2D.Float(point2D_Double0, point2D_Double0);
        Rectangle2D rectangle2D0 = line2D_Float0.getBounds2D();
        ChartRenderingInfo chartRenderingInfo0 = new ChartRenderingInfo();
        PlotRenderingInfo plotRenderingInfo0 = chartRenderingInfo0.getPlotInfo();
        plotRenderingInfo0.setDataArea(rectangle2D0);
        Rectangle2D rectangle2D1 = plotRenderingInfo0.getDataArea();
        assertEquals((-1.0), rectangle2D1.getMaxY(), 0.01);
    }
}