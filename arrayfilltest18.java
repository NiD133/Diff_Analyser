package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayFill}.
 * This class name is kept for consistency with the original problem statement.
 */
public class ArrayFillTestTest18 extends AbstractLangTest {

    /**
     * Tests that {@link ArrayFill#fill(short[], short)} correctly fills the array
     * with the specified value and returns the same array instance.
     */
    @Test
    void fill_withShortArray_shouldFillArrayAndReturnSameInstance() {
        // Arrange
        final short[] inputArray = new short[3];
        final short fillValue = 1;
        final short[] expectedArray = {1, 1, 1};

        // Act
        final short[] resultArray = ArrayFill.fill(inputArray, fillValue);

        // Assert
        // 1. Verify that the array contents are correct.
        assertArrayEquals(expectedArray, resultArray, "The array should be filled with the specified value.");

        // 2. Verify that the method returns the same array instance (fluent API contract).
        assertSame(inputArray, resultArray, "The returned array should be the same instance as the input array.");
    }
}