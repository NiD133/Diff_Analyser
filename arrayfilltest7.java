package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArrayFill}.
 */
// The class name was improved from the generated "ArrayFillTestTest7" to the more standard "ArrayFillTest".
public class ArrayFillTest extends AbstractLangTest {

    @Test
    void testFill_withDoubleArray_fillsArrayAndReturnsSameInstance() {
        // Arrange: Define the input array, the value to fill it with, and the expected result.
        final double[] arrayToFill = new double[3];
        final double fillValue = 1.0;
        final double[] expectedArray = {1.0, 1.0, 1.0};

        // Act: Call the method under test.
        final double[] resultArray = ArrayFill.fill(arrayToFill, fillValue);

        // Assert: Verify both the content and the identity of the returned array.
        // 1. Check that the method returns the same array instance for fluent chaining.
        assertSame(arrayToFill, resultArray, "The method should return the same array instance.");

        // 2. Check that the array was correctly filled with the specified value.
        assertArrayEquals(expectedArray, resultArray, "The array content should match the expected values.");
    }
}