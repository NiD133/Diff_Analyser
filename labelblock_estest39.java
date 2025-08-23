package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.SystemColor;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.util.Hashtable;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.StyleContext;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.ViolatedAssumptionAnswer;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.data.Range;
import org.junit.runner.RunWith;

public class LabelBlock_ESTestTest39 extends LabelBlock_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test38() throws Throwable {
        LabelBlock labelBlock0 = new LabelBlock("");
        SystemColor systemColor0 = SystemColor.desktop;
        labelBlock0.setPaint(systemColor0);
        Graphics2D graphics2D0 = mock(Graphics2D.class, new ViolatedAssumptionAnswer());
        Rectangle2D.Float rectangle2D_Float0 = new Rectangle2D.Float();
        labelBlock0.draw(graphics2D0, (Rectangle2D) rectangle2D_Float0);
        assertEquals(0.0, labelBlock0.getHeight(), 0.01);
    }
}
