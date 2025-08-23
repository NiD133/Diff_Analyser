package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;

import org.junit.Test;

/**
 * Tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    @Test
    public void fillFloatArrayShouldReturnNullWhenArrayIsNull() {
        // The fill method is expected to be null-safe.
        // When a null array is passed, it should return null.
        
        // Arrange
        final float[] inputArray = null;
        
        // Act
        final float[] resultArray = ArrayFill.fill(inputArray, 1.0f);
        
        // Assert
        assertNull("The result should be null for a null input array.", resultArray);
    }
}