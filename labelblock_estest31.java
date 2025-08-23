package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertNull;
import java.awt.Font;

/**
 * Unit tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies that getFont() returns null if the block was constructed with a
     * null font.
     *
     * Note: The LabelBlock constructor's Javadoc states that the font parameter
     * should not be null. However, the implementation does not enforce this.
     * This test confirms the actual behavior of the current implementation.
     */
    @Test
    public void getFontShouldReturnNullWhenConstructedWithNullFont() {
        // Arrange: Create a LabelBlock instance, intentionally passing a null font
        // to test how the class handles it.
        LabelBlock labelBlock = new LabelBlock("Test Label", null);

        // Act: Retrieve the font from the LabelBlock.
        Font retrievedFont = labelBlock.getFont();

        // Assert: The retrieved font should be null, matching what was passed to the constructor.
        assertNull("The font should be null as it was set to null in the constructor.", retrievedFont);
    }
}