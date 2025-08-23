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

public class PlotRenderingInfo_ESTestTest2 extends PlotRenderingInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        ChartRenderingInfo chartRenderingInfo0 = new ChartRenderingInfo();
        PlotRenderingInfo plotRenderingInfo0 = chartRenderingInfo0.getPlotInfo();
        Point point0 = new Point(4047, 4047);
        Line2D.Float line2D_Float0 = new Line2D.Float(point0, point0);
        Rectangle2D rectangle2D0 = line2D_Float0.getBounds2D();
        plotRenderingInfo0.setPlotArea(rectangle2D0);
        plotRenderingInfo0.hashCode();
    }
}
