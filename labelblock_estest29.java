package org.jfree.chart.block;

import org.jfree.chart.api.RectangleAnchor;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Contains tests for the equals() method in the {@link LabelBlock} class.
 */
public class LabelBlockTest {

    /**
     * Verifies that the equals() method returns false for two LabelBlock instances
     * that have the same text but different text anchors.
     */
    @Test
    public void equals_shouldReturnFalse_whenTextAnchorsDiffer() {
        // Arrange: Create two LabelBlock instances with the same text.
        // By default, their text anchor is RectangleAnchor.CENTER.
        String labelText = "Test Label";
        LabelBlock block1 = new LabelBlock(labelText);
        LabelBlock block2 = new LabelBlock(labelText);

        // Act: Modify the text anchor of the first block to be different from the second.
        block1.setTextAnchor(RectangleAnchor.BOTTOM);

        // Assert: The two blocks should no longer be considered equal.
        assertFalse("Blocks with different text anchors should not be equal", block1.equals(block2));
    }
}