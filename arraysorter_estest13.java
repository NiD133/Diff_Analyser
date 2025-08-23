package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNull;

import java.util.Comparator;

/**
 * Tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that ArraySorter.sort() returns null when the input array is null,
     * even when a comparator is provided.
     */
    @Test
    public void testSortWithComparatorHandlesNullArray() {
        // The behavior should be to return null for a null array, regardless of the comparator.
        // We pass a null comparator as it's a valid and simple choice.
        final Comparator<String> nullComparator = null;

        // Act: Call the sort method with a null array.
        // The cast `(String[]) null` is necessary to resolve the correct method overload.
        final String[] result = ArraySorter.sort((String[]) null, nullComparator);

        // Assert: Verify that the result is null.
        assertNull("Sorting a null array should return null", result);
    }
}