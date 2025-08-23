package org.jfree.chart.block;

import org.jfree.chart.ui.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GridArrangement_ESTestTest15 extends GridArrangement_ESTest_scaffolding {

    /**
     * Tests that arrangeNR() handles an invalid grid (negative rows/columns)
     * and an empty container gracefully, returning a size of (0, 0).
     */
    @Test
    public void arrangeNRWithNegativeDimensionsAndEmptyContainerShouldReturnZeroSize() {
        // Arrange: Create a grid arrangement with invalid negative dimensions,
        // an empty container, and a zero-range height constraint.
        GridArrangement arrangement = new GridArrangement(-827, -827);
        BlockContainer emptyContainer = new BlockContainer(arrangement);
        Range zeroHeightRange = new Range(0.0, 0.0);
        RectangleConstraint constraint = new RectangleConstraint(0.0, zeroHeightRange);

        // Act: Arrange the empty container. The Graphics2D context is not used
        // in this scenario, so null is acceptable.
        Size2D arrangedSize = arrangement.arrangeNR(emptyContainer, (Graphics2D) null, constraint);

        // Assert: The resulting size should be non-null with zero width and height.
        assertNotNull(arrangedSize);
        assertEquals("Width should be zero for an empty container", 0.0, arrangedSize.getWidth(), 0.01);
        assertEquals("Height should be zero for an empty container", 0.0, arrangedSize.getHeight(), 0.01);
    }
}