package org.jfree.chart.renderer.category;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.image.BufferedImage;
import javax.swing.text.DefaultCaret;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.jfree.chart.api.RectangleEdge;
import org.jfree.chart.util.GradientPaintTransformer;
import org.junit.runner.RunWith;

public class StandardBarPainter_ESTestTest4 extends StandardBarPainter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        StandardBarPainter standardBarPainter0 = new StandardBarPainter();
        StackedBarRenderer stackedBarRenderer0 = new StackedBarRenderer(true);
        Rectangle2D.Float rectangle2D_Float0 = new Rectangle2D.Float(0.0F, 1.0F, 707.9548F, 1.0F);
        RectangleEdge rectangleEdge0 = RectangleEdge.BOTTOM;
        // Undeclared exception!
        try {
            standardBarPainter0.paintBarShadow((Graphics2D) null, stackedBarRenderer0, (-1233), (-1795), rectangle2D_Float0, rectangleEdge0, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }
}
