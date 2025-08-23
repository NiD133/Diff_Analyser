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

public class LabelBlock_ESTestTest13 extends LabelBlock_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test12() throws Throwable {
        Rectangle2D.Float rectangle2D_Float0 = new Rectangle2D.Float();
        LabelBlock labelBlock0 = new LabelBlock("Duplicate items in 'columnKeys'.");
        // Undeclared exception!
        try {
            labelBlock0.draw((Graphics2D) null, (Rectangle2D) rectangle2D_Float0);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            //
            // no message in exception (getMessage() returned null)
            //
            verifyException("org.jfree.chart.block.BlockBorder", e);
        }
    }
}
