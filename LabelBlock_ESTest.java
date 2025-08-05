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
import org.jfree.chart.block.LabelBlock;
import org.jfree.chart.block.LengthConstraintType;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.block.Size2D;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.data.Range;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true)
public class LabelBlock_ESTest {

    @Test(timeout = 4000)
    public void testSetAndGetURLText() {
        LabelBlock labelBlock = new LabelBlock("F\"M5 ; wfEs");
        labelBlock.setURLText("F\"M5 ; wfEs");
        assertEquals("F\"M5 ; wfEs", labelBlock.getURLText());
    }

    @Test(timeout = 4000)
    public void testSetAndGetEmptyURLText() {
        LabelBlock labelBlock = new LabelBlock("");
        labelBlock.setURLText("");
        assertEquals("", labelBlock.getURLText());
    }

    @Test(timeout = 4000)
    public void testSetAndGetToolTipText() {
        LabelBlock labelBlock = new LabelBlock("/s");
        labelBlock.setToolTipText("mpAJ'YV3YX;OK");
        assertEquals("mpAJ'YV3YX;OK", labelBlock.getToolTipText());
    }

    @Test(timeout = 4000)
    public void testGetFontWithDerivedFont() {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        Font baseFont = styleContext.getFont("\"IAf&@`u", 1447, 1447);
        AffineTransform transform = AffineTransform.getRotateInstance(1447.0, 1447.0);
        Font derivedFont = baseFont.deriveFont(transform);
        LabelBlock labelBlock = new LabelBlock("\"IAf&@`u", derivedFont, Color.CYAN);
        assertSame(derivedFont, labelBlock.getFont());
    }

    @Test(timeout = 4000)
    public void testGetFontWithPlainFont() {
        Font font = new Font("~", Font.BOLD, 1);
        LabelBlock labelBlock = new LabelBlock("~", font);
        assertFalse(labelBlock.getFont().isTransformed());
    }

    @Test(timeout = 4000)
    public void testSetFontWithCustomFont() {
        LabelBlock labelBlock = new LabelBlock("5");
        Font font = new Font("(J*5t4K-JH}", -2963, -2963);
        Font derivedFont = font.deriveFont(-2963, 0.0F);
        labelBlock.setFont(derivedFont);
        assertFalse(labelBlock.getFont().hasUniformLineMetrics());
    }

    @Test(timeout = 4000)
    public void testGetFontWithStyleContext() {
        StyleContext styleContext = new StyleContext();
        Font font = styleContext.getFont("<iBHKz~", -250218615, -250218615);
        LabelBlock labelBlock = new LabelBlock("org.jfree.data.flow.DefaultFlowDataset", font, Color.ORANGE);
        assertSame(font, labelBlock.getFont());
    }

    @Test(timeout = 4000)
    public void testArrangeWithNoneConstraint() {
        LabelBlock labelBlock = new LabelBlock("{VWG-Lf]Z_E1vj+i'tt");
        BufferedImage bufferedImage = new BufferedImage(11, 1028, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        Size2D size = labelBlock.arrange(graphics, RectangleConstraint.NONE);
        assertEquals(13.0, size.getHeight(), 0.01);
    }

    @Test(timeout = 4000)
    public void testSetPaintWithNullThrowsException() {
        LabelBlock labelBlock = new LabelBlock("font");
        try {
            labelBlock.setPaint(null);
            fail("Expected IllegalArgumentException: Null 'paint' argument.");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'paint' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testSetFontWithNullThrowsException() {
        LabelBlock labelBlock = new LabelBlock(";8O?_[S");
        try {
            labelBlock.setFont(null);
            fail("Expected IllegalArgumentException: Null 'font' argument.");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'font' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testEqualsWithNullFontThrowsException() {
        LabelBlock labelBlock = new LabelBlock("");
        LabelBlock labelBlockWithNullFont = new LabelBlock("", (Font) null);
        try {
            labelBlock.equals(labelBlockWithNullFont);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected due to null font in one block
        }
    }

    @Test(timeout = 4000)
    public void testDrawWithNullGraphicsThrowsException() {
        LabelBlock labelBlock = new LabelBlock("FaFh7Y@oA>oU`EV9|Ws");
        Rectangle2D area = new Rectangle2D.Float();
        try {
            labelBlock.draw(null, area, area);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected due to null graphics context
        }
    }

    @Test(timeout = 4000)
    public void testDrawWithNullGraphics2ThrowsException() {
        LabelBlock labelBlock = new LabelBlock("Duplicate items in 'columnKeys'.");
        Rectangle2D area = new Rectangle2D.Float();
        try {
            labelBlock.draw(null, area);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected due to null graphics context
        }
    }

    @Test(timeout = 4000)
    public void testArrangeWithNullConstraintThrowsException() {
        LabelBlock labelBlock = new LabelBlock("EJ");
        try {
            labelBlock.arrange(null, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected due to null constraint
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullFontThrowsException() {
        try {
            new LabelBlock("The 'item' index is out of bounds.", null, Color.GREEN);
            fail("Expected IllegalArgumentException: Null 'font' argument.");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'font' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullFont2ThrowsException() {
        try {
            new LabelBlock("YpgCb8jaC&Q", (Font) null);
            fail("Expected IllegalArgumentException: Null 'font' argument.");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'font' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testConstructorWithNullTextThrowsException() {
        try {
            new LabelBlock(null);
            fail("Expected IllegalArgumentException: Null 'text' argument.");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'text' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testDrawReturnsNull() {
        LabelBlock labelBlock = new LabelBlock("9\"M5 ; _s");
        BufferedImage bufferedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = bufferedImage.createGraphics();
        Rectangle area = new Rectangle(new Point());
        assertNull(labelBlock.draw(graphics, area, area));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentContentAlignment() {
        LabelBlock block1 = new LabelBlock("Sg~6fAVv>P)");
        LabelBlock block2 = new LabelBlock("Sg~6fAVv>P)");
        block2.setContentAlignmentPoint(TextBlockAnchor.TOP_RIGHT);
        assertFalse(block1.equals(block2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentTooltip() {
        LabelBlock block1 = new LabelBlock("");
        LabelBlock block2 = new LabelBlock("");
        block2.setToolTipText("");
        assertFalse(block1.equals(block2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentPaint() {
        Font font = new Font(new Hashtable<>());
        LabelBlock block1 = new LabelBlock("SansSeri", font, Color.CYAN);
        LabelBlock block2 = new LabelBlock("SansSeri", font, Color.PINK);
        assertFalse(block1.equals(block2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithNullFontVsNonNull() {
        LabelBlock block1 = new LabelBlock("");
        LabelBlock block2 = new LabelBlock("", (Font) null);
        assertFalse(block1.equals(block2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentText() {
        Font font = Font.decode("F\"M5 ; ws");
        DefaultStyledDocument doc = new DefaultStyledDocument();
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        Color color = doc.getBackground(styleContext.new NamedStyle());
        LabelBlock block1 = new LabelBlock("F\"M5 ; ws", font, color);
        LabelBlock block2 = new LabelBlock("SansSerif", font, color);
        assertFalse(block1.equals(block2));
    }

    @Test(timeout = 4000)
    public void testEqualsWithNonLabelBlockObject() {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        Font font = styleContext.getFont("\"IAf&@`u", 1447, 1447);
        LabelBlock block = new LabelBlock("\"IAf&@`u", font, Color.CYAN);
        assertFalse(block.equals(Color.CYAN));
    }

    @Test(timeout = 4000)
    public void testEqualsReflexive() {
        LabelBlock block = new LabelBlock("");
        assertTrue(block.equals(block));
    }

    @Test(timeout = 4000)
    public void testGetDefaultContentAlignmentPoint() {
        LabelBlock block = new LabelBlock("");
        assertEquals(TextBlockAnchor.CENTER, block.getContentAlignmentPoint());
    }

    @Test(timeout = 4000)
    public void testGetDefaultToolTipTextIsNull() {
        LabelBlock block = new LabelBlock("Sg~6fAVv>P)");
        assertNull(block.getToolTipText());
    }

    @Test(timeout = 4000)
    public void testEqualsWithDifferentURLText() {
        LabelBlock block1 = new LabelBlock("");
        block1.setURLText("");
        LabelBlock block2 = new LabelBlock("");
        assertFalse(block2.equals(block1));
    }

    @Test(timeout = 4000)
    public void testSetTextAnchorChangesEquality() {
        LabelBlock block1 = new LabelBlock(" xof?pcOt?!");
        block1.setTextAnchor(RectangleAnchor.BOTTOM);
        LabelBlock block2 = new LabelBlock(" xof?pcOt?!");
        assertFalse(block1.equals(block2));
    }

    @Test(timeout = 4000)
    public void testSetContentAlignmentPointWithNullThrowsException() {
        LabelBlock block = new LabelBlock("");
        try {
            block.setContentAlignmentPoint(null);
            fail("Expected IllegalArgumentException: Null 'anchor' argument.");
        } catch (IllegalArgumentException e) {
            assertEquals("Null 'anchor' argument.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testGetFontWithNullInConstructor() {
        LabelBlock block = new LabelBlock("", (Font) null);
        assertNull(block.getFont());
    }

    @Test(timeout = 4000)
    public void testSetAndGetEmptyToolTipText() {
        LabelBlock block = new LabelBlock("");
        block.setToolTipText("");
        assertEquals("", block.getToolTipText());
    }

    @Test(timeout = 4000)
    public void testGetPaint() {
        StyleContext styleContext = StyleContext.getDefaultStyleContext();
        Font font = styleContext.getFont("\"IAf&@`u", 1447, 1447);
        LabelBlock block = new LabelBlock("\"IAf&@`u", font, Color.CYAN);
        assertEquals(Color.CYAN, block.getPaint());
    }

    @Test(timeout = 4000)
    public void testGetURLTextDefaultIsNull() {
        LabelBlock block = new LabelBlock("");
        assertNull(block.getURLText());
    }

    @Test(timeout = 4000)
    public void testGetDefaultTextAnchor() {
        LabelBlock block = new LabelBlock("");
        assertEquals(RectangleAnchor.CENTER, block.getTextAnchor());
    }

    @Test(timeout = 4000)
    public void testClone() throws CloneNotSupportedException {
        LabelBlock original = new LabelBlock("");
        LabelBlock clone = (LabelBlock) original.clone();
        assertEquals(0.0, clone.getContentXOffset(), 0.01);
    }

    @Test(timeout = 4000)
    public void testDrawWithMockGraphics() {
        LabelBlock block = new LabelBlock("");
        Graphics2D mockGraphics = mock(Graphics2D.class, new ViolatedAssumptionAnswer());
        block.draw(mockGraphics, new Rectangle2D.Float());
        assertNull(block.getID());
    }

    @Test(timeout = 4000)
    public void testArrangeWithFixedConstraint() {
        LabelBlock block = new LabelBlock("");
        Graphics2D mockGraphics = mock(Graphics2D.class, new ViolatedAssumptionAnswer());
        RectangleConstraint constraint = new RectangleConstraint(0.0F, null, 
            LengthConstraintType.FIXED, 3392.70533605612, null, LengthConstraintType.FIXED);
        Size2D size = block.arrange(mockGraphics, constraint);
        assertEquals(0.0, size.getHeight(), 0.01);
    }

    @Test(timeout = 4000)
    public void testDrawWithCustomPaint() {
        LabelBlock block = new LabelBlock("");
        block.setPaint(SystemColor.desktop);
        Graphics2D mockGraphics = mock(Graphics2D.class, new ViolatedAssumptionAnswer());
        block.draw(mockGraphics, new Rectangle2D.Float());
        assertEquals(0.0, block.getHeight(), 0.01);
    }
}