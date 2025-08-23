package org.apache.commons.lang3;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Test suite for the {@link ArrayFill} class.
 */
public class ArrayFillTest {

    @Test
    public void testFillByteArray() {
        // Arrange
        final byte[] array = new byte[1];
        final byte fillValue = 76;
        final byte[] expectedArray = {76};

        // Act
        final byte[] resultArray = ArrayFill.fill(array, fillValue);

        // Assert
        // 1. Verify the array is filled with the correct value.
        assertArrayEquals("The array content should be filled with the specified value.", expectedArray, resultArray);

        // 2. Verify the method returns the same array instance, as per the contract.
        assertSame("The method should return the same array instance that was passed in.", array, resultArray);
    }
}