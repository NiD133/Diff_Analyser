package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies that the tool tip text is null by default when a LabelBlock is
     * created using the simple constructor.
     */
    @Test
    public void getToolTipText_shouldReturnNull_whenNotExplicitlySet() {
        // Arrange: Create a new LabelBlock instance without setting a tool tip.
        LabelBlock labelBlock = new LabelBlock("Test Label");

        // Act: Retrieve the tool tip text.
        String toolTipText = labelBlock.getToolTipText();

        // Assert: The default tool tip text should be null.
        assertNull("The tool tip text should be null by default.", toolTipText);
    }
}