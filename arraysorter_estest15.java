package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for {@link org.apache.commons.lang3.ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that {@link ArraySorter#sort(Object[])} handles a null input array
     * by returning null, as specified by its Javadoc contract.
     */
    @Test
    public void testSortWithNullObjectArrayShouldReturnNull() {
        // Act: Call the sort method with a null array.
        // The cast to (Object[]) is necessary to resolve ambiguity between the overloaded sort() methods.
        final Object[] sortedArray = ArraySorter.sort((Object[]) null);

        // Assert: Verify that the result is null.
        assertNull("Sorting a null array should return null.", sortedArray);
    }
}