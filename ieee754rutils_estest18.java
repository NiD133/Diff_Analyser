package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that the max() method correctly returns the input value when all
     * three float arguments are identical.
     */
    @Test
    public void testMaxWithThreeEqualFloats() {
        // Arrange
        final float value = -659.8F;
        final float expectedResult = -659.8F;

        // Act
        final float actualResult = IEEE754rUtils.max(value, value, value);

        // Assert
        assertEquals("The maximum of three equal numbers should be the number itself.",
                expectedResult, actualResult, 0.0f);
    }
}