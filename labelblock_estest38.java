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

public class LabelBlock_ESTestTest38 extends LabelBlock_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test37() throws Throwable {
        LabelBlock labelBlock0 = new LabelBlock("");
        Graphics2D graphics2D0 = mock(Graphics2D.class, new ViolatedAssumptionAnswer());
        LengthConstraintType lengthConstraintType0 = LengthConstraintType.FIXED;
        RectangleConstraint rectangleConstraint0 = new RectangleConstraint(0.0F, (Range) null, lengthConstraintType0, 3392.70533605612, (Range) null, lengthConstraintType0);
        Size2D size2D0 = labelBlock0.arrange(graphics2D0, rectangleConstraint0);
        assertEquals(0.0, size2D0.getHeight(), 0.01);
    }
}
