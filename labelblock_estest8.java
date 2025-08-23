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

public class LabelBlock_ESTestTest8 extends LabelBlock_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test07() throws Throwable {
        LabelBlock labelBlock0 = new LabelBlock("{VWG-Lf]Z_E1vj+i'tt");
        BufferedImage bufferedImage0 = new BufferedImage(11, 1028, 11);
        Graphics2D graphics2D0 = bufferedImage0.createGraphics();
        RectangleConstraint rectangleConstraint0 = RectangleConstraint.NONE;
        Size2D size2D0 = labelBlock0.arrange(graphics2D0, rectangleConstraint0);
        assertEquals(13.0, size2D0.getHeight(), 0.01);
    }
}
