package org.jfree.chart.block;

import org.jfree.chart.text.TextBlockAnchor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This test class focuses on the behavior of the equals() method in the LabelBlock class.
 * Note: This is an improved version of a single test case, likely from a larger, generated test suite.
 */
public class LabelBlock_ESTestTest19 extends LabelBlock_ESTest_scaffolding {

    /**
     * Verifies that the equals() method returns false if two LabelBlock objects
     * differ only by their content alignment point.
     */
    @Test
    public void equals_shouldReturnFalse_whenContentAlignmentPointIsDifferent() {
        // Arrange: Create two LabelBlock instances that are identical in every way.
        LabelBlock block1 = new LabelBlock("Test Label");
        LabelBlock block2 = new LabelBlock("Test Label");

        // Assert: The two blocks should be considered equal initially.
        assertEquals("Initially, the two identical blocks should be equal.", block1, block2);

        // Act: Modify the content alignment point of the second block.
        block2.setContentAlignmentPoint(TextBlockAnchor.TOP_RIGHT);

        // Assert: After the modification, the blocks should no longer be equal.
        assertNotEquals("Blocks should not be equal after changing the content alignment point.", block1, block2);
    }
}