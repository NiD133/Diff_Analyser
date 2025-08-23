package org.jfree.chart.block;

import org.jfree.chart.ui.Size2D;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.awt.Graphics2D;

/**
 * Contains tests for the {@link GridArrangement} class, focusing on edge cases.
 * This class improves upon an auto-generated test.
 */
public class GridArrangement_ESTestTest22 {

    /**
     * Verifies the behavior of the {@code arrangeNF} method when provided with
     * invalid grid dimensions (zero rows, negative columns) and an empty container.
     * <p>
     * This edge case is expected to result in a calculated size with a width of -0.0
     * and a height of NaN, which appears to be the designated outcome for these
     * invalid inputs.
     */
    @Test
    public void arrangeNFWithZeroRowsAndNegativeColumnsShouldReturnNegativeZeroWidthAndNaNHeight() {
        // Arrange: Set up a grid arrangement with invalid dimensions and an empty container.
        GridArrangement gridArrangement = new GridArrangement(0, -827);
        BlockContainer emptyContainer = new BlockContainer(gridArrangement);
        RectangleConstraint noConstraint = RectangleConstraint.NONE;

        // Act: Arrange the empty container. The Graphics2D context is null as it's
        // not required for this particular calculation.
        Size2D arrangedSize = gridArrangement.arrangeNF(emptyContainer, (Graphics2D) null, noConstraint);

        // Assert: The resulting size should reflect the invalid input parameters.
        assertEquals("Width for zero-row grid", -0.0, arrangedSize.getWidth(), 0.01);
        assertEquals("Height for zero-row grid", Double.NaN, arrangedSize.getHeight(), 0.01);
    }
}