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

public class MeterNeedle_ESTestTest37 extends MeterNeedle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test36() throws Throwable {
        LongNeedle longNeedle0 = new LongNeedle();
        BufferedImage bufferedImage0 = new BufferedImage(9, 9, 9);
        Graphics2D graphics2D0 = bufferedImage0.createGraphics();
        Rectangle2D.Float rectangle2D_Float0 = new Rectangle2D.Float(9, 9, 0.0F, 9);
        Rectangle rectangle0 = rectangle2D_Float0.getBounds();
        longNeedle0.draw(graphics2D0, (Rectangle2D) rectangle0, (Point2D) null, (double) 0.0F);
        assertEquals(0.5, longNeedle0.getRotateX(), 0.01);
        assertEquals(0.8, longNeedle0.getRotateY(), 0.01);
        assertEquals(5, longNeedle0.getSize());
    }
}