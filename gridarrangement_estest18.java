package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * This test verifies the behavior of the `arrangeNR` method when provided with
     * a constraint based on a negative range.
     *
     * The `arrangeNR` method is designed for arranging with "No" width constraint
     * and a "Ranged" height constraint. This test uses an empty container and a
     * constraint where the range for both width and height is a single negative
     * value (e.g., [-919.0, -919.0]).
     *
     * The expected behavior, based on the current implementation, is that the
     * method returns a Size2D object with both width and height equal to this
     * negative value. This test documents this unusual edge-case behavior.
     */
    @Test
    public void arrangeNR_withNegativeRangeConstraint_shouldReturnNegativeSize() {
        // Arrange
        final int NEGATIVE_DIMENSION = -919;
        
        // Create a grid arrangement with negative row and column counts.
        GridArrangement arrangement = new GridArrangement(NEGATIVE_DIMENSION, NEGATIVE_DIMENSION);
        BlockContainer emptyContainer = new BlockContainer(arrangement);

        // Define a constraint where the range for width and height is a single negative number.
        Range negativeRange = new Range(NEGATIVE_DIMENSION, NEGATIVE_DIMENSION);
        RectangleConstraint constraint = new RectangleConstraint(negativeRange, negativeRange);

        // Act
        // Arrange the empty container with the specified constraint.
        // The Graphics2D context is null, as it's not used by this arrangement logic.
        Size2D resultSize = arrangement.arrangeNR(emptyContainer, (Graphics2D) null, constraint);

        // Assert
        assertNotNull("The resulting size should not be null.", resultSize);
        
        assertEquals("The resulting width should match the negative value from the constraint's range.",
                NEGATIVE_DIMENSION, resultSize.getWidth(), 0.01);
        assertEquals("The resulting height should match the negative value from the constraint's range.",
                NEGATIVE_DIMENSION, resultSize.getHeight(), 0.01);
    }
}