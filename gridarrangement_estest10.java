package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.jfree.data.Range;
import org.junit.Test;

import java.awt.Graphics2D;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    private static final double DELTA = 0.001;

    /**
     * Verifies that arranging an empty container results in a size of (0, 0),
     * even when constraints are provided.
     */
    @Test
    public void arrangeRRWithEmptyContainerShouldReturnZeroSize() {
        // Arrange: Create a grid arrangement, an empty container, and a constraint
        // that allows for a non-zero size.
        GridArrangement gridArrangement = new GridArrangement(5, 5);
        BlockContainer emptyContainer = new BlockContainer();

        // The constraint specifies a valid range for width and height.
        Range widthAndHeightRange = new Range(-100.0, 200.0);
        RectangleConstraint constraint = new RectangleConstraint(widthAndHeightRange, widthAndHeightRange);

        // Act: Arrange the empty container. The Graphics2D context can be null
        // as it is not used when the container has no blocks.
        Size2D resultSize = gridArrangement.arrangeRR(emptyContainer, (Graphics2D) null, constraint);

        // Assert: The resulting size should be zero because there are no blocks to arrange.
        assertNotNull(resultSize);
        assertEquals("Width should be 0.0 for an empty container", 0.0, resultSize.getWidth(), DELTA);
        assertEquals("Height should be 0.0 for an empty container", 0.0, resultSize.getHeight(), DELTA);
    }
}