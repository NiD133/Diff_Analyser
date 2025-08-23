package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * This test suite focuses on edge cases for the GridArrangement class.
 */
public class GridArrangement_ESTestTest35 extends GridArrangement_ESTest_scaffolding {

    /**
     * Verifies that attempting to arrange a container that contains itself
     * results in a StackOverflowError. This is expected because the arrangement
     * process becomes infinitely recursive.
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void arrangeRRWithRecursiveContainerShouldThrowStackOverflowError() {
        // Arrange: Create a grid arrangement and a block container that adds itself,
        // creating a recursive data structure.
        GridArrangement arrangement = new GridArrangement(10, 10);
        BlockContainer recursiveContainer = new BlockContainer();
        recursiveContainer.add(recursiveContainer); // The container now contains itself.

        // Act: Attempt to arrange the container. The recursive structure should
        // trigger a StackOverflowError. The Graphics2D context is null as it's
        // not needed to trigger the error.
        arrangement.arrangeRR(recursiveContainer, null, RectangleConstraint.NONE);

        // Assert: The test passes if a StackOverflowError is thrown, as declared
        // in the @Test annotation.
    }
}