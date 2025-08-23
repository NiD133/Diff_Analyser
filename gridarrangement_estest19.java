package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Graphics2D;

/**
 * Unit tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * Verifies that arranging an empty container with a 0x0 grid results in a
     * Size2D object with NaN (Not-a-Number) for its width and height. This tests
     * a specific edge case of the arrangeNN method.
     */
    @Test
    public void arrangeNNWithZeroByZeroGridAndEmptyContainerReturnsNaNSize() {
        // Arrange: A 0x0 grid arrangement and an empty block container.
        GridArrangement arrangement = new GridArrangement(0, 0);
        BlockContainer emptyContainer = new BlockContainer();
        Graphics2D g2 = null; // The graphics context is not used in this scenario.

        // Act: Arrange the container with no constraints (the "NN" in arrangeNN).
        Size2D resultSize = arrangement.arrangeNN(emptyContainer, g2);

        // Assert: The resulting size should have NaN for width and height.
        assertNotNull("The result of arrangement should not be null.", resultSize);
        assertEquals("Width should be NaN for a 0x0 grid.", Double.NaN, resultSize.width, 0.0);
        assertEquals("Height should be NaN for a 0x0 grid.", Double.NaN, resultSize.height, 0.0);
    }
}