package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.awt.Font;

/**
 * This test class was improved to clearly verify the font handling
 * of the LabelBlock class. The original test was auto-generated and obscure.
 */
public class LabelBlock_ESTestTest6 extends LabelBlock_ESTest_scaffolding {

    /**
     * Verifies that the setFont() method correctly updates the font of the LabelBlock,
     * and that getFont() subsequently retrieves the same font instance.
     */
    @Test
    public void setFont_shouldUpdateAndReturnTheCorrectFont() {
        // Arrange: Create a LabelBlock and a standard, easily recognizable font.
        LabelBlock labelBlock = new LabelBlock("Test Label");
        Font expectedFont = new Font("Arial", Font.BOLD, 14);

        // Act: Set the new font on the label block and then retrieve it.
        labelBlock.setFont(expectedFont);
        Font actualFont = labelBlock.getFont();

        // Assert: Verify that the font retrieved is the same as the one that was set.
        assertEquals("The font retrieved should be the one that was set.",
                expectedFont, actualFont);
    }
}