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

public class MeterNeedle_ESTestTest17 extends MeterNeedle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test16() throws Throwable {
        PointerNeedle pointerNeedle0 = new PointerNeedle();
        Rectangle2D.Float rectangle2D_Float0 = new Rectangle2D.Float();
        // Undeclared exception!
        try {
            pointerNeedle0.draw((Graphics2D) null, (Rectangle2D) rectangle2D_Float0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.plot.compass.MeterNeedle", e);
        }
    }
}
