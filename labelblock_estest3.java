package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link LabelBlock} class, focusing on its properties.
 */
public class LabelBlockTest {

    /**
     * Verifies that the getToolTipText() method correctly returns the value
     * set by setToolTipText().
     */
    @Test
    public void getToolTipText_ShouldReturnTheSetText() {
        // Arrange
        LabelBlock labelBlock = new LabelBlock("Test Label");
        String expectedToolTip = "This is a sample tooltip.";

        // Act
        labelBlock.setToolTipText(expectedToolTip);
        String actualToolTip = labelBlock.getToolTipText();

        // Assert
        assertEquals("The retrieved tooltip text should match the one that was set.",
                expectedToolTip, actualToolTip);
    }
}