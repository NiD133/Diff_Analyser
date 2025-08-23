package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * A collection of tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies that the URL text for a newly created LabelBlock is null by default.
     * A URL text must be explicitly set after construction.
     */
    @Test
    public void getURLText_shouldReturnNullByDefault() {
        // Arrange: Create a new LabelBlock instance. The URL text is not set
        // in any of the constructors.
        LabelBlock labelBlock = new LabelBlock("A sample label");

        // Act: Retrieve the URL text from the block.
        String urlText = labelBlock.getURLText();

        // Assert: The URL text should be null since it was never set.
        assertNull("The default URL text for a new LabelBlock should be null.", urlText);
    }
}