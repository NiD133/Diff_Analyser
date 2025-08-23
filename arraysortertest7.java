package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArraySorter}.
 *
 * Note: The original test class name 'ArraySorterTestTest7' was renamed to 'ArraySorterTest'
 * for improved clarity and adherence to standard naming conventions.
 */
class ArraySorterTest extends AbstractLangTest {

    @Test
    @DisplayName("sort(long[]) should sort the given array in place")
    void sort_shouldSortLongArray_whenArrayIsNotNull() {
        // Arrange
        final long[] array = {2L, 1L, 3L};
        final long[] expected = {1L, 2L, 3L};

        // Act
        final long[] result = ArraySorter.sort(array);

        // Assert
        // Verify the array content is sorted correctly.
        assertArrayEquals(expected, array);
        // Verify the method returns the same array instance, as per its contract.
        assertSame(array, result);
    }

    @Test
    @DisplayName("sort(long[]) should return null when the input array is null")
    void sort_shouldReturnNull_whenArrayIsNull() {
        // Arrange
        final long[] nullArray = null;

        // Act
        final long[] result = ArraySorter.sort(nullArray);

        // Assert
        assertNull(result);
    }
}