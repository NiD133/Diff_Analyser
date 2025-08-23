package org.joda.time.field;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the utility methods in {@link FieldUtils}.
 */
public class FieldUtilsTest {

    /**
     * Tests that safeMultiply correctly handles multiplication by 1 with Long.MIN_VALUE.
     * <p>
     * The product of 1 and Long.MIN_VALUE is Long.MIN_VALUE itself, which is a valid
     * long value and should not cause an overflow exception. This test verifies that
     * this specific edge case is handled correctly.
     */
    @Test
    public void testSafeMultiply_withOneAndLongMinValue_shouldReturnLongMinValue() {
        // Arrange
        long value = Long.MIN_VALUE;
        long multiplier = 1L;
        
        // Act
        long result = FieldUtils.safeMultiply(value, multiplier);
        
        // Assert
        assertEquals(Long.MIN_VALUE, result);
    }
}