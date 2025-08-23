package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for the {@link ArraySorter} class.
 */
public class ArraySorterTest {

    /**
     * Tests that {@code ArraySorter.sort(long[])} correctly handles a null input
     * by returning null, as specified by its API contract.
     */
    @Test
    public void sort_withNullLongArray_shouldReturnNull() {
        // The Javadoc for ArraySorter.sort() states that a null input array
        // should result in a null output. This test verifies that behavior.

        // The explicit cast to (long[]) is necessary here to resolve ambiguity
        // between the multiple overloaded sort() methods that accept array types.
        long[] result = ArraySorter.sort((long[]) null);

        assertNull("Passing a null array to sort should return null.", result);
    }
}