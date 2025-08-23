package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for {@link ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that ArrayFill.fill() returns null when the input long array is null.
     * This is the specified behavior for handling null inputs.
     */
    @Test
    public void testFill_withNullLongArray_shouldReturnNull() {
        // Arrange: Define a null input array. The fill value is arbitrary for this test.
        final long[] inputArray = null;
        final long valueToFill = 123L;

        // Act: Call the method under test with the null array.
        final long[] result = ArrayFill.fill(inputArray, valueToFill);

        // Assert: Verify that the method returns null as expected.
        assertNull("The method should return null when the input array is null.", result);
    }
}