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

public class LabelBlock_ESTestTest7 extends LabelBlock_ESTest_scaffolding {

    @Test(timeout = 4000)
    public void test06() throws Throwable {
        StyleContext styleContext0 = new StyleContext();
        Font font0 = styleContext0.getFont("<iBHKz~", (-250218615), (-250218615));
        Color color0 = Color.ORANGE;
        LabelBlock labelBlock0 = new LabelBlock("org.jfree.data.flow.DefaultFlowDataset", font0, color0);
        Font font1 = labelBlock0.getFont();
        assertSame(font1, font0);
    }
}
