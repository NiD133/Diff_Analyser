package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that sorting an empty array returns the same array instance.
     * This verifies the fluent API design for this edge case, where the
     * input array is modified in-place and returned.
     */
    @Test
    public void testSortEmptyIntArrayReturnsSameInstance() {
        // Arrange: Create an empty integer array.
        final int[] emptyArray = new int[0];

        // Act: Call the sort method.
        final int[] result = ArraySorter.sort(emptyArray);

        // Assert: The returned array should be the exact same instance as the input array.
        assertSame("Sorting an empty array should return the same instance.", emptyArray, result);
    }
}