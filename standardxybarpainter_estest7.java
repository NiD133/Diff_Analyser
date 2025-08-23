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

public class StandardXYBarPainter_ESTestTest7 extends StandardXYBarPainter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        StandardXYBarPainter standardXYBarPainter0 = new StandardXYBarPainter();
        Rectangle2D.Float rectangle2D_Float0 = new Rectangle2D.Float();
        rectangle2D_Float0.setFrameFromCenter((double) 1469, (-84.5), 0.025, (double) 1469);
        XYBarRenderer xYBarRenderer0 = new XYBarRenderer();
        RectangleEdge rectangleEdge0 = RectangleEdge.TOP;
        // Undeclared exception!
        try {
            standardXYBarPainter0.paintBarShadow((Graphics2D) null, xYBarRenderer0, 1469, 1469, rectangle2D_Float0, rectangleEdge0, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }
}
