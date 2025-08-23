package org.jfree.chart.block;

import org.jfree.chart.util.Size2D;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the {@link GridArrangement} class.
 */
public class GridArrangementTest {

    /**
     * Verifies that arranging an empty container with a fixed-size constraint
     * returns a Size2D object that matches the constraint's dimensions, even
     * when those dimensions are negative.
     */
    @Test
    public void arrange_withEmptyContainerAndFixedNegativeConstraint_shouldReturnConstraintSize() {
        // Arrange: Set up the test objects and preconditions.
        // The grid dimensions are arbitrary since the container is empty.
        GridArrangement gridArrangement = new GridArrangement(10, 20);
        BlockContainer emptyContainer = new BlockContainer(gridArrangement);

        // Define a constraint with fixed negative width and height.
        final double expectedWidth = -900.0;
        final double expectedHeight = -2.147483648E9; // A large negative value.
        RectangleConstraint fixedNegativeConstraint = new RectangleConstraint(expectedWidth, expectedHeight);

        // Act: Call the method under test.
        // The Graphics2D object is not used in this arrangement scenario.
        Size2D resultSize = gridArrangement.arrange(emptyContainer, null, fixedNegativeConstraint);

        // Assert: Verify the outcome.
        assertNotNull("The resulting size should not be null.", resultSize);
        assertEquals("The resulting width should match the fixed constraint width.",
                expectedWidth, resultSize.getWidth(), 0.01);
        assertEquals("The resulting height should match the fixed constraint height.",
                expectedHeight, resultSize.getHeight(), 0.01);
    }
}