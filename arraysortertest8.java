package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArraySorter}.
 * This class name was changed from ArraySorterTestTest8 for clarity.
 */
public class ArraySorterTest extends AbstractLangTest {

    @Test
    @DisplayName("sort(T[]) should sort an array of objects correctly")
    void sortObjectArray_shouldSortArrayCorrectly() {
        // Arrange
        final String[] unsortedArray = {"foo", "bar", "baz"};
        final String[] expectedSortedArray = unsortedArray.clone();
        Arrays.sort(expectedSortedArray); // Use the standard library to establish the expected result.

        // Act
        final String[] actualSortedArray = ArraySorter.sort(unsortedArray);

        // Assert
        // Verify the array is sorted as expected.
        assertArrayEquals(expectedSortedArray, actualSortedArray);
        // Verify the method returns the same array instance, as per its contract.
        assertSame(unsortedArray, actualSortedArray);
    }

    @Test
    @DisplayName("sort(T[]) should return null when the input array is null")
    void sortObjectArray_withNullInput_shouldReturnNull() {
        // Arrange
        final String[] nullArray = null;

        // Act
        final String[] result = ArraySorter.sort(nullArray);

        // Assert
        assertNull(result);
    }
}