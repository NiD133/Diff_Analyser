package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ArraySorter}.
 *
 * Note: The original test class name "ArraySorter_ESTestTest4" is an artifact
 * of a test generation tool. A more conventional name would be "ArraySorterTest".
 */
public class ArraySorter_ESTestTest4 {

    /**
     * Tests that sorting an empty long array returns the same array instance.
     * The sort operation should be a no-op for an empty array.
     */
    @Test
    public void whenSortingEmptyLongArray_thenReturnsSameInstance() {
        // Arrange: Create an empty long array.
        final long[] emptyArray = new long[0];

        // Act: Call the sort method.
        final long[] sortedArray = ArraySorter.sort(emptyArray);

        // Assert: Verify that the method returned the exact same object instance.
        // According to the contract, the array is sorted in-place and returned.
        assertSame("Sorting an empty array should return the same instance.", emptyArray, sortedArray);
    }
}