package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

// Unused imports from the original test have been removed to reduce clutter.

public class GridArrangement_ESTestTest17 extends GridArrangement_ESTest_scaffolding {

    /**
     * Tests that arranging an empty container with a specific ranged constraint
     * results in a size that matches the upper bound of that constraint.
     *
     * The method under test is `arrangeNR`, which is intended for arrangements
     * with a ranged height and no width constraint. This test verifies its
     * behavior when a width constraint is also provided.
     */
    @Test
    public void arrangeNR_withEmptyContainerAndRangedConstraint_returnsSizeMatchingConstraint() {
        // Arrange
        final int gridRows = 105;
        final int gridCols = 105;
        final double expectedDimension = 105.0;

        GridArrangement arrangement = new GridArrangement(gridRows, gridCols);
        BlockContainer emptyContainer = new BlockContainer();

        // Create a constraint where width and height are both a range of [105.0, 105.0],
        // which effectively simulates a fixed-size constraint.
        Range dimensionRange = new Range(expectedDimension, expectedDimension);
        RectangleConstraint constraint = new RectangleConstraint(dimensionRange, dimensionRange);

        // Act
        // The arrangeNR method is called with a null Graphics2D context, as rendering
        // is not required to calculate the size of an empty container.
        Size2D resultingSize = arrangement.arrangeNR(emptyContainer, null, constraint);

        // Assert
        assertNotNull("The resulting size should not be null.", resultingSize);
        assertEquals("The resulting width should match the constraint's dimension.",
                expectedDimension, resultingSize.getWidth(), 0.01);
        assertEquals("The resulting height should match the constraint's dimension.",
                expectedDimension, resultingSize.getHeight(), 0.01);
    }
}