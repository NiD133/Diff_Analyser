package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * This test class contains tests for the GridArrangement class, focusing on its
 * behavior with invalid constructor arguments.
 *
 * Note: The original test class name 'GridArrangement_ESTestTest52' is kept for
 * context, but in a real-world scenario, it would be renamed to something more
 * descriptive, like 'GridArrangementTest'.
 */
public class GridArrangement_ESTestTest52 extends GridArrangement_ESTest_scaffolding {

    /**
     * Verifies that calling arrangeNN() on a GridArrangement initialized with
     * negative dimensions throws an IndexOutOfBoundsException. The method is
     * expected to fail during internal array access when calculating block
     * positions with negative grid dimensions.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void arrangeNNShouldThrowExceptionWhenGridDimensionsAreNegative() {
        // Arrange: Create a grid arrangement with invalid negative dimensions.
        // The arrangeNN method uses these dimensions to create and access internal
        // arrays. Negative values are expected to cause an exception during this process.
        GridArrangement arrangement = new GridArrangement(-1, -1);

        // Arrange: Create a container with at least one block, as the arrangeNN
        // method iterates over the blocks in the container.
        BlockContainer container = new BlockContainer();
        container.add(new EmptyBlock(0, 0)); // An EmptyBlock is a simple way to add a block.

        // Act & Assert: Attempt to arrange the container.
        // This call is expected to throw an IndexOutOfBoundsException due to the
        // invalid grid dimensions. The Graphics2D parameter can be null as the
        // code path that uses it is not reached before the exception is thrown.
        arrangement.arrangeNN(container, (Graphics2D) null);
    }
}