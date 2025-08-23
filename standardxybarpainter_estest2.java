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

public class StandardXYBarPainter_ESTestTest2 extends StandardXYBarPainter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test01() throws Throwable {
        StandardXYBarPainter standardXYBarPainter0 = new StandardXYBarPainter();
        XYBarRenderer xYBarRenderer0 = new XYBarRenderer();
        AffineTransform affineTransform0 = new AffineTransform();
        FontRenderContext fontRenderContext0 = new FontRenderContext(affineTransform0, false, false);
        Rectangle2D rectangle2D0 = xYBarRenderer0.DEFAULT_VALUE_LABEL_FONT.getMaxCharBounds(fontRenderContext0);
        RectangleEdge rectangleEdge0 = RectangleEdge.LEFT;
        // Undeclared exception!
        try {
            standardXYBarPainter0.paintBarShadow((Graphics2D) null, xYBarRenderer0, 1469, 1361, rectangle2D0, rectangleEdge0, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.renderer.xy.StandardXYBarPainter", e);
        }
    }
}
