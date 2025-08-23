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

public class PlotRenderingInfo_ESTestTest10 extends PlotRenderingInfo_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test09() throws Throwable {
        PlotRenderingInfo plotRenderingInfo0 = new PlotRenderingInfo((ChartRenderingInfo) null);
        Rectangle2D.Double rectangle2D_Double0 = new Rectangle2D.Double((-452.97088092026), (-452.97088092026), (-452.97088092026), (-452.97088092026));
        plotRenderingInfo0.setDataArea(rectangle2D_Double0);
        Rectangle2D.Double rectangle2D_Double1 = (Rectangle2D.Double) plotRenderingInfo0.getDataArea();
        assertEquals((-452.97088092026), rectangle2D_Double1.height, 0.01);
    }
}
