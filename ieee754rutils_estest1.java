package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that {@link IEEE754rUtils#min(float, float, float)} correctly returns the smallest
     * of three distinct float values.
     */
    @Test
    public void testMinFloatShouldReturnSmallestOfThreeValues() {
        // Arrange
        final float value1 = 0.0F;
        final float value2 = 629.0559F;
        final float smallestValue = -161.0F;
        
        // Act
        final float result = IEEE754rUtils.min(value1, value2, smallestValue);

        // Assert
        assertEquals(smallestValue, result, 0.0F);
    }
}