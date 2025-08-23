package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertSame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;

/**
 * Contains unit tests for the {@link LabelBlock} class.
 * This refactored test focuses on verifying the behavior of the font property.
 */
public class LabelBlockTest {

    /**
     * Verifies that the getFont() method correctly returns the same Font instance
     * that was provided to the LabelBlock's constructor.
     */
    @Test
    public void getFont_ShouldReturnFontSetInConstructor() {
        // Arrange: Define the properties for creating a LabelBlock instance.
        // Using clear, meaningful values makes the test's purpose obvious.
        String labelText = "Sample Title";
        Font expectedFont = new Font("Arial", Font.BOLD, 14);
        Paint textColor = Color.BLACK;

        // Act: Create a LabelBlock with the specified font and then retrieve it.
        LabelBlock labelBlock = new LabelBlock(labelText, expectedFont, textColor);
        Font actualFont = labelBlock.getFont();

        // Assert: The retrieved font should be the exact same instance as the one provided.
        // Using assertSame checks for object identity, which is correct in this case.
        assertSame("The font returned by getFont() should be the same instance "
                + "as the one passed to the constructor.", expectedFont, actualFont);
    }
}