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

public class StandardBarPainter_ESTestTest5 extends StandardBarPainter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test04() throws Throwable {
        StandardBarPainter standardBarPainter0 = new StandardBarPainter();
        LayeredBarRenderer layeredBarRenderer0 = new LayeredBarRenderer();
        Ellipse2D.Double ellipse2D_Double0 = new Ellipse2D.Double((-2370.0918517736486), (-2370.0918517736486), 0.1, 2.0);
        RectangleEdge rectangleEdge0 = RectangleEdge.BOTTOM;
        // Undeclared exception!
        try {
            standardBarPainter0.paintBarShadow((Graphics2D) null, layeredBarRenderer0, 30, (-2371), ellipse2D_Double0, rectangleEdge0, false);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }
}
