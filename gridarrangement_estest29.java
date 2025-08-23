package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;

// The original class name and extension are kept as provided.
public class GridArrangement_ESTestTest29 extends GridArrangement_ESTest_scaffolding {

    /**
     * Tests that arranging an empty container with a zero-column grid using
     * the arrangeFN() method results in a size with NaN width and height.
     * <p>
     * This scenario represents an edge case where a division by the number of
     * columns (zero) likely occurs, leading to a non-finite result.
     */
    @Test
    public void arrangeFNWithZeroColumnGridShouldReturnNaNSiz() {
        // Arrange
        // A grid arrangement with zero rows and zero columns is an edge case.
        GridArrangement arrangement = new GridArrangement(0, 0);
        BlockContainer emptyContainer = new BlockContainer();
        RectangleConstraint unconstrained = RectangleConstraint.NONE;

        // Act
        // The arrangeFN method is called, which expects a fixed-width constraint.
        // Here, we pass an unconstrained one to test this specific edge case.
        // The Graphics2D object is not used in this calculation, so it can be null.
        Size2D resultSize = arrangement.arrangeFN(emptyContainer, null, unconstrained);

        // Assert
        // The expected result is NaN because the calculation likely involves
        // dividing by the number of columns, which is zero.
        assertEquals("Width should be NaN for a zero-column grid",
                Double.NaN, resultSize.getWidth(), 0.0);
        assertEquals("Height should be NaN for a zero-column grid",
                Double.NaN, resultSize.getHeight(), 0.0);
    }
}