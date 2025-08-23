package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.SystemColor;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import javax.swing.JScrollPane;
import javax.swing.text.DefaultCaret;
import javax.swing.tree.DefaultTreeCellRenderer;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class MeterNeedle_ESTestTest16 extends MeterNeedle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test15() throws Throwable {
        Rectangle2D.Float rectangle2D_Float0 = new Rectangle2D.Float();
        MiddlePinNeedle middlePinNeedle0 = new MiddlePinNeedle();
        Point2D.Float point2D_Float0 = new Point2D.Float(0.0F, 0.0F);
        // Undeclared exception!
        try {
            middlePinNeedle0.draw((Graphics2D) null, (Rectangle2D) rectangle2D_Float0, (Point2D) point2D_Float0, (-1.0));
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.plot.compass.MeterNeedle", e);
        }
    }
}
