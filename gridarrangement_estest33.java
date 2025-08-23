package org.jfree.chart.block;

import org.jfree.chart.ui.Size2D;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.Graphics2D;

/**
 * Contains tests for the {@link GridArrangement} class, focusing on its arrangement logic.
 */
public class GridArrangementTest {

    /**
     * Verifies that arranging an empty container with a fixed-size constraint
     * results in a size equal to the constraint's dimensions.
     * This test specifically checks the `arrangeFF` method, which handles
     * fixed-width and fixed-height scenarios. It also confirms that a negative
     * height in the constraint is correctly propagated.
     */
    @Test
    public void arrangeFFWithEmptyContainerShouldReturnConstraintSize() {
        // Arrange: Set up the test objects and preconditions.
        GridArrangement arrangement = new GridArrangement(81, 81);
        BlockContainer emptyContainer = new BlockContainer();

        // Define a constraint with a fixed width and a negative height to test edge cases.
        final double expectedWidth = 81.0;
        final double expectedNegativeHeight = -2677.7593688945217;
        RectangleConstraint constraint = new RectangleConstraint(expectedWidth, expectedNegativeHeight);

        // Act: Execute the method under test.
        // We pass null for Graphics2D as it is not used when the container is empty.
        Size2D resultSize = arrangement.arrangeFF(emptyContainer, (Graphics2D) null, constraint);

        // Assert: Verify the outcome.
        assertNotNull("The resulting size should not be null.", resultSize);
        assertEquals("The resulting width should match the constraint's width.",
                expectedWidth, resultSize.getWidth(), 0.01);
        assertEquals("The resulting height should match the constraint's height.",
                expectedNegativeHeight, resultSize.getHeight(), 0.01);
    }
}