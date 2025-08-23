package org.apache.commons.lang3;

import static org.junit.Assert.assertSame;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that sorting an empty array returns the same array instance,
     * as the sort operation should be performed in-place.
     */
    @Test
    public void sort_withEmptyArray_returnsSameInstance() {
        // Arrange: Create an empty array.
        final Integer[] emptyArray = new Integer[0];

        // Act: Call the sort method.
        final Integer[] result = ArraySorter.sort(emptyArray);

        // Assert: The returned array should be the same instance as the input.
        assertSame("Sorting an empty array should return the same instance.", emptyArray, result);
    }
}