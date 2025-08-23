package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link RandomUtils} class.
 */
public class RandomUtilsTest {

    /**
     * Tests that {@code nextFloat} returns the boundary value when the start and end bounds are identical.
     * This is an edge case where the range of possible random values is zero.
     */
    @Test
    public void testNextFloat_shouldReturnBound_whenStartAndEndBoundsAreEqual() {
        // Arrange
        final float equalBound = 1.0F;

        // Act
        // Call the method with the same value for the start and end of the range.
        final float result = RandomUtils.nextFloat(equalBound, equalBound);

        // Assert
        // The result must be exactly the boundary value, as it's the only possible outcome.
        assertEquals(equalBound, result, 0.0F);
    }
}