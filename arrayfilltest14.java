package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

public class ArrayFillTestTest14 extends AbstractLangTest {

    @Test
    void fillLongArrayShouldFillAllElementsAndReturnSameInstance() {
        // Arrange
        final long[] actualArray = new long[3];
        final long fillValue = 1L;
        final long[] expectedArray = {1L, 1L, 1L};

        // Act
        final long[] resultArray = ArrayFill.fill(actualArray, fillValue);

        // Assert
        // 1. The method should return the same array instance that was passed in.
        assertSame(actualArray, resultArray, "The returned array should be the same instance as the input.");

        // 2. The array's contents should be completely overwritten with the fill value.
        assertArrayEquals(expectedArray, resultArray, "Each element in the array should be set to the fill value.");
    }
}