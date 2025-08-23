package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that ArrayFill.fill() correctly handles a null input array
     * by returning null.
     */
    @Test
    public void testFill_withNullShortArray_shouldReturnNull() {
        // Arrange: Define a null input array. The fill value is arbitrary.
        final short[] inputArray = null;
        final short fillValue = 123;

        // Act: Call the method under test with the null array.
        final short[] result = ArrayFill.fill(inputArray, fillValue);

        // Assert: Verify that the method returns null as expected.
        assertNull("The result of filling a null array should be null", result);
    }
}