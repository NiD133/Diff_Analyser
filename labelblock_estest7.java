package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertSame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

/**
 * Tests for the {@link LabelBlock} class, focusing on the font property.
 */
public class LabelBlockTest {

    /**
     * Verifies that the getFont() method correctly returns the Font object
     * that was provided in the constructor.
     */
    @Test
    public void getFont_shouldReturnFontSetInConstructor() {
        // Arrange: Create a standard font and a LabelBlock instance.
        Font expectedFont = new Font("SansSerif", Font.BOLD, 12);
        Paint dummyPaint = Color.BLACK;
        String dummyText = "Test Label";
        LabelBlock labelBlock = new LabelBlock(dummyText, expectedFont, dummyPaint);

        // Act: Retrieve the font from the LabelBlock.
        Font actualFont = labelBlock.getFont();

        // Assert: The retrieved font should be the same instance as the one used for construction.
        assertSame("The font returned by getFont() should be the same instance " +
                   "as the one set in the constructor.", expectedFont, actualFont);
    }
}