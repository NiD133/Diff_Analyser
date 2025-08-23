package org.jfree.chart.renderer.xy;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.text.DefaultCaret;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.util.GradientPaintTransformer;
import org.junit.runner.RunWith;

public class StandardXYBarPainter_ESTestTest3 extends StandardXYBarPainter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test02() throws Throwable {
        StandardXYBarPainter standardXYBarPainter0 = new StandardXYBarPainter();
        XYBarRenderer xYBarRenderer0 = new XYBarRenderer();
        RoundRectangle2D.Double roundRectangle2D_Double0 = new RoundRectangle2D.Double(5468.77884, 5468.77884, 4367, 0.0, (-2043.0), 1135);
        Rectangle2D rectangle2D0 = roundRectangle2D_Double0.getBounds2D();
        RectangleEdge rectangleEdge0 = RectangleEdge.BOTTOM;
        // Undeclared exception!
        try {
            standardXYBarPainter0.paintBarShadow((Graphics2D) null, xYBarRenderer0, 1135, 4367, rectangle2D0, rectangleEdge0, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }
}
