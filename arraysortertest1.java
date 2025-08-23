package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArraySorter}.
 *
 * This version improves upon the original by separating concerns into distinct
 * test cases, using descriptive names, and making assertions more explicit.
 */
// The class name was simplified from ArraySorterTestTest1 to the standard convention.
public class ArraySorterTest extends AbstractLangTest {

    @Test
    @DisplayName("sort(byte[]) should sort an array in place and return the same instance")
    void sortByteArray_withValidArray_shouldSortAndReturnSameInstance() {
        // Arrange: Define a clear, unsorted input and its expected sorted state.
        final byte[] unsortedArray = {2, 1, 3};
        final byte[] expectedSortedArray = {1, 2, 3};

        // Act: Call the method under test.
        final byte[] returnedArray = ArraySorter.sort(unsortedArray);

        // Assert: Verify both the correctness of the sort and the method's contract.
        // 1. The array should be sorted correctly.
        assertArrayEquals(expectedSortedArray, returnedArray, "The array should be sorted in ascending order.");

        // 2. The method should return the same array instance for fluent use.
        assertSame(unsortedArray, returnedArray, "The returned array should be the same instance as the input.");
    }

    @Test
    @DisplayName("sort(byte[]) should return null when given a null array")
    void sortByteArray_withNullArray_shouldReturnNull() {
        // Arrange: The input is null.
        final byte[] nullArray = null;

        // Act: Call the method with the null input.
        final byte[] result = ArraySorter.sort(nullArray);

        // Assert: The result should be null, as per the contract for null inputs.
        assertNull(result, "Sorting a null array should result in null.");
    }
}