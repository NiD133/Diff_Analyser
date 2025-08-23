package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies that the tooltip text can be set via setToolTipText()
     * and correctly retrieved using getToolTipText().
     */
    @Test
    public void shouldSetAndGetToolTipText() {
        // Arrange: Create a LabelBlock and define the expected tooltip text.
        LabelBlock labelBlock = new LabelBlock("Test Label");
        String expectedToolTipText = "This is a sample tooltip.";

        // Act: Set the tooltip text and then retrieve it.
        labelBlock.setToolTipText(expectedToolTipText);
        String actualToolTipText = labelBlock.getToolTipText();

        // Assert: Verify that the retrieved text matches the expected text.
        assertEquals("The retrieved tooltip text should match the value that was set.",
                expectedToolTipText, actualToolTipText);
    }
}