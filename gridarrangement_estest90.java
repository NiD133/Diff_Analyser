package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * Tests for the {@link GridArrangement} class, focusing on its handling of
 * invalid or edge-case inputs.
 */
public class GridArrangementTest {

    /**
     * Verifies that the arrangeRF method throws a NullPointerException if the
     * container it is arranging holds a null block. The arrangement logic is
     * expected to iterate over blocks, and a null entry should cause a failure.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeRFShouldThrowNPEWhenContainerContainsNullBlock() {
        // Arrange: Create a grid arrangement and a container with a null block.
        // The grid dimensions (1x1) are arbitrary and don't affect this test's outcome.
        GridArrangement arrangement = new GridArrangement(1, 1);
        BlockContainer container = new BlockContainer();
        container.add((Block) null);
        RectangleConstraint constraint = RectangleConstraint.NONE;

        // Act: Attempt to arrange the container. This call is expected to fail.
        arrangement.arrangeRF(container, (Graphics2D) null, constraint);

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // by the @Test(expected=...) annotation.
    }
}