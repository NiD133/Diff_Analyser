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

public class MeterNeedle_ESTestTest24 extends MeterNeedle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test23() throws Throwable {
        ShipNeedle shipNeedle0 = new ShipNeedle();
        PointerNeedle pointerNeedle0 = new PointerNeedle();
        Color color0 = Color.darkGray;
        pointerNeedle0.setHighlightPaint(color0);
        shipNeedle0.equals(pointerNeedle0);
        assertEquals(5, pointerNeedle0.getSize());
        assertEquals(0.5, pointerNeedle0.getRotateY(), 0.01);
        assertEquals(0.5, pointerNeedle0.getRotateX(), 0.01);
        assertEquals(0.5, shipNeedle0.getRotateX(), 0.01);
    }
}
