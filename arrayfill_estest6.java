package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that `ArrayFill.fill()` correctly populates an integer array
     * with a given value and returns the same array instance.
     */
    @Test
    public void fill_withIntArray_fillsArrayAndReturnsSameInstance() {
        // Arrange
        final int[] originalArray = new int[7];
        final int fillValue = -1;
        final int[] expectedArray = {-1, -1, -1, -1, -1, -1, -1};

        // Act
        final int[] resultArray = ArrayFill.fill(originalArray, fillValue);

        // Assert
        // 1. Verify the method returns the same array instance passed as input.
        assertSame("The method should return the same array instance.", originalArray, resultArray);

        // 2. Verify the array's contents were correctly modified.
        assertArrayEquals("Each element in the array should be set to the fill value.", expectedArray, resultArray);
    }
}