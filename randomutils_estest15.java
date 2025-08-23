package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link RandomUtils} class.
 * This test focuses on verifying the range contract of the {@code randomInt(int, int)} method.
 */
public class RandomUtilsTest {

    /**
     * Verifies that randomInt(start, end) generates an integer that falls
     * within the specified inclusive start and exclusive end bounds.
     *
     * The original auto-generated test had an unstable assertion for a specific random value.
     * This version correctly tests the method's contract by checking if the result
     * is within the expected range.
     */
    @Test
    public void randomInt_shouldReturnIntegerWithinGivenRange() {
        // Arrange
        final int startInclusive = 228;
        final int endExclusive = Integer.MAX_VALUE;
        
        // Use the recommended factory method instead of the deprecated constructor.
        // The original `new RandomUtils()` defaults to the strong secure random generator.
        final RandomUtils randomUtils = RandomUtils.secureStrong();

        // Act
        final int generatedNumber = randomUtils.randomInt(startInclusive, endExclusive);

        // Assert
        // The result must be within the range [startInclusive, endExclusive).
        assertTrue(
            "The generated integer (" + generatedNumber + ") must be greater than or equal to the start bound (" + startInclusive + ").",
            generatedNumber >= startInclusive
        );
        assertTrue(
            "The generated integer (" + generatedNumber + ") must be less than the end bound (" + endExclusive + ").",
            generatedNumber < endExclusive
        );
    }
}