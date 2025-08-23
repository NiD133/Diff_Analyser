package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * Unit tests for the {@link GridArrangement} class.
 * This focuses on the behavior of the arrange method under specific conditions.
 */
public class GridArrangementTest {

    /**
     * Verifies that the arrange method throws a StackOverflowError when attempting
     * to arrange a BlockContainer that contains itself. This scenario creates an
     * infinite recursion loop, which should be handled by the JVM throwing this error.
     */
    @Test(expected = StackOverflowError.class)
    public void arrangeShouldThrowStackOverflowErrorWhenContainerContainsItself() {
        // Arrange: Create a grid arrangement and a container that recursively
        // contains itself. The grid dimensions (1x1) are arbitrary and do not
        // affect the outcome.
        GridArrangement arrangement = new GridArrangement(1, 1);
        BlockContainer selfContainingContainer = new BlockContainer();
        selfContainingContainer.add(selfContainingContainer); // This creates the recursive relationship.

        // Act: Attempt to arrange the container. The arrangement process will
        // try to calculate the size of the container, which in turn requires
        // calculating the size of its children, leading to infinite recursion.
        // The Graphics2D and RectangleConstraint parameters are not relevant to
        // triggering the recursion.
        arrangement.arrange(selfContainingContainer, null, RectangleConstraint.NONE);

        // Assert: The test is expected to throw a StackOverflowError.
        // The JUnit @Test(expected=...) annotation handles this assertion.
    }
}