package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * Tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * Verifies that arrangeNN throws a StackOverflowError when attempting to arrange
     * a BlockContainer that contains itself. This recursive structure should lead
     * to an infinite loop, which is correctly handled by the JVM throwing this error.
     */
    @Test(expected = StackOverflowError.class)
    public void arrangeNNShouldThrowStackOverflowErrorForRecursiveContainer() {
        // Arrange: Create a grid arrangement and a block container.
        GridArrangement arrangement = new GridArrangement(1, 1);
        BlockContainer recursiveContainer = new BlockContainer();

        // Create a recursive structure by adding the container to its own list of blocks.
        recursiveContainer.add(recursiveContainer);

        // Act & Assert: Attempt to arrange the recursive container.
        // The @Test(expected) annotation asserts that a StackOverflowError is thrown.
        // The Graphics2D context can be null as it is not relevant to this error.
        arrangement.arrangeNN(recursiveContainer, (Graphics2D) null);
    }
}