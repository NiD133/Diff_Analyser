package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link org.apache.commons.lang3.RandomUtils}.
 */
public class RandomUtilsTest {

    @Test
    public void randomDouble_withEqualBounds_shouldReturnTheBoundValue() {
        // Arrange
        final RandomUtils randomUtils = RandomUtils.insecure();
        final double bound = 0.0;

        // Act
        final double result = randomUtils.randomDouble(bound, bound);

        // Assert
        // For a zero-width range, the result must be exactly the boundary value.
        final double delta = 0.0;
        assertEquals("For a range of [0.0, 0.0), the result must be 0.0.", bound, result, delta);
    }
}