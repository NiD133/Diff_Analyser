package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    @Test
    public void testFillLongArray() {
        // Arrange
        final long[] inputArray = new long[8];
        final long[] expectedArray = {-1L, -1L, -1L, -1L, -1L, -1L, -1L, -1L};

        // Act
        final long[] resultArray = ArrayFill.fill(inputArray, -1L);

        // Assert
        // The method should fill the array in place and return the same instance.
        assertSame("The returned array should be the same instance as the input.", inputArray, resultArray);
        assertArrayEquals("The array elements should be filled with the specified value.", expectedArray, resultArray);
    }
}