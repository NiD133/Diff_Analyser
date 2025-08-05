package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.data.Range;

/**
 * A collection of well-structured and understandable unit tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    private static final Font TEST_FONT = new Font("SansSerif", Font.PLAIN, 12);
    private static final Paint TEST_PAINT = Color.BLUE;

    /**
     * Helper to get a Graphics2D instance for rendering tests, avoiding mocks.
     */
    private Graphics2D createTestGraphics2D() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
        return image.createGraphics();
    }

    // --- Constructor Tests ---

    @Test
    public void constructorWithTextOnlyShouldUseDefaultFontAndPaint() {
        // Act
        LabelBlock block = new LabelBlock("Test");

        // Assert
        assertEquals(new Font("SansSerif", Font.PLAIN, 10), block.getFont());
        assertEquals(Color.BLACK, block.getPaint());
        assertNull(block.getToolTipText());
        assertNull(block.getURLText());
    }

    @Test
    public void constructorWithTextAndFontShouldUseDefaultPaint() {
        // Act
        LabelBlock block = new LabelBlock("Test", TEST_FONT);

        // Assert
        assertEquals(TEST_FONT, block.getFont());
        assertEquals(Color.BLACK, block.getPaint());
    }

    @Test
    public void constructorWithAllArgumentsShouldSetPropertiesCorrectly() {
        // Act
        LabelBlock block = new LabelBlock("Test", TEST_FONT, TEST_PAINT);

        // Assert
        assertEquals(TEST_FONT, block.getFont());
        assertEquals(TEST_PAINT, block.getPaint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionForNullText() {
        new LabelBlock(null, TEST_FONT, TEST_PAINT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionForNullFont() {
        new LabelBlock("Test", null, TEST_PAINT);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorShouldThrowExceptionForNullPaint() {
        new LabelBlock("Test", TEST_FONT, null);
    }

    // --- Property Accessor Tests ---

    @Test
    public void setFontShouldUpdateFont() {
        // Arrange
        LabelBlock block = new LabelBlock("Test");
        Font newFont = new Font("Dialog", Font.BOLD, 16);

        // Act
        block.setFont(newFont);

        // Assert
        assertEquals(newFont, block.getFont());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setFontShouldThrowExceptionForNullArgument() {
        LabelBlock block = new LabelBlock("Test");
        block.setFont(null);
    }

    @Test
    public void setPaintShouldUpdatePaint() {
        // Arrange
        LabelBlock block = new LabelBlock("Test");
        Paint newPaint = Color.GREEN;

        // Act
        block.setPaint(newPaint);

        // Assert
        assertEquals(newPaint, block.getPaint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setPaintShouldThrowExceptionForNullArgument() {
        LabelBlock block = new LabelBlock("Test");
        block.setPaint(null);
    }

    @Test
    public void getAndSetToolTipText() {
        // Arrange
        LabelBlock block = new LabelBlock("Test");
        assertNull("Default tooltip text should be null", block.getToolTipText());

        // Act & Assert for setting a value
        block.setToolTipText("Tooltip");
        assertEquals("Tooltip", block.getToolTipText());

        // Act & Assert for setting back to null
        block.setToolTipText(null);
        assertNull("Tooltip text should be settable to null", block.getToolTipText());
    }

    @Test
    public void getAndSetURLText() {
        // Arrange
        LabelBlock block = new LabelBlock("Test");
        assertNull("Default URL text should be null", block.getURLText());

        // Act & Assert for setting a value
        block.setURLText("http://example.com");
        assertEquals("http://example.com", block.getURLText());

        // Act & Assert for setting back to null
        block.setURLText(null);
        assertNull("URL text should be settable to null", block.getURLText());
    }
    
    // --- Equality, Hashing and Cloning ---

    @Test
    public void equalsShouldReturnTrueForSameInstance() {
        LabelBlock block = new LabelBlock("Test");
        assertTrue(block.equals(block));
    }

    @Test
    public void equalsShouldReturnTrueForIdenticalBlocks() {
        LabelBlock block1 = new LabelBlock("Test", TEST_FONT, TEST_PAINT);
        LabelBlock block2 = new LabelBlock("Test", TEST_FONT, TEST_PAINT);
        assertTrue(block1.equals(block2));
    }

    @Test
    public void equalsShouldReturnFalseForDifferentText() {
        LabelBlock block1 = new LabelBlock("Test1", TEST_FONT, TEST_PAINT);
        LabelBlock block2 = new LabelBlock("Test2", TEST_FONT, TEST_PAINT);
        assertFalse(block1.equals(block2));
    }

    @Test
    public void equalsShouldReturnFalseForDifferentFont() {
        Font font1 = new Font("SansSerif", Font.PLAIN, 10);
        Font font2 = new Font("SansSerif", Font.PLAIN, 12);
        LabelBlock block1 = new LabelBlock("Test", font1);
        LabelBlock block2 = new LabelBlock("Test", font2);
        assertFalse(block1.equals(block2));
    }

    @Test
    public void equalsShouldReturnFalseForDifferentPaint() {
        LabelBlock block1 = new LabelBlock("Test", TEST_FONT, Color.RED);
        LabelBlock block2 = new LabelBlock("Test", TEST_FONT, Color.BLUE);
        assertFalse(block1.equals(block2));
    }

    @Test
    public void equalsShouldReturnFalseForDifferentToolTip() {
        LabelBlock block1 = new LabelBlock("Test");
        block1.setToolTipText("Tip 1");
        LabelBlock block2 = new LabelBlock("Test");
        block2.setToolTipText("Tip 2");
        assertFalse(block1.equals(block2));
    }

    @Test
    public void hashCodeShouldBeConsistentWithEquals() {
        LabelBlock block1 = new LabelBlock("Test", TEST_FONT, TEST_PAINT);
        LabelBlock block2 = new LabelBlock("Test", TEST_FONT, TEST_PAINT);
        assertEquals(block1.hashCode(), block2.hashCode());
    }

    @Test
    public void cloneShouldProduceEqualButNotSameInstance() throws CloneNotSupportedException {
        // Arrange
        LabelBlock original = new LabelBlock("Test", TEST_FONT, TEST_PAINT);
        original.setToolTipText("Tooltip");
        
        // Act
        LabelBlock clone = (LabelBlock) original.clone();
        
        // Assert
        assertNotSame("Clone should be a different instance", original, clone);
        assertEquals("Clone should be equal to the original", original, clone);
    }

    // --- Core Functionality: arrange() and draw() ---

    @Test
    public void arrangeShouldCalculateSizeForNonEmptyText() {
        // Arrange
        LabelBlock block = new LabelBlock("Hello World", TEST_FONT);
        Graphics2D g2 = createTestGraphics2D();
        RectangleConstraint constraint = RectangleConstraint.NONE;

        // Act
        Size2D size = block.arrange(g2, constraint);

        // Assert
        // The exact size depends on font metrics, but it must be positive.
        assertTrue("Width should be positive for non-empty text", size.getWidth() > 0);
        assertTrue("Height should be positive for non-empty text", size.getHeight() > 0);
    }

    @Test
    public void arrangeWithEmptyTextShouldReturnZeroHeight() {
        // Arrange
        LabelBlock block = new LabelBlock("", TEST_FONT);
        Graphics2D g2 = createTestGraphics2D();
        RectangleConstraint constraint = new RectangleConstraint(0.0, null, LengthConstraintType.FIXED, 
                                                                 0.0, null, LengthConstraintType.NONE);

        // Act
        Size2D size = block.arrange(g2, constraint);

        // Assert
        // An empty label should have zero height.
        assertEquals(0.0, size.getHeight(), 0.001);
    }

    @Test
    public void drawShouldExecuteWithoutErrors() {
        // Arrange
        LabelBlock block = new LabelBlock("Test", TEST_FONT, TEST_PAINT);
        Graphics2D g2 = createTestGraphics2D();
        Rectangle2D area = new Rectangle2D.Double(0, 0, 100, 50);

        // Act & Assert
        // This test ensures the draw method runs without exceptions. Verifying
        // graphical output is complex and typically out of scope for unit tests.
        try {
            block.draw(g2, area);
        } catch (Exception e) {
            fail("draw() method should not throw an exception: " + e.getMessage());
        }
    }
    
    @Test
    public void drawWithParamsShouldReturnNullAsPerContract() {
        // Arrange
        LabelBlock block = new LabelBlock("Test");
        Graphics2D g2 = createTestGraphics2D();
        Rectangle2D area = new Rectangle2D.Double(0, 0, 100, 50);

        // Act
        Object result = block.draw(g2, area, null);

        // Assert
        // The method is documented to return null if no block result is generated.
        assertNull(result);
    }
}