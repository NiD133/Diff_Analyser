package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that sorting a null byte array returns null, as specified by the Javadoc.
     */
    @Test
    public void sort_givenNullByteArray_returnsNull() {
        // Arrange: Define a null byte array. The cast is necessary to resolve the
        // correct overloaded sort() method.
        final byte[] inputArray = null;

        // Act: Call the sort method with the null array.
        final byte[] result = ArraySorter.sort(inputArray);

        // Assert: Verify that the result is null.
        assertNull("Sorting a null array should return null.", result);
    }
}