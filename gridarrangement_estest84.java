package org.jfree.chart.block;

import org.jfree.chart.ui.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * This test verifies the behavior of the arrange method when the GridArrangement
     * is initialized with negative row and column counts, which is an edge case.
     * When arranging an empty container with a RANGE constraint, the resulting size
     * should be derived from the constraint's boundaries.
     */
    @Test
    public void arrangeWithNegativeDimensionsAndEmptyContainerShouldRespectRangeConstraint() {
        // Arrange
        final int invalidDimension = -1068;
        GridArrangement arrangement = new GridArrangement(invalidDimension, invalidDimension);
        BlockContainer emptyContainer = new BlockContainer();

        // Define a constraint with a specific width range and a broader height range.
        // The width range [-1068, -1068] forces a specific width calculation.
        Range widthRange = new Range(invalidDimension, invalidDimension);
        Range heightRange = new Range(invalidDimension, 1.0);
        RectangleConstraint constraint = new RectangleConstraint(widthRange, heightRange);

        // Act
        // Arrange the empty container. The Graphics2D context is not used in this
        // calculation path, so null is acceptable.
        Size2D resultSize = arrangement.arrange(emptyContainer, null, constraint);

        // Assert
        // The resulting width should be constrained by the lower bound of the width range.
        // The height is expected to be zero as there are no blocks to arrange.
        assertEquals("Width should match the lower bound of the constraint range",
                invalidDimension, resultSize.getWidth(), 0.01);
        assertEquals("Height should be zero for an empty container",
                0.0, resultSize.getHeight(), 0.01);
    }
}