package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * A test suite for the {@link GridArrangement} class, focusing on edge cases.
 */
public class GridArrangementTest {

    /**
     * Verifies that attempting to arrange a {@link BlockContainer} that contains
     * itself results in a {@link StackOverflowError}. This tests the handling
     * of recursive block structures which should lead to infinite recursion.
     */
    @Test(expected = StackOverflowError.class, timeout = 4000)
    public void arrangeShouldThrowStackOverflowErrorWhenContainerContainsItself() {
        // Arrange: Create a grid arrangement and a container that recursively contains itself.
        GridArrangement gridArrangement = new GridArrangement(80, 80);
        BlockContainer recursiveContainer = new BlockContainer();
        
        // This creates the recursive relationship that will cause the stack overflow.
        recursiveContainer.add(recursiveContainer);

        RectangleConstraint noConstraint = RectangleConstraint.NONE;
        Graphics2D g2 = null; // The Graphics2D object is not relevant for this test case.

        // Act & Assert:
        // Attempt to arrange the container. The test expects a StackOverflowError,
        // which is declared in the @Test annotation.
        gridArrangement.arrangeRN(recursiveContainer, g2, noConstraint);
    }
}