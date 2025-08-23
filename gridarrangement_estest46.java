package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * Tests for the {@link GridArrangement} class, focusing on edge cases.
 */
public class GridArrangementTest {

    /**
     * Verifies that attempting to arrange a container that recursively contains itself
     * results in a StackOverflowError. This tests the arrangement's behavior
     * with pathological object graphs, preventing infinite loops.
     */
    @Test(expected = StackOverflowError.class)
    public void arrangeShouldThrowStackOverflowErrorForRecursiveContainer() {
        // Arrange: Create a grid arrangement and a container that contains itself.
        GridArrangement arrangement = new GridArrangement(1, 1);
        BlockContainer recursiveContainer = new BlockContainer();
        recursiveContainer.add(recursiveContainer); // This creates the recursive structure.

        // Act: Attempt to arrange the container.
        // The arrangement logic will try to process the container's blocks, find the
        // container itself, and recurse infinitely, leading to a stack overflow.
        arrangement.arrangeNR(recursiveContainer, null, RectangleConstraint.NONE);
    }
}