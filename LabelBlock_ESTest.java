package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.util.Hashtable;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.block.LabelBlock;
import org.jfree.chart.block.RectangleConstraint;
import org.jfree.chart.block.Size2D;
import org.jfree.chart.text.TextBlockAnchor;

/**
 * Test suite for LabelBlock class functionality.
 * Tests cover construction, property management, rendering, and equality operations.
 */
public class LabelBlockTest {

    // Test Constants
    private static final String SAMPLE_TEXT = "Sample Label";
    private static final String EMPTY_TEXT = "";
    private static final Font DEFAULT_FONT = new Font("SansSerif", Font.PLAIN, 12);
    private static final Color DEFAULT_COLOR = Color.BLACK;

    // ========== Constructor Tests ==========

    @Test
    public void testConstructorWithTextOnly() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        
        assertNotNull("Label block should be created", labelBlock);
        assertNotNull("Font should have default value", labelBlock.getFont());
        assertNotNull("Paint should have default value", labelBlock.getPaint());
    }

    @Test
    public void testConstructorWithTextAndFont() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT, DEFAULT_FONT);
        
        assertEquals("Font should match provided font", DEFAULT_FONT, labelBlock.getFont());
    }

    @Test
    public void testConstructorWithAllParameters() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT, DEFAULT_FONT, DEFAULT_COLOR);
        
        assertEquals("Font should match provided font", DEFAULT_FONT, labelBlock.getFont());
        assertEquals("Paint should match provided color", DEFAULT_COLOR, labelBlock.getPaint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRejectsNullText() {
        new LabelBlock(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRejectsNullFont() {
        new LabelBlock(SAMPLE_TEXT, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorRejectsNullPaint() {
        new LabelBlock(SAMPLE_TEXT, DEFAULT_FONT, null);
    }

    // ========== Property Management Tests ==========

    @Test
    public void testFontPropertyManagement() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        Font newFont = new Font("Arial", Font.BOLD, 14);
        
        labelBlock.setFont(newFont);
        
        assertEquals("Font should be updated", newFont, labelBlock.getFont());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetFontRejectsNull() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        labelBlock.setFont(null);
    }

    @Test
    public void testPaintPropertyManagement() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        Color newColor = Color.RED;
        
        labelBlock.setPaint(newColor);
        
        assertEquals("Paint should be updated", newColor, labelBlock.getPaint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPaintRejectsNull() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        labelBlock.setPaint(null);
    }

    @Test
    public void testToolTipTextManagement() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        String toolTip = "This is a tooltip";
        
        assertNull("Initial tooltip should be null", labelBlock.getToolTipText());
        
        labelBlock.setToolTipText(toolTip);
        assertEquals("Tooltip should be set", toolTip, labelBlock.getToolTipText());
        
        labelBlock.setToolTipText(null);
        assertNull("Tooltip should be cleared", labelBlock.getToolTipText());
    }

    @Test
    public void testUrlTextManagement() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        String url = "http://example.com";
        
        assertNull("Initial URL should be null", labelBlock.getURLText());
        
        labelBlock.setURLText(url);
        assertEquals("URL should be set", url, labelBlock.getURLText());
        
        labelBlock.setURLText(null);
        assertNull("URL should be cleared", labelBlock.getURLText());
    }

    @Test
    public void testContentAlignmentPointManagement() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        
        assertEquals("Default alignment should be CENTER", 
                    TextBlockAnchor.CENTER, labelBlock.getContentAlignmentPoint());
        
        labelBlock.setContentAlignmentPoint(TextBlockAnchor.TOP_RIGHT);
        assertEquals("Alignment should be updated", 
                    TextBlockAnchor.TOP_RIGHT, labelBlock.getContentAlignmentPoint());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetContentAlignmentPointRejectsNull() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        labelBlock.setContentAlignmentPoint(null);
    }

    @Test
    public void testTextAnchorManagement() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        
        assertEquals("Default text anchor should be CENTER", 
                    RectangleAnchor.CENTER, labelBlock.getTextAnchor());
        
        labelBlock.setTextAnchor(RectangleAnchor.BOTTOM);
        assertEquals("Text anchor should be updated", 
                    RectangleAnchor.BOTTOM, labelBlock.getTextAnchor());
    }

    // ========== Rendering Tests ==========

    @Test
    public void testArrangeWithValidConstraint() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        Graphics2D graphics = createTestGraphics();
        RectangleConstraint constraint = RectangleConstraint.NONE;
        
        Size2D size = labelBlock.arrange(graphics, constraint);
        
        assertNotNull("Size should be returned", size);
        assertTrue("Height should be positive", size.getHeight() > 0);
        assertTrue("Width should be positive", size.getWidth() > 0);
    }

    @Test(expected = NullPointerException.class)
    public void testArrangeRejectsNullGraphics() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        labelBlock.arrange(null, RectangleConstraint.NONE);
    }

    @Test(expected = NullPointerException.class)
    public void testArrangeRejectsNullConstraint() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        Graphics2D graphics = createTestGraphics();
        labelBlock.arrange(graphics, null);
    }

    @Test
    public void testDrawWithValidParameters() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        Graphics2D graphics = createTestGraphics();
        Rectangle2D area = new Rectangle(0, 0, 100, 50);
        
        // Should not throw exception
        Object result = labelBlock.draw(graphics, area, null);
        assertNull("Draw should return null", result);
    }

    // ========== Equality and Cloning Tests ==========

    @Test
    public void testEqualityWithIdenticalBlocks() {
        LabelBlock block1 = new LabelBlock(SAMPLE_TEXT);
        LabelBlock block2 = new LabelBlock(SAMPLE_TEXT);
        
        assertTrue("Identical blocks should be equal", block1.equals(block2));
        assertTrue("Equality should be symmetric", block2.equals(block1));
    }

    @Test
    public void testEqualityWithSameInstance() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        
        assertTrue("Block should equal itself", labelBlock.equals(labelBlock));
    }

    @Test
    public void testInequalityWithDifferentText() {
        LabelBlock block1 = new LabelBlock("Text 1");
        LabelBlock block2 = new LabelBlock("Text 2");
        
        assertFalse("Blocks with different text should not be equal", block1.equals(block2));
    }

    @Test
    public void testInequalityWithDifferentFont() {
        Font font1 = new Font("Arial", Font.PLAIN, 12);
        Font font2 = new Font("Times", Font.BOLD, 14);
        LabelBlock block1 = new LabelBlock(SAMPLE_TEXT, font1);
        LabelBlock block2 = new LabelBlock(SAMPLE_TEXT, font2);
        
        assertFalse("Blocks with different fonts should not be equal", block1.equals(block2));
    }

    @Test
    public void testInequalityWithDifferentPaint() {
        LabelBlock block1 = new LabelBlock(SAMPLE_TEXT, DEFAULT_FONT, Color.RED);
        LabelBlock block2 = new LabelBlock(SAMPLE_TEXT, DEFAULT_FONT, Color.BLUE);
        
        assertFalse("Blocks with different paint should not be equal", block1.equals(block2));
    }

    @Test
    public void testInequalityWithDifferentToolTip() {
        LabelBlock block1 = new LabelBlock(SAMPLE_TEXT);
        LabelBlock block2 = new LabelBlock(SAMPLE_TEXT);
        
        block1.setToolTipText("Tooltip 1");
        block2.setToolTipText("Tooltip 2");
        
        assertFalse("Blocks with different tooltips should not be equal", block1.equals(block2));
    }

    @Test
    public void testInequalityWithDifferentUrl() {
        LabelBlock block1 = new LabelBlock(SAMPLE_TEXT);
        LabelBlock block2 = new LabelBlock(SAMPLE_TEXT);
        
        block1.setURLText("http://example1.com");
        block2.setURLText("http://example2.com");
        
        assertFalse("Blocks with different URLs should not be equal", block1.equals(block2));
    }

    @Test
    public void testInequalityWithDifferentContentAlignment() {
        LabelBlock block1 = new LabelBlock(SAMPLE_TEXT);
        LabelBlock block2 = new LabelBlock(SAMPLE_TEXT);
        
        block1.setContentAlignmentPoint(TextBlockAnchor.CENTER);
        block2.setContentAlignmentPoint(TextBlockAnchor.TOP_RIGHT);
        
        assertFalse("Blocks with different content alignment should not be equal", 
                   block1.equals(block2));
    }

    @Test
    public void testInequalityWithDifferentTextAnchor() {
        LabelBlock block1 = new LabelBlock(SAMPLE_TEXT);
        LabelBlock block2 = new LabelBlock(SAMPLE_TEXT);
        
        block1.setTextAnchor(RectangleAnchor.CENTER);
        block2.setTextAnchor(RectangleAnchor.BOTTOM);
        
        assertFalse("Blocks with different text anchors should not be equal", 
                   block1.equals(block2));
    }

    @Test
    public void testInequalityWithNonLabelBlockObject() {
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT);
        String otherObject = "Not a LabelBlock";
        
        assertFalse("LabelBlock should not equal non-LabelBlock object", 
                   labelBlock.equals(otherObject));
    }

    @Test
    public void testCloning() throws CloneNotSupportedException {
        LabelBlock original = new LabelBlock(SAMPLE_TEXT);
        original.setToolTipText("Test tooltip");
        original.setURLText("http://example.com");
        
        LabelBlock clone = (LabelBlock) original.clone();
        
        assertNotSame("Clone should be different instance", original, clone);
        assertEquals("Clone should be equal to original", original, clone);
    }

    // ========== Edge Cases ==========

    @Test
    public void testEmptyTextHandling() {
        LabelBlock labelBlock = new LabelBlock(EMPTY_TEXT);
        Graphics2D graphics = createTestGraphics();
        RectangleConstraint constraint = RectangleConstraint.NONE;
        
        Size2D size = labelBlock.arrange(graphics, constraint);
        
        assertNotNull("Size should be returned for empty text", size);
        // Empty text should still have some height due to font metrics
        assertTrue("Height should be non-negative", size.getHeight() >= 0);
    }

    @Test
    public void testComplexFontHandling() {
        // Test with font attributes
        Hashtable<AttributedCharacterIterator.Attribute, Object> attributes = 
            new Hashtable<>();
        Font complexFont = new Font(attributes);
        
        LabelBlock labelBlock = new LabelBlock(SAMPLE_TEXT, complexFont, Color.CYAN);
        
        assertEquals("Complex font should be preserved", complexFont, labelBlock.getFont());
        assertEquals("Color should be preserved", Color.CYAN, labelBlock.getPaint());
    }

    // ========== Helper Methods ==========

    /**
     * Creates a test Graphics2D instance for testing purposes.
     */
    private Graphics2D createTestGraphics() {
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        return image.createGraphics();
    }
}