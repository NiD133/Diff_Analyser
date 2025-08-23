package org.apache.commons.lang3;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@code randomLong(start, end)} returns the lower bound when the
     * range is empty (i.e., start and end are the same). This is an important
     * edge case to verify.
     */
    @Test
    public void randomLongShouldReturnBoundWhenRangeIsEmpty() {
        // Arrange
        final RandomUtils randomUtils = RandomUtils.insecure();
        final long bound = 0L;

        // Act
        // Call the method with an empty range [0, 0).
        final long result = randomUtils.randomLong(bound, bound);

        // Assert
        // For an empty range, the method should consistently return the bound.
        assertEquals(bound, result);
    }
}