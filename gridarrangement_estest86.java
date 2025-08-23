package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;

/**
 * A test suite for the GridArrangement class.
 */
public class GridArrangementTest {

    /**
     * Verifies that arranging an empty container with a zero-width constraint
     * results in a size of (0, 0).
     *
     * This test specifically calls the `arrangeRN` method, which handles
     * arrangements with a ranged-width and no-height constraint. For an empty
     * container, the calculated size should be zero, regardless of the grid
     * configuration.
     */
    @Test
    public void arrangeRN_withEmptyContainerAndZeroWidthConstraint_shouldReturnZeroSize() {
        // Arrange: Set up the components for the test.
        // The grid is initialized with negative dimensions to cover an edge case.
        final GridArrangement arrangement = new GridArrangement(-1, -1);
        final BlockContainer emptyContainer = new BlockContainer();

        // A zero-length range creates a constraint that effectively fixes the width to 0.
        final Range zeroLengthRange = new Range(0.0, 0.0);
        final RectangleConstraint constraint = new RectangleConstraint(zeroLengthRange, zeroLengthRange);

        // Act: Arrange the empty container. The Graphics2D context is not used in this scenario.
        final Size2D arrangedSize = arrangement.arrangeRN(emptyContainer, (Graphics2D) null, constraint);

        // Assert: The resulting size should have zero width and zero height.
        assertEquals("Width should be zero for an empty container with a zero-width constraint",
                0.0, arrangedSize.width, 0.0);
        assertEquals("Height should be zero for an empty container",
                0.0, arrangedSize.height, 0.0);
    }
}