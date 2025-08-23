package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link ArrayFill} class.
 */
public class ArrayFillTest {

    @Test
    public void fillDoubleArray_shouldFillArrayWithGivenValueAndReturnSameInstance() {
        // Arrange
        final double[] inputArray = new double[8];
        final double fillValue = 0.0;
        final double[] expectedArray = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};

        // Act
        final double[] resultArray = ArrayFill.fill(inputArray, fillValue);

        // Assert
        // 1. Verify that the array content is correctly filled.
        assertArrayEquals("The array should be filled with the specified value.", expectedArray, resultArray, 0.0);

        // 2. Verify that the method returns the same array instance for fluent chaining.
        assertSame("The returned array should be the same instance as the input.", inputArray, resultArray);
    }
}