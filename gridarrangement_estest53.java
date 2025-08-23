package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * A test suite for the {@link GridArrangement} class, focusing on edge cases.
 */
public class GridArrangementTest {

    /**
     * Verifies that attempting to arrange a container that contains itself
     * results in a StackOverflowError.
     *
     * This test covers the pathological case of a recursive block structure. While
     * such a configuration is invalid, the arrangement algorithm should fail fast
     * rather than hanging. A StackOverflowError is an acceptable failure mode
     * for this scenario.
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void arrangeShouldThrowStackOverflowErrorForRecursiveContainer() {
        // Arrange: Create a grid arrangement and a container that adds itself as a block.
        GridArrangement gridArrangement = new GridArrangement(1, 1);
        BlockContainer recursiveContainer = new BlockContainer();

        // This creates a circular reference, which is expected to cause infinite
        // recursion during the arrangement process.
        recursiveContainer.add(recursiveContainer);

        RectangleConstraint constraint = new RectangleConstraint(100.0, 100.0);
        Graphics2D g2 = null; // Graphics2D is not needed to trigger this error.

        // Act & Assert: Attempt to arrange the container.
        // The test expects a StackOverflowError, as declared in the @Test annotation.
        gridArrangement.arrangeNF(recursiveContainer, g2, constraint);
    }
}