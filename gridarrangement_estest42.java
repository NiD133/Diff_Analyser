package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * This test suite contains tests for the GridArrangement class.
 * This specific test case focuses on how the arrangement handles recursive structures.
 */
public class GridArrangementTest {

    /**
     * Verifies that calling arrangeRF with a BlockContainer that contains itself
     * results in a StackOverflowError. This tests the system's behavior when
     * faced with a recursive block structure, which should be avoided in practice
     * but needs to be handled gracefully (even if by crashing predictably).
     */
    @Test(expected = StackOverflowError.class)
    public void arrangeRFShouldThrowStackOverflowErrorWhenContainerContainsItself() {
        // Arrange: Create a grid arrangement and a block container that contains itself,
        // which creates a recursive structure.
        GridArrangement gridArrangement = new GridArrangement(1, 1);
        BlockContainer selfContainingContainer = new BlockContainer();
        selfContainingContainer.add(selfContainingContainer); // The recursive step

        RectangleConstraint constraint = RectangleConstraint.NONE;
        Graphics2D g2 = null; // Graphics2D is not used in the path to the error.

        // Act: Attempt to arrange the container. This should lead to infinite recursion.
        gridArrangement.arrangeRF(selfContainingContainer, g2, constraint);

        // Assert: The @Test(expected) annotation handles the assertion that a
        // StackOverflowError is thrown. No further assertions are needed.
    }
}