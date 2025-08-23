package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link RandomUtils} class.
 * This class demonstrates an improved version of an auto-generated test.
 */
public class RandomUtilsImprovedTest {

    /**
     * Verifies that randomDouble() from a secure RandomUtils instance
     * generates a value within the specified inclusive start and exclusive end bounds.
     */
    @Test
    public void testSecureRandomDoubleReturnsValueWithinSpecifiedRange() {
        // Arrange
        final double startInclusive = 117.68;
        final double endExclusive = 2958.43561;
        final RandomUtils randomUtils = RandomUtils.secure();

        // Act
        final double result = randomUtils.randomDouble(startInclusive, endExclusive);

        // Assert
        // The result must be within the defined range [startInclusive, endExclusive).
        assertTrue(
            "The generated double (" + result + ") should be greater than or equal to the start value (" + startInclusive + ").",
            result >= startInclusive
        );
        assertTrue(
            "The generated double (" + result + ") should be less than the end value (" + endExclusive + ").",
            result < endExclusive
        );
    }
}