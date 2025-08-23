package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArraySorter}.
 */
class ArraySorterTest extends AbstractLangTest {

    @Test
    void sort_shouldSortShortArrayInAscendingOrder() {
        // Given an unsorted array
        final short[] array = {2, 1, 3};
        final short[] expectedSortedArray = {1, 2, 3};

        // When the array is sorted
        final short[] actualSortedArray = ArraySorter.sort(array);

        // Then the array should be sorted correctly
        assertArrayEquals(expectedSortedArray, actualSortedArray);
    }

    @Test
    void sort_shouldReturnSameArrayInstanceForShortArray() {
        // Given an array
        final short[] array = {2, 1};

        // When the array is sorted
        final short[] result = ArraySorter.sort(array);

        // Then the method should return the same array instance (fluent API)
        assertSame(array, result, "The method should return the same instance that was passed in.");
    }

    @Test
    void sort_shouldReturnNullWhenGivenNullShortArray() {
        // When a null array is sorted
        final short[] result = ArraySorter.sort((short[]) null);

        // Then the result should be null
        assertNull(result);
    }
}