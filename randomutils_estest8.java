package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link RandomUtils} class.
 */
public class RandomUtilsTest {

    /**
     * Tests the behavior of {@code RandomUtils.nextDouble(start, end)} for the edge case
     * where the start and end bounds are identical.
     *
     * <p>When the range is effectively empty (e.g., [0.0, 0.0)), the method should
     * consistently return the boundary value.</p>
     */
    @Test
    public void nextDoubleShouldReturnBoundaryWhenRangeIsEmpty() {
        // Arrange: Define the start and end of the range to be the same value.
        // This creates an "empty" range [0.0, 0.0).
        final double boundary = 0.0;

        // Act: Call the method under test.
        final double result = RandomUtils.nextDouble(boundary, boundary);

        // Assert: The result must be equal to the boundary value.
        // A delta of 0.0 ensures an exact match for the double comparison.
        assertEquals(boundary, result, 0.0);
    }
}