package org.jfree.chart.block;

import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for the GridArrangement class.
 */
public class GridArrangementTest {

    /**
     * Tests that arranging an empty container with a zero-size constraint
     * results in a size of zero width and zero height.
     */
    @Test
    public void arrangeRFWithEmptyContainerAndZeroConstraintShouldReturnZeroSize() {
        // Arrange
        // The grid dimensions (1x1) are arbitrary since the container is empty.
        GridArrangement arrangement = new GridArrangement(1, 1);
        BlockContainer emptyContainer = new BlockContainer();

        // Create a constraint that effectively fixes both width and height to zero.
        Range zeroRange = new Range(0.0, 0.0);
        RectangleConstraint zeroConstraint = new RectangleConstraint(zeroRange, zeroRange);

        // Act
        // The 'RF' in arrangeRF stands for Range-width, Fixed-height.
        // Here, both are constrained to a fixed value of 0.0.
        Size2D resultSize = arrangement.arrangeRF(emptyContainer, (Graphics2D) null, zeroConstraint);

        // Assert
        // The resulting size should have zero width and zero height.
        assertNotNull("The resulting size should not be null.", resultSize);
        assertEquals("Width should be 0.0 for a zero-size constraint.", 0.0, resultSize.getWidth(), 0.0);
        assertEquals("Height should be 0.0 for a zero-size constraint.", 0.0, resultSize.getHeight(), 0.0);
    }
}