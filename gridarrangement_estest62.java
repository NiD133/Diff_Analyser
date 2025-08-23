package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * This test class contains an improved test case for the GridArrangement class.
 * The original test was automatically generated and has been refactored for better
 * understandability.
 */
public class GridArrangement_ESTestTest62 extends GridArrangement_ESTest_scaffolding {

    /**
     * Tests that calling the arrangeFN method with an empty BlockContainer
     * throws an IndexOutOfBoundsException. This is expected because the
     * arrangement logic attempts to access a block from the empty container.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void arrangeFNWithEmptyContainerShouldThrowException() {
        // Arrange: Create a grid arrangement and an empty container.
        // A 1x1 grid is sufficient to trigger the arrangement logic.
        GridArrangement arrangement = new GridArrangement(1, 1);
        BlockContainer emptyContainer = new BlockContainer();

        // Act: Attempt to arrange the empty container.
        // This call is expected to fail immediately as it tries to access the
        // first block in an empty list. The Graphics2D and RectangleConstraint
        // arguments are required by the method signature but are not relevant
        // to the cause of this specific exception.
        arrangement.arrangeFN(emptyContainer, null, RectangleConstraint.NONE);
    }
}