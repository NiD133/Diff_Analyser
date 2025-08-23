package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.ArraySorter}.
 * 
 * Note: This class demonstrates the improvement of a single test case. In a real-world
 * scenario, it would contain multiple tests for the ArraySorter class.
 */
public class ArraySorterTest {

    /**
     * Tests that {@code ArraySorter.sort(float[])} correctly handles a null input array
     * by returning null, as specified by its contract.
     */
    @Test
    public void testSortFloatArrayWithNullInput() {
        // Arrange: The input array is explicitly null.
        final float[] inputArray = null;

        // Act: Call the sort method with the null array.
        final float[] result = ArraySorter.sort(inputArray);

        // Assert: Verify that the method returns null.
        assertNull("Sorting a null array should return null.", result);
    }
}