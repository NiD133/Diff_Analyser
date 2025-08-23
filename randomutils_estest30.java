package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@code RandomUtils.nextLong(start, end)} returns the boundary value
     * when the start and end of the range are identical.
     *
     * <p>This scenario represents a zero-width range, e.g., [n, n), where the only
     * valid return value is n.</p>
     */
    @Test
    public void nextLongShouldReturnBoundWhenStartAndEndAreEqual() {
        // Arrange: Define a range where the start and end boundaries are the same.
        // The specific value is taken from the original auto-generated test.
        final long boundary = 2144936217L;

        // Act: Call the method with the zero-width range.
        final long result = RandomUtils.nextLong(boundary, boundary);

        // Assert: The result must be the boundary value itself.
        assertEquals(boundary, result);
    }
}