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

public class DialBackground_ESTestTest5 extends DialBackground_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        DialBackground dialBackground0 = new DialBackground();
        DialBackground dialBackground1 = new DialBackground();
        assertTrue(dialBackground1.equals((Object) dialBackground0));
        GradientPaintTransformType gradientPaintTransformType0 = GradientPaintTransformType.CENTER_VERTICAL;
        StandardGradientPaintTransformer standardGradientPaintTransformer0 = new StandardGradientPaintTransformer(gradientPaintTransformType0);
        dialBackground1.setGradientPaintTransformer(standardGradientPaintTransformer0);
        boolean boolean0 = dialBackground0.equals(dialBackground1);
        assertFalse(dialBackground1.equals((Object) dialBackground0));
        assertFalse(boolean0);
    }
}
