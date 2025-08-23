package org.jfree.chart.block;

import org.jfree.chart.api.RectangleAnchor;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies that a newly created LabelBlock has a default text anchor of CENTER.
     * This test checks the default state set by the constructor.
     */
    @Test
    public void newLabelBlockShouldHaveCenterTextAnchorByDefault() {
        // Arrange: Create a new LabelBlock instance.
        // The constructor should set a default text anchor.
        LabelBlock labelBlock = new LabelBlock("Test Label");
        RectangleAnchor expectedAnchor = RectangleAnchor.CENTER;

        // Act: Retrieve the text anchor from the new instance.
        RectangleAnchor actualAnchor = labelBlock.getTextAnchor();

        // Assert: Verify that the actual anchor matches the expected default value.
        assertEquals("The default text anchor should be CENTER.", expectedAnchor, actualAnchor);
    }
}