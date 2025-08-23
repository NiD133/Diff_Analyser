package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link RandomUtils} class.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@code RandomUtils.nextLong(start, end)} returns the start value
     * when the start and end of the range are the same. This tests the behavior
     * for a zero-width range, e.g., [0, 0), where the upper bound is exclusive.
     */
    @Test
    public void nextLong_withIdenticalBounds_shouldReturnLowerBound() {
        // Define the boundary for the zero-width range [0, 0).
        final long boundary = 0L;

        // When the start and end values are the same, the exclusive upper bound
        // means the range is effectively empty. The method should return the
        // inclusive start value.
        final long result = RandomUtils.nextLong(boundary, boundary);

        assertEquals("For a zero-width range, the result should be the boundary value.", boundary, result);
    }
}