package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link IEEE754rUtils}.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that IEEE754rUtils.max(float, float) returns the input value
     * when both arguments are identical.
     */
    @Test
    public void maxFloat_shouldReturnArgument_whenBothArgumentsAreEqual() {
        // Arrange
        final float identicalValue = -2157.9656F;

        // Act
        final float result = IEEE754rUtils.max(identicalValue, identicalValue);

        // Assert
        // The maximum of two identical numbers should be the number itself.
        // A delta of 0.0f is used because the result should be exact.
        assertEquals(identicalValue, result, 0.0f);
    }
}