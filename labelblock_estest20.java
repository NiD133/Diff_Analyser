package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests for the equality logic in the {@link LabelBlock} class.
 */
public class LabelBlockEqualsTest {

    /**
     * Verifies that the equals() method correctly distinguishes between two
     * LabelBlock objects when their toolTipText property differs.
     */
    @Test
    public void equals_shouldReturnFalse_whenToolTipTextIsDifferent() {
        // Arrange: Create two LabelBlock instances that are otherwise identical.
        // By default, their toolTipText is null.
        LabelBlock block1 = new LabelBlock("Test Label");
        LabelBlock block2 = new LabelBlock("Test Label");

        // Assert that the blocks are equal to begin with. This is a sanity check.
        assertEquals(block1, block2);

        // Act: Modify the toolTipText of one block, making it different from the other.
        block2.setToolTipText("A new tooltip");

        // Assert: The blocks should no longer be considered equal.
        assertNotEquals(block1, block2);
    }
}