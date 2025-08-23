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

public class LabelBlock_ESTestTest4 extends LabelBlock_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test03() throws Throwable {
        StyleContext styleContext0 = StyleContext.getDefaultStyleContext();
        Font font0 = styleContext0.getFont("\"IAf&@`u", 1447, 1447);
        AffineTransform affineTransform0 = AffineTransform.getRotateInstance((double) 1447, (double) 1447);
        Font font1 = font0.deriveFont(affineTransform0);
        Color color0 = Color.cyan;
        LabelBlock labelBlock0 = new LabelBlock("\"IAf&@`u", font1, color0);
        Font font2 = labelBlock0.getFont();
        assertSame(font1, font2);
    }
}
