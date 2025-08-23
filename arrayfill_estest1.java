package org.apache.commons.lang3;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.ArrayFill}.
 */
public class ArrayFillTest {

    @Test
    public void testFill_withEmptyBooleanArray_shouldReturnSameInstance() {
        // Arrange: Create an empty boolean array.
        final boolean[] emptyArray = new boolean[0];

        // Act: Call the fill method. The value 'true' is arbitrary as the array is empty.
        final boolean[] result = ArrayFill.fill(emptyArray, true);

        // Assert: The method should return the exact same array instance.
        // This is crucial for the class's fluent API design.
        assertSame("The returned array should be the same instance as the input", emptyArray, result);
    }
}