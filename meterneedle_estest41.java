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

public class MeterNeedle_ESTestTest41 extends MeterNeedle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test40() throws Throwable {
        PinNeedle pinNeedle0 = new PinNeedle();
        BufferedImage bufferedImage0 = new BufferedImage(1, 1, 1);
        Graphics2D graphics2D0 = bufferedImage0.createGraphics();
        JScrollPane jScrollPane0 = new JScrollPane();
        Rectangle rectangle0 = jScrollPane0.getViewportBorderBounds();
        pinNeedle0.draw(graphics2D0, (Rectangle2D) rectangle0);
        assertEquals(0.5, pinNeedle0.getRotateX(), 0.01);
        assertEquals(0.5, pinNeedle0.getRotateY(), 0.01);
        assertEquals(5, pinNeedle0.getSize());
    }
}
