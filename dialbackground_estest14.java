package org.jfree.chart.plot.dial;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.chart.plot.pie.PiePlot;
import org.jfree.chart.util.GradientPaintTransformType;
import org.jfree.chart.util.GradientPaintTransformer;
import org.jfree.chart.util.StandardGradientPaintTransformer;
import org.jfree.data.general.DefaultValueDataset;
import org.junit.runner.RunWith;

public class DialBackground_ESTestTest14 extends DialBackground_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test13() throws Throwable {
        DialBackground dialBackground0 = new DialBackground();
        Color color0 = Color.blue;
        GradientPaint gradientPaint0 = new GradientPaint((-66.0279F), (-66.0279F), color0, (-66.0279F), (-66.0279F), color0);
        dialBackground0.setPaint(gradientPaint0);
        DialPlot dialPlot0 = new DialPlot();
        Rectangle2D.Float rectangle2D_Float0 = new Rectangle2D.Float();
        // Undeclared exception!
        try {
            dialBackground0.draw((Graphics2D) null, dialPlot0, (Rectangle2D) null, rectangle2D_Float0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.plot.dial.DialBackground", e);
        }
    }
}
