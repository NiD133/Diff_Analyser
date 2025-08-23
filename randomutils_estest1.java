package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@code randomFloat(start, end)} returns a value within the specified range.
     *
     * <p>Because the output is random, this test cannot assert a specific value.
     * Instead, it verifies that the generated float correctly falls within the expected bounds
     * of [startInclusive, endExclusive).</p>
     */
    @Test
    public void randomFloat_shouldReturnFloatWithinGivenRange() {
        // Arrange
        final float startInclusive = 1.0F;
        final float endExclusive = 1797.5656F;
        final RandomUtils randomUtils = RandomUtils.secure();

        // Act
        final float result = randomUtils.randomFloat(startInclusive, endExclusive);

        // Assert
        // The result must be greater than or equal to the start boundary.
        assertTrue(
            "The generated float " + result + " should be >= " + startInclusive,
            result >= startInclusive
        );
        // The result must be less than the exclusive end boundary.
        assertTrue(
            "The generated float " + result + " should be < " + endExclusive,
            result < endExclusive
        );
    }
}