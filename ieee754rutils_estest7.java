package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link IEEE754rUtils}.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that IEEE754rUtils.min(float, float) returns the input value
     * when both inputs are identical.
     */
    @Test
    public void minFloat_shouldReturnInput_whenInputsAreEqual() {
        // Arrange
        final float value = 0.0F;
        final float expected = 0.0F;

        // Act
        final float result = IEEE754rUtils.min(value, value);

        // Assert
        // A delta of 0.0F is used to assert an exact match.
        assertEquals(expected, result, 0.0F);
    }
}