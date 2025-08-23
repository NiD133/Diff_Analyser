package org.jfree.chart.block;

import org.junit.Test;
import java.awt.Graphics2D;

/**
 * Tests for the {@link GridArrangement} class, focusing on exception handling.
 */
public class GridArrangementTest {

    /**
     * Verifies that the arrangeFR method throws a NullPointerException when
     * passed a null RectangleConstraint. The arrangeFR method is protected,
     * but its behavior with null arguments is important to validate.
     */
    @Test(expected = NullPointerException.class)
    public void arrangeFRShouldThrowNullPointerExceptionForNullConstraint() {
        // Arrange: Create a GridArrangement and a container for it.
        GridArrangement arrangement = new GridArrangement(2, 2);
        BlockContainer container = new BlockContainer(arrangement);

        // Act: Call arrangeFR with null arguments.
        // This is expected to throw a NullPointerException because the constraint is null.
        arrangement.arrangeFR(container, null, null);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}