package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that sorting a null array returns null, as specified by the Javadoc.
     * This test covers the int[] overload of the sort method.
     */
    @Test
    public void testSort_withNullIntArray_shouldReturnNull() {
        // Arrange: Define the input, which is a null array.
        // The variable's type ensures the correct overloaded `sort(int[])` method is called.
        final int[] inputArray = null;

        // Act: Call the method under test.
        final int[] resultArray = ArraySorter.sort(inputArray);

        // Assert: Verify that the result is null, as expected.
        assertNull("Sorting a null array must return null.", resultArray);
    }
}