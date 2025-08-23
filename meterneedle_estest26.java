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

public class MeterNeedle_ESTestTest26 extends MeterNeedle_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test25() throws Throwable {
        MiddlePinNeedle middlePinNeedle0 = new MiddlePinNeedle();
        MiddlePinNeedle middlePinNeedle1 = (MiddlePinNeedle) middlePinNeedle0.clone();
        assertTrue(middlePinNeedle1.equals((Object) middlePinNeedle0));
        BasicStroke basicStroke0 = new BasicStroke();
        middlePinNeedle1.setOutlineStroke(basicStroke0);
        boolean boolean0 = middlePinNeedle0.equals(middlePinNeedle1);
        assertFalse(middlePinNeedle1.equals((Object) middlePinNeedle0));
        assertFalse(boolean0);
    }
}
