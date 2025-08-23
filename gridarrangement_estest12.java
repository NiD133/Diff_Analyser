package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Contains tests for the {@link GridArrangement} class, focusing on edge cases.
 */
public class GridArrangementTest {

    private static final double DELTA = 0.01;

    /**
     * Verifies that arrangeRR returns a size based on the constraint's range,
     * even when the GridArrangement is initialized with negative dimensions.
     * In this scenario, with an empty container, the arrangement seems to fall back
     * to using the lower bound of the constraint's range for the resulting size.
     */
    @Test
    public void arrangeRRWithNegativeGridAndNegativeRangeConstraintShouldReturnNegativeSize() {
        // Arrange
        final int negativeDimension = -1616;
        GridArrangement gridArrangement = new GridArrangement(negativeDimension, negativeDimension);

        // A constraint where both width and height are defined by a range with negative bounds.
        Range negativeRange = new Range(negativeDimension, negativeDimension);
        RectangleConstraint constraint = new RectangleConstraint(negativeRange, negativeRange);

        BlockContainer emptyContainer = new BlockContainer();
        Graphics2D g2 = null; // The method is tested with a null graphics context.

        // Act
        Size2D actualSize = gridArrangement.arrangeRR(emptyContainer, g2, constraint);

        // Assert
        assertNotNull("The resulting size should not be null", actualSize);
        assertEquals("Width should match the lower bound of the range constraint",
                negativeDimension, actualSize.getWidth(), DELTA);
        assertEquals("Height should match the lower bound of the range constraint",
                negativeDimension, actualSize.getHeight(), DELTA);
    }
}