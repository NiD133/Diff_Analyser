package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArraySorter}.
 *
 * The original test class 'ArraySorterTestTest2' was renamed for clarity and
 * adherence to standard naming conventions. The single test method was split
 * into two separate, focused tests: one for the valid sorting case and one
 * for null input handling.
 */
class ArraySorterTest extends AbstractLangTest {

    @Test
    @DisplayName("sort(char[]) should correctly sort a character array")
    void sort_shouldCorrectlySortCharArray() {
        // Arrange
        // Create an unsorted array and a corresponding sorted version for comparison.
        // Using Arrays.sort() on a clone establishes a reliable "expected" result,
        // ensuring ArraySorter behaves consistently with the standard library.
        final char[] unsortedArray = {'c', 'a', 'b'};
        final char[] expectedSortedArray = unsortedArray.clone();
        Arrays.sort(expectedSortedArray);

        // Act
        final char[] actualSortedArray = ArraySorter.sort(unsortedArray);

        // Assert
        assertArrayEquals(expectedSortedArray, actualSortedArray, "The array should be sorted in ascending order.");
    }

    @Test
    @DisplayName("sort(char[]) should return null for a null input array")
    void sort_shouldReturnNullForNullCharArray() {
        // Arrange
        final char[] nullArray = null;

        // Act
        final char[] result = ArraySorter.sort(nullArray);

        // Assert
        assertNull(result, "Sorting a null array should return null.");
    }
}