package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that sorting an empty array is a no-op and returns the same array instance.
     * This verifies the handling of an edge case.
     */
    @Test
    public void sort_withEmptyShortArray_returnsSameInstance() {
        // Arrange: Create an empty short array.
        final short[] emptyArray = new short[0];

        // Act: Call the sort method.
        final short[] resultArray = ArraySorter.sort(emptyArray);

        // Assert: The returned array should be the same instance as the input array.
        // The method is expected to sort in-place and return the same reference.
        assertSame("Sorting an empty array should return the same instance", emptyArray, resultArray);
    }
}