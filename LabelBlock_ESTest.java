package org.jfree.chart.block;

import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;
import static org.evosuite.shaded.org.mockito.Mockito.*;
import static org.evosuite.runtime.EvoAssertions.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
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
import org.jfree.chart.block.LabelBlock;
import org.jfree.chart.block.LengthConstraintType;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.block.Size2D;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.data.Range;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class LabelBlock_ESTest extends LabelBlock_ESTest_scaffolding {

    // Test setting and getting URL text
    @Test(timeout = 4000)
    public void testSetAndGetURLText() throws Throwable {
        LabelBlock labelBlock = new LabelBlock("Sample Text");
        labelBlock.setURLText("Sample URL");
        assertEquals("Sample URL", labelBlock.getURLText());
    }

    // Test setting and getting empty URL text
    @Test(timeout = 4000)
    public void testSetAndGetEmptyURLText() throws Throwable {
        LabelBlock labelBlock = new LabelBlock("");
        labelBlock.setURLText("");
        assertEquals("", labelBlock.getURLText());
    }

    // Test setting and getting tooltip text
    @Test(timeout = 4000)
    public void testSetAndGetToolTipText() throws Throwable {
        LabelBlock labelBlock = new LabelBlock("Sample");
        labelBlock.setToolTipText("Tooltip Text");
        assertEquals("Tooltip Text", labelBlock.getToolTipText());
    }

    // Test font transformation
    @Test(timeout = 4000)
    public void testFontTransformation() throws Throwable {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        Font originalFont = styleContext.getFont("FontName", Font.PLAIN, 12);
        AffineTransform transform = AffineTransform.getRotateInstance(Math.PI / 4);
        Font transformedFont = originalFont.deriveFont(transform);
        LabelBlock labelBlock = new LabelBlock("Text", transformedFont, Color.CYAN);
        assertSame(transformedFont, labelBlock.getFont());
    }

    // Test font without transformation
    @Test(timeout = 4000)
    public void testFontWithoutTransformation() throws Throwable {
        Font font = new Font("Arial", Font.PLAIN, 12);
        LabelBlock labelBlock = new LabelBlock("Text", font);
        assertFalse(labelBlock.getFont().isTransformed());
    }

    // Test setting a font with negative size
    @Test(timeout = 4000)
    public void testSetFontWithNegativeSize() throws Throwable {
        LabelBlock labelBlock = new LabelBlock("Text");
        Font font = new Font("Arial", Font.PLAIN, -10);
        labelBlock.setFont(font);
        assertFalse(labelBlock.getFont().hasUniformLineMetrics());
    }

    // Test arranging label block with graphics and constraints
    @Test(timeout = 4000)
    public void testArrangeLabelBlock() throws Throwable {
        LabelBlock labelBlock = new LabelBlock("Sample Text");
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        RectangleConstraint constraint = RectangleConstraint.NONE;
        Size2D size = labelBlock.arrange(graphics, constraint);
        assertEquals(13.0, size.getHeight(), 0.01);
    }

    // Test setting null paint throws exception
    @Test(timeout = 4000)
    public void testSetNullPaintThrowsException() throws Throwable {
        LabelBlock labelBlock = new LabelBlock("Text");
        try {
            labelBlock.setPaint(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    // Test setting null font throws exception
    @Test(timeout = 4000)
    public void testSetNullFontThrowsException() throws Throwable {
        LabelBlock labelBlock = new LabelBlock("Text");
        try {
            labelBlock.setFont(null);
            fail("Expecting exception: IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            verifyException("org.jfree.chart.internal.Args", e);
        }
    }

    // Test drawing with null graphics throws exception
    @Test(timeout = 4000)
    public void testDrawWithNullGraphicsThrowsException() throws Throwable {
        Rectangle2D.Float rectangle = new Rectangle2D.Float();
        LabelBlock labelBlock = new LabelBlock("Text");
        try {
            labelBlock.draw(null, rectangle);
            fail("Expecting exception: NullPointerException");
        } catch (NullPointerException e) {
            verifyException("org.jfree.chart.block.BlockBorder", e);
        }
    }

    // Test clone method
    @Test(timeout = 4000)
    public void testClone() throws Throwable {
        LabelBlock labelBlock = new LabelBlock("Text");
        LabelBlock clonedBlock = (LabelBlock) labelBlock.clone();
        assertEquals(labelBlock, clonedBlock);
    }

    // Test equals method with different text
    @Test(timeout = 4000)
    public void testEqualsWithDifferentText() throws Throwable {
        LabelBlock labelBlock1 = new LabelBlock("Text1");
        LabelBlock labelBlock2 = new LabelBlock("Text2");
        assertFalse(labelBlock1.equals(labelBlock2));
    }

    // Test equals method with same text
    @Test(timeout = 4000)
    public void testEqualsWithSameText() throws Throwable {
        LabelBlock labelBlock1 = new LabelBlock("Text");
        LabelBlock labelBlock2 = new LabelBlock("Text");
        assertTrue(labelBlock1.equals(labelBlock2));
    }

    // Test setting and getting content alignment point
    @Test(timeout = 4000)
    public void testSetAndGetContentAlignmentPoint() throws Throwable {
        LabelBlock labelBlock = new LabelBlock("Text");
        labelBlock.setContentAlignmentPoint(TextBlockAnchor.TOP_LEFT);
        assertEquals(TextBlockAnchor.TOP_LEFT, labelBlock.getContentAlignmentPoint());
    }

    // Test setting and getting text anchor
    @Test(timeout = 4000)
    public void testSetAndGetTextAnchor() throws Throwable {
        LabelBlock labelBlock = new LabelBlock("Text");
        labelBlock.setTextAnchor(RectangleAnchor.BOTTOM_LEFT);
        assertEquals(RectangleAnchor.BOTTOM_LEFT, labelBlock.getTextAnchor());
    }

    // Test drawing with valid graphics and rectangle
    @Test(timeout = 4000)
    public void testDrawWithValidGraphicsAndRectangle() throws Throwable {
        LabelBlock labelBlock = new LabelBlock("Text");
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        Rectangle2D rectangle = new Rectangle2D.Double(0, 0, 50, 50);
        labelBlock.draw(graphics, rectangle);
        assertNull(labelBlock.getID());
    }
}