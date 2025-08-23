package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that sorting an empty float array returns the same array instance.
     * The sort operation is performed in-place, and for an empty array,
     * no changes are needed. The fluent API should return the original array reference.
     */
    @Test
    public void sort_shouldReturnSameInstance_whenGivenEmptyFloatArray() {
        // Arrange: Create an empty float array.
        final float[] emptyArray = new float[0];

        // Act: Call the sort method.
        final float[] result = ArraySorter.sort(emptyArray);

        // Assert: Verify that the returned array is the same instance as the input array.
        assertSame("Sorting an empty array should return the same instance", emptyArray, result);
    }
}