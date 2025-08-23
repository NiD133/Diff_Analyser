package org.jfree.chart.block;

import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;

import org.jfree.chart.TestUtils;
import org.jfree.chart.text.TextBlockAnchor;
import org.jfree.chart.api.RectangleAnchor;
import org.jfree.chart.internal.CloneUtils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Tests the {@code equals()} method of {@link LabelBlock} to ensure it correctly
     * distinguishes between different instances based on their properties.
     */
    @Test
    public void testEquals() {
        // Test equality with identical properties
        LabelBlock block1 = createLabelBlock("ABC", Font.PLAIN, 12, Color.RED);
        LabelBlock block2 = createLabelBlock("ABC", Font.PLAIN, 12, Color.RED);
        assertEquals(block1, block2, "Blocks with identical properties should be equal");

        // Test inequality with different text
        block1 = createLabelBlock("XYZ", Font.PLAIN, 12, Color.RED);
        assertNotEquals(block1, block2, "Blocks with different text should not be equal");

        // Update block2 to match block1 and test equality again
        block2 = createLabelBlock("XYZ", Font.PLAIN, 12, Color.RED);
        assertEquals(block1, block2, "Blocks with identical properties should be equal");

        // Test inequality with different font style
        block1 = createLabelBlock("XYZ", Font.BOLD, 12, Color.RED);
        assertNotEquals(block1, block2, "Blocks with different font styles should not be equal");

        // Update block2 to match block1 and test equality again
        block2 = createLabelBlock("XYZ", Font.BOLD, 12, Color.RED);
        assertEquals(block1, block2, "Blocks with identical properties should be equal");

        // Test inequality with different colors
        block1 = createLabelBlock("XYZ", Font.BOLD, 12, Color.BLUE);
        assertNotEquals(block1, block2, "Blocks with different colors should not be equal");

        // Update block2 to match block1 and test equality again
        block2 = createLabelBlock("XYZ", Font.BOLD, 12, Color.BLUE);
        assertEquals(block1, block2, "Blocks with identical properties should be equal");

        // Test inequality with different tooltip text
        block1.setToolTipText("Tooltip");
        assertNotEquals(block1, block2, "Blocks with different tooltip text should not be equal");

        // Update block2 to match block1 and test equality again
        block2.setToolTipText("Tooltip");
        assertEquals(block1, block2, "Blocks with identical properties should be equal");

        // Test inequality with different URL text
        block1.setURLText("URL");
        assertNotEquals(block1, block2, "Blocks with different URL text should not be equal");

        // Update block2 to match block1 and test equality again
        block2.setURLText("URL");
        assertEquals(block1, block2, "Blocks with identical properties should be equal");

        // Test inequality with different content alignment points
        block1.setContentAlignmentPoint(TextBlockAnchor.CENTER_RIGHT);
        assertNotEquals(block1, block2, "Blocks with different content alignment points should not be equal");

        // Update block2 to match block1 and test equality again
        block2.setContentAlignmentPoint(TextBlockAnchor.CENTER_RIGHT);
        assertEquals(block1, block2, "Blocks with identical properties should be equal");

        // Test inequality with different text anchors
        block1.setTextAnchor(RectangleAnchor.BOTTOM_RIGHT);
        assertNotEquals(block1, block2, "Blocks with different text anchors should not be equal");

        // Update block2 to match block1 and test equality again
        block2.setTextAnchor(RectangleAnchor.BOTTOM_RIGHT);
        assertEquals(block1, block2, "Blocks with identical properties should be equal");
    }

    /**
     * Tests the cloning functionality of {@link LabelBlock}.
     */
    @Test
    public void testCloning() throws CloneNotSupportedException {
        LabelBlock original = createLabelBlock("ABC", Font.PLAIN, 12, Color.RED);
        LabelBlock clone = CloneUtils.clone(original);

        assertNotSame(original, clone, "Clone should be a different instance");
        assertSame(original.getClass(), clone.getClass(), "Clone should be of the same class");
        assertEquals(original, clone, "Clone should be equal to the original");
    }

    /**
     * Tests the serialization and deserialization of {@link LabelBlock}.
     */
    @Test
    public void testSerialization() {
        GradientPaint gradientPaint = new GradientPaint(1.0f, 2.0f, Color.RED, 3.0f, 4.0f, Color.BLUE);
        LabelBlock original = new LabelBlock("ABC", new Font("Dialog", Font.PLAIN, 12), gradientPaint);
        LabelBlock deserialized = TestUtils.serialised(original);

        assertEquals(original, deserialized, "Deserialized block should be equal to the original");
    }

    /**
     * Helper method to create a {@link LabelBlock} with specified properties.
     *
     * @param text the text for the label
     * @param fontStyle the style of the font
     * @param fontSize the size of the font
     * @param color the color of the label
     * @return a new {@link LabelBlock} instance
     */
    private LabelBlock createLabelBlock(String text, int fontStyle, int fontSize, Color color) {
        return new LabelBlock(text, new Font("Dialog", fontStyle, fontSize), color);
    }
}