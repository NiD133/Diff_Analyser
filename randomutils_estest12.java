package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link RandomUtils} class.
 */
public class RandomUtilsTest {

    /**
     * Verifies that {@code RandomUtils.secureStrong().randomLong(start, end)}
     * generates a long value within the expected range [start, end).
     */
    @Test
    public void secureStrongRandomLongReturnsValueInCorrectRange() {
        // Arrange
        final long startInclusive = 0L;
        final long endExclusive = 452L;
        final RandomUtils randomUtils = RandomUtils.secureStrong();

        // Act
        // Generate a random long within the specified bounds.
        final long generatedLong = randomUtils.randomLong(startInclusive, endExclusive);

        // Assert
        // A random number cannot be asserted to be a specific value.
        // Instead, we verify that it falls within the specified boundaries.
        assertTrue(
            "The generated long should be greater than or equal to the start boundary. " +
            "Expected >= " + startInclusive + ", but was " + generatedLong,
            generatedLong >= startInclusive
        );
        assertTrue(
            "The generated long should be less than the exclusive end boundary. " +
            "Expected < " + endExclusive + ", but was " + generatedLong,
            generatedLong < endExclusive
        );
    }
}