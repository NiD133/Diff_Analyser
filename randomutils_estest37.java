package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@code RandomUtils.nextDouble(start, end)} returns the lower bound
     * when the start and end bounds are identical. This verifies the behavior
     * for a zero-sized range, which is an important edge case.
     */
    @Test
    public void nextDoubleShouldReturnBoundWhenRangeIsZero() {
        // Arrange: Define a representative boundary value. The specific value is not
        // important, only that the start and end bounds are the same.
        final double bound = 123.456;

        // Act: Call the method with identical start and end bounds.
        final double result = RandomUtils.nextDouble(bound, bound);

        // Assert: The result must be exactly the boundary value.
        // A delta of 0.0 is used for an exact floating-point comparison, as no
        // random calculation should occur in this case.
        assertEquals(bound, result, 0.0);
    }
}