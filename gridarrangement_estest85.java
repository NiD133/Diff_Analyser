package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class contains tests for the GridArrangement class.
 * This particular test was refactored for clarity from an auto-generated test case.
 */
public class GridArrangement_ESTestTest85 {

    /**
     * Verifies the behavior of the arrange method when the GridArrangement is
     * configured with zero rows and a negative number of columns. This is an
     * edge case that could lead to calculation errors like division by zero.
     */
    @Test(timeout = 4000)
    public void arrangeWithZeroRowsAndNegativeColumnsShouldReturnSizeWithZeroWidthAndNaNHeight() {
        // Arrange: Set up a grid arrangement with invalid dimensions (0 rows, negative columns)
        // and an empty container to lay out.
        GridArrangement arrangement = new GridArrangement(0, -2597);
        BlockContainer emptyContainer = new BlockContainer();
        RectangleConstraint zeroSizeConstraint = new RectangleConstraint(0.0, 0.0);

        // Act: Attempt to arrange the empty container with the invalid grid layout.
        Size2D resultSize = arrangement.arrange(emptyContainer, (Graphics2D) null, zeroSizeConstraint);

        // Assert: The resulting width should be zero, and the height should be NaN.
        // The NaN height is the expected outcome of a calculation that likely involves
        // division by the number of rows (which is zero).
        assertEquals("Width should be zero for an invalid grid arrangement", 0.0, resultSize.getWidth(), 0.01);
        assertTrue("Height should be NaN due to division by zero rows", Double.isNaN(resultSize.getHeight()));
    }
}