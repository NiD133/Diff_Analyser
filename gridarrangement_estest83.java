package org.jfree.chart.block;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.Graphics2D;

// Note: The original test class name and inheritance structure have been preserved.
// The focus of the improvement is on the test method itself.
public class GridArrangement_ESTestTest83 extends GridArrangement_ESTest_scaffolding {

    /**
     * Tests that arranging an empty container with a grid that has zero columns
     * results in a calculated size with a NaN width and zero height. This is an
     * important edge case, as it can lead to division-by-zero errors.
     */
    @Test(timeout = 4000)
    public void arrangeWithZeroColumnsAndEmptyContainerShouldReturnNaNWidth() {
        // Arrange: Create a grid arrangement with 1 row and 0 columns.
        GridArrangement arrangement = new GridArrangement(1, 0);
        BlockContainer emptyContainer = new BlockContainer(arrangement);
        RectangleConstraint noConstraint = RectangleConstraint.NONE;

        // Act: Arrange the container. The arrangeFN method is called directly to test
        // its specific behavior with a zero-column grid.
        Size2D resultSize = arrangement.arrangeFN(emptyContainer, (Graphics2D) null, noConstraint);

        // Assert: Verify that the width is NaN and the height is zero.
        assertEquals("Width should be NaN for a zero-column grid", Double.NaN, resultSize.getWidth(), 0.0);
        assertEquals("Height should be 0.0 for an empty container", 0.0, resultSize.getHeight(), 0.0);
    }
}