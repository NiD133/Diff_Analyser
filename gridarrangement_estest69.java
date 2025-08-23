package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;
import org.jfree.data.Range;

/**
 * Contains an improved test case for the {@link GridArrangement} class.
 * The original test was auto-generated and difficult to comprehend.
 */
public class GridArrangement_ESTestTest69 {

    /**
     * Verifies that the arrange() method throws an IndexOutOfBoundsException when
     * the container holds fewer blocks than the number of cells defined in the grid.
     * The implementation likely attempts to access a block for each grid cell,
     * leading to the exception when the block list is exhausted.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void arrangeShouldThrowExceptionWhenBlockCountIsLessThanGridSize() {
        // Arrange: Create a 2x2 grid arrangement, which expects 4 blocks.
        // Using small, clear numbers makes the test's intent easier to grasp.
        GridArrangement arrangement = new GridArrangement(2, 2);

        // Arrange: Create a container with only one block, which is insufficient for the 2x2 grid.
        BlockContainer containerWithInsufficientBlocks = new BlockContainer();
        containerWithInsufficientBlocks.add(new EmptyBlock(0, 0));

        // Arrange: Define a simple constraint for the arrangement. The specific values are not
        // critical for this test, as the exception is expected to occur before the
        // constraint is fully utilized.
        RectangleConstraint constraint = new RectangleConstraint(new Range(0, 100), new Range(0, 100));

        // Act: Attempt to arrange the container. This call is expected to fail.
        arrangement.arrange(containerWithInsufficientBlocks, (Graphics2D) null, constraint);

        // Assert: The test passes if an IndexOutOfBoundsException is thrown,
        // as declared by the @Test annotation. No further assertions are needed.
    }
}