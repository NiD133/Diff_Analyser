package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link org.apache.commons.lang3.ArrayFill}.
 */
public class ArrayFillTest {

    /**
     * Tests that the fill(char[], char) method gracefully handles a null input array
     * by returning null, as specified in its contract.
     */
    @Test
    public void fill_withNullCharArray_shouldReturnNull() {
        // Arrange: Define the inputs for the test case.
        // The method contract allows for a null array.
        final char[] inputArray = null;
        final char valueToFill = 'B';

        // Act: Execute the method under test.
        final char[] resultArray = ArrayFill.fill(inputArray, valueToFill);

        // Assert: Verify that the actual output matches the expected outcome.
        assertNull("The result of filling a null array should be null.", resultArray);
    }
}