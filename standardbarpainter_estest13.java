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

public class StandardBarPainter_ESTestTest13 extends StandardBarPainter_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        StandardBarPainter standardBarPainter0 = new StandardBarPainter();
        GanttRenderer ganttRenderer0 = new GanttRenderer();
        DefaultCaret defaultCaret0 = new DefaultCaret();
        RectangleEdge rectangleEdge0 = RectangleEdge.RIGHT;
        // Undeclared exception!
        try {
            standardBarPainter0.paintBarShadow((Graphics2D) null, ganttRenderer0, 0, 0, defaultCaret0, rectangleEdge0, true);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.renderer.category.StandardBarPainter", e);
        }
    }
}
