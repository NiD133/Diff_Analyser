package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@code RandomUtils.nextInt(start, end)} returns the boundary value
     * when the start and end of the range are identical.
     *
     * <p>This tests an edge case where the exclusive upper bound is equal to the
     * inclusive lower bound, resulting in an empty range [n, n). The expected
     * behavior is to return the boundary value itself.</p>
     */
    @Test
    @SuppressWarnings("deprecation") // Intentionally testing deprecated method for backward compatibility.
    public void testNextIntShouldReturnBoundaryWhenBoundsAreEqual() {
        // Arrange: Define a boundary value to test.
        final int boundary = 404;
        
        // Act: Call the method with the start and end bounds being the same.
        final int result = RandomUtils.nextInt(boundary, boundary);
        
        // Assert: The method should return the boundary value.
        assertEquals("For a range [n, n), the result should be n", boundary, result);
    }
}