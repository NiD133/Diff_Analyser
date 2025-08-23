package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that IEEE754rUtils.min() returns the correct value when all three
     * double arguments are identical.
     */
    @Test
    public void min_shouldReturnTheValue_whenAllDoubleInputsAreEqual() {
        // Arrange
        final double value = 1.0;
        final double expected = 1.0;

        // Act
        final double actual = IEEE754rUtils.min(value, value, value);

        // Assert
        assertEquals(expected, actual, 0.0);
    }
}