package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link ArraySorter} class.
 */
public class ArraySorterTest {

    /**
     * Tests that sorting an int array that is already sorted (containing only zeros)
     * does not change its contents. It also verifies that the method returns the
     * same array instance it was given, fulfilling its API contract.
     */
    @Test
    public void testSortIntArray_withAllZeros_returnsSameInstanceUnchanged() {
        // Arrange: Create an array of integers. By default, it's filled with zeros,
        // so it's already in sorted order.
        final int[] array = new int[7];
        final int[] expected = {0, 0, 0, 0, 0, 0, 0};

        // Act: Call the sort method on the already-sorted array.
        final int[] sortedArray = ArraySorter.sort(array);

        // Assert: Verify the array's contents are unchanged and that the
        // returned object is the same instance as the input.
        assertArrayEquals("The array contents should remain a sequence of zeros.", expected, sortedArray);
        assertSame("The method should return the same array instance.", array, sortedArray);
    }
}