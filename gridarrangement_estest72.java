package org.jfree.chart.block;

import org.jfree.chart.util.LengthConstraintType;
import org.jfree.chart.util.Size2D;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * A test suite for the GridArrangement class, focusing on its arrangement logic.
 */
public class GridArrangementTest {

    /**
     * Verifies that arranging an empty container with a fixed negative width
     * results in a size that respects the negative width and has a NaN height.
     *
     * This tests an edge case where the grid has zero rows, meaning there are no
     * blocks to arrange. The height is unconstrained, so it is expected to be NaN.
     */
    @Test
    public void arrangeWithZeroRowsAndNegativeFixedWidthShouldReturnNegativeWidthAndNaNHeight() {
        // Arrange: Set up a grid arrangement with zero rows, representing an empty layout.
        // The number of columns is irrelevant when rows are zero.
        GridArrangement gridArrangement = new GridArrangement(0, -827);
        BlockContainer emptyContainer = new BlockContainer(gridArrangement);

        // Define a constraint with a fixed negative width and no height constraint.
        final double fixedNegativeWidth = -827.0;
        RectangleConstraint constraint = new RectangleConstraint(
                fixedNegativeWidth, null, LengthConstraintType.FIXED,
                0.0, null, LengthConstraintType.NONE);

        // Act: Arrange the empty container. The Graphics2D context is not used
        // in this scenario, so null is acceptable.
        Size2D arrangedSize = gridArrangement.arrange(emptyContainer, null, constraint);

        // Assert: The resulting size should have the specified negative width
        // and a NaN height, as it's unconstrained and there are no items.
        assertEquals("Width should match the fixed negative constraint",
                fixedNegativeWidth, arrangedSize.getWidth(), 0.01);
        assertEquals("Height should be NaN for an unconstrained, empty container",
                Double.NaN, arrangedSize.getHeight(), 0.01);
    }
}