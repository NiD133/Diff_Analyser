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

public class MeterNeedle_ESTestTest9 extends MeterNeedle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test08() throws Throwable {
        LongNeedle longNeedle0 = new LongNeedle();
        longNeedle0.setRotateY((-590.9257035));
        double double0 = longNeedle0.getRotateY();
        assertEquals((-590.9257035), double0, 0.01);
    }
}