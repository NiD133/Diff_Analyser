package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * A test suite for the {@link GridArrangement} class, focusing on exception handling
 * and edge cases in its arrangement logic.
 */
public class GridArrangementTest {

    /**
     * Verifies that the protected arrangeRF method throws a NullPointerException
     * when called with a null Graphics2D context.
     *
     * Note: This test calls a protected method directly to isolate its behavior.
     * The public arrange() method would normally delegate to this method under
     * specific constraint conditions.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeRFShouldThrowNullPointerExceptionForNullGraphics() {
        // Arrange: Set up a GridArrangement and a container with a single block.
        GridArrangement arrangement = new GridArrangement(5, 5);
        BlockContainer container = new BlockContainer(arrangement);
        
        // The container must contain at least one block for the arrangement logic to run.
        container.add(new EmptyBlock(10, 10));

        // The constraint object is a required parameter for the method, but its specific
        // values are not relevant for this null-check test.
        RectangleConstraint constraint = RectangleConstraint.NONE;

        // Act & Assert: Call the method under test with a null Graphics2D object.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        arrangement.arrangeRF(container, null, constraint);
    }
}