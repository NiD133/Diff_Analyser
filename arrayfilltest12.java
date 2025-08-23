package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayFill}.
 */
class ArrayFillTest {

    @Test
    void fill_withIntArray_shouldFillArrayWithGivenValueAndReturnSameInstance() {
        // Arrange
        final int[] originalArray = new int[3];
        final int valueToFill = 1;
        final int[] expectedArray = {1, 1, 1};

        // Act
        final int[] resultArray = ArrayFill.fill(originalArray, valueToFill);

        // Assert
        // 1. The method should return the same array instance that was passed in.
        assertSame(originalArray, resultArray, "The method should return the same array instance.");

        // 2. The array's contents should be updated with the new value.
        assertArrayEquals(expectedArray, resultArray, "All elements in the array should be filled with the specified value.");
    }
}