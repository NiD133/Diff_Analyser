package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link RandomUtils#nextLong(long, long)}.
 */
// The original class name and inheritance are kept to match the request.
public class RandomUtils_ESTestTest32 extends RandomUtils_ESTest_scaffolding {

    /**
     * Tests that nextLong(start, end) generates a value within the specified bounds.
     * The range is [startInclusive, endExclusive).
     */
    @Test
    public void nextLongWithRangeShouldReturnResultWithinBounds() {
        // Arrange: Define the boundaries for the random number generation.
        final long startInclusive = 1L;
        final long endExclusive = 6590828120745478464L;

        // Act: Generate a random long within the specified range.
        final long result = RandomUtils.nextLong(startInclusive, endExclusive);

        // Assert: Verify that the generated number is within the expected range.
        // The result must be greater than or equal to the start value...
        assertTrue(
            "The generated long " + result + " should be >= " + startInclusive,
            result >= startInclusive
        );
        // ...and strictly less than the end value.
        assertTrue(
            "The generated long " + result + " should be < " + endExclusive,
            result < endExclusive
        );
    }
}