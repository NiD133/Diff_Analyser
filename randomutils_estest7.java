package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@code RandomUtils.nextFloat(start, end)} returns the start value
     * when the start and end of the range are identical. This effectively tests
     * the behavior for an empty range [start, start).
     */
    @Test
    public void testNextFloatWithEmptyRangeReturnsStartValue() {
        // Arrange: Define an empty range where start and end are the same.
        final float rangeBoundary = 0.0F;

        // Act: Call the method with the empty range.
        final float result = RandomUtils.nextFloat(rangeBoundary, rangeBoundary);

        // Assert: The result should be exactly the start value.
        assertEquals(rangeBoundary, result, 0.0F);
    }
}