package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link GridArrangement} class, focusing on edge cases.
 */
public class GridArrangementTest {

    /**
     * Tests that arranging an empty container with a negative range constraint
     * results in a size whose dimensions match the lower bound of the negative range.
     * This is an edge case test for the arrangeRF method, which handles a
     * Range-constrained width and a Fixed-height constraint.
     */
    @Test
    public void arrangeRFWithNegativeRangeConstraintShouldReturnSizeMatchingRangeLowerBound() {
        // Arrange
        final int negativeRowCount = -919;
        final int negativeColCount = -919;
        final double negativeDimension = -919.0;

        // Create a grid arrangement with negative rows and columns, an edge case.
        GridArrangement arrangement = new GridArrangement(negativeRowCount, negativeColCount);
        BlockContainer emptyContainer = new BlockContainer(arrangement);

        // Define a constraint where both width and height ranges are a single negative value.
        Range negativeRange = new Range(negativeDimension, negativeDimension);
        RectangleConstraint constraint = new RectangleConstraint(negativeRange, negativeRange);

        // Act
        // Arrange the empty container. The Graphics2D context is not used in this scenario.
        Size2D actualSize = arrangement.arrangeRF(emptyContainer, null, constraint);

        // Assert
        assertNotNull("The resulting size should not be null", actualSize);
        assertEquals("Width should match the lower bound of the negative range constraint",
                negativeDimension, actualSize.getWidth(), 0.01);
        assertEquals("Height should match the lower bound of the negative range constraint",
                negativeDimension, actualSize.getHeight(), 0.01);
    }
}