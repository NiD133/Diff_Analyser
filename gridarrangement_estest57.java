package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * Contains tests for exceptional behavior in the {@link GridArrangement} class.
 * This test was originally named GridArrangement_ESTestTest57.
 */
public class GridArrangementExceptionTest {

    /**
     * Verifies that arranging a container that contains itself results in a
     * StackOverflowError. This tests the algorithm's behavior when faced with a
     * recursive block structure, which should lead to infinite recursion.
     */
    @Test(expected = StackOverflowError.class)
    public void arrange_withRecursiveContainer_shouldThrowStackOverflowError() {
        // Arrange: Create a BlockContainer that adds itself as a child element,
        // forming a recursive data structure.
        BlockContainer recursiveContainer = new BlockContainer();
        recursiveContainer.add(recursiveContainer);

        // Arrange: Create a GridArrangement. The specific dimensions are not
        // critical to triggering the error, but we use invalid ones as in the
        // original test to fully replicate the scenario.
        GridArrangement arrangement = new GridArrangement(-1, -1);
        
        RectangleConstraint constraint = RectangleConstraint.NONE;
        
        // The Graphics2D context is not used in the code path that leads to the
        // stack overflow, so it can be null for this test.
        Graphics2D g2 = null;

        // Act & Assert: Calling an arrange method on this recursive structure
        // is expected to cause infinite recursion. The @Test(expected=...)
        // annotation asserts that a StackOverflowError is thrown.
        arrangement.arrangeFR(recursiveContainer, g2, constraint);
    }
}