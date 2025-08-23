package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link org.apache.commons.lang3.ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that ArraySorter.sort(short[]) correctly sorts an array of shorts
     * in ascending order and performs the sort in-place.
     */
    @Test
    public void testSortShortArrayInPlace() {
        // Arrange: Create an unsorted array and define its expected state after sorting.
        final short[] actual = {5, 2, 8, 1};
        final short[] expected = {1, 2, 5, 8};

        // Act: Call the method under test.
        final short[] result = ArraySorter.sort(actual);

        // Assert: Verify the results.
        // 1. The array's contents should be sorted correctly.
        assertArrayEquals(expected, result);

        // 2. The method should return the same array instance that was passed in,
        // confirming the sort was done in-place.
        assertSame(actual, result);
    }
}