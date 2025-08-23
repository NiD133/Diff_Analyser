package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertTrue;

/**
 * Unit tests for the {@link GridArrangement} class, focusing on edge cases.
 */
public class GridArrangementTest {

    /**
     * Tests that calling the protected method {@code arrangeFF} with a grid
     * configured to have zero rows and zero columns results in a size with
     * NaN (Not a Number) dimensions. This is an important edge case, as it
     * could lead to division-by-zero errors when calculating cell sizes.
     */
    @Test
    public void arrangeFF_withZeroRowsAndColumns_shouldReturnNaNDimensions() {
        // Arrange: Create a 0x0 grid arrangement and an empty container for it.
        GridArrangement arrangement = new GridArrangement(0, 0);
        BlockContainer emptyContainer = new BlockContainer(arrangement);

        // Define a constraint with a fixed width and height. The arrangeFF method
        // is designed for fixed-fixed constraints.
        RectangleConstraint fixedConstraint = new RectangleConstraint(0.0, new Range(0.0, 0.0));

        // Act: Directly invoke the protected arrangeFF method with the 0x0 grid.
        // We pass a null Graphics2D object as it is not used in this calculation.
        Size2D resultSize = arrangement.arrangeFF(emptyContainer, (Graphics2D) null, fixedConstraint);

        // Assert: The resulting size should have NaN for both width and height,
        // which is the expected outcome of a division-by-zero (e.g., width / 0 columns).
        assertTrue("Width should be NaN for a 0x0 grid", Double.isNaN(resultSize.getWidth()));
        assertTrue("Height should be NaN for a 0x0 grid", Double.isNaN(resultSize.getHeight()));
    }
}