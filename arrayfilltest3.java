package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

// The original class name 'ArrayFillTestTest3' and base class 'AbstractLangTest' are preserved.
public class ArrayFillTestTest3 extends AbstractLangTest {

    @Test
    void fillForByteArrayShouldModifyAndReturnSameInstance() {
        // Arrange
        final byte[] inputArray = new byte[3];
        final byte fillValue = 1;
        final byte[] expectedArray = {1, 1, 1};

        // Act
        final byte[] resultArray = ArrayFill.fill(inputArray, fillValue);

        // Assert
        // The method contract specifies returning the same array instance to support a fluent style.
        assertSame(inputArray, resultArray, "The method should return the same array instance.");

        // The primary function is to fill the array with the specified value.
        assertArrayEquals(expectedArray, resultArray, "All elements should be set to the fill value.");
    }
}