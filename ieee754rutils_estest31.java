package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link IEEE754rUtils}.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that IEEE754rUtils.max(float, float) returns the correct value
     * when both arguments are equal.
     */
    @Test
    public void testMaxFloatWithEqualValues() {
        // Arrange
        final float valueA = 0.0F;
        final float valueB = 0.0F;
        final float expected = 0.0F;

        // Act
        final float actual = IEEE754rUtils.max(valueA, valueB);

        // Assert
        // The maximum of two equal numbers should be the number itself.
        // A delta of 0.0f is used to assert an exact match.
        assertEquals(expected, actual, 0.0f);
    }
}