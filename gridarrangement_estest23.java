package org.jfree.chart.block;

import org.jfree.chart.api.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// The test class name is kept for context, but a more descriptive name like
// GridArrangementTest would be preferable in a real-world scenario.
public class GridArrangement_ESTestTest23 {

    /**
     * Tests the behavior of the arrangeNF method when called with an empty container
     * and a RectangleConstraint that has a 'RANGE' type for its height.
     * The method is expected to calculate the size using the lower bound of the
     * height range, even though its name implies a 'FIXED' height constraint.
     */
    @Test
    public void arrangeNF_WithEmptyContainerAndRangeHeightConstraint_ReturnsSizeBasedOnRange() {
        // Arrange
        final int NEGATIVE_DIMENSION = -919;
        GridArrangement gridArrangement = new GridArrangement(NEGATIVE_DIMENSION, NEGATIVE_DIMENSION);
        BlockContainer emptyContainer = new BlockContainer(gridArrangement);

        // Create a constraint with a negative range for both width and height.
        Range negativeRange = new Range(NEGATIVE_DIMENSION, NEGATIVE_DIMENSION);
        RectangleConstraint constraint = new RectangleConstraint(negativeRange, negativeRange);

        // Act
        // Note: We are directly testing the protected 'arrangeNF' method. This method
        // is typically used for 'NO' width constraint and 'FIXED' height constraint.
        // By passing a 'RANGE' height constraint, we are testing a specific edge case.
        Size2D arrangedSize = gridArrangement.arrangeNF(emptyContainer, (Graphics2D) null, constraint);

        // Assert
        assertNotNull("The resulting size should not be null.", arrangedSize);
        assertEquals("Width should be zero for an empty container.", 0.0, arrangedSize.getWidth(), 0.01);
        assertEquals("Height should be the lower bound of the constraint's range.",
                NEGATIVE_DIMENSION, arrangedSize.getHeight(), 0.01);
    }
}