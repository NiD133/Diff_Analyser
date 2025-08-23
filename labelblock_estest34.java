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

public class LabelBlock_ESTestTest34 extends LabelBlock_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test33() throws Throwable {
        LabelBlock labelBlock0 = new LabelBlock("");
        String string0 = labelBlock0.getURLText();
        assertNull(string0);
    }
}
