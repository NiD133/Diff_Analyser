package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArraySorter}.
 */
// The class was renamed from ArraySorterTestTest5 to ArraySorterTest for clarity
// and to follow standard naming conventions.
class ArraySorterTest extends AbstractLangTest {

    @Test
    @DisplayName("Sorting a float array should sort it correctly in-place")
    void testSortFloatArray() {
        // Arrange: Define the initial unsorted array and the expected result.
        final float[] unsortedArray = { 2.0f, 1.0f, 3.5f };
        final float[] expectedSortedArray = { 1.0f, 2.0f, 3.5f };

        // Act: Call the method under test.
        final float[] actualSortedArray = ArraySorter.sort(unsortedArray);

        // Assert: Verify the array is sorted correctly and the operation was in-place.
        assertArrayEquals(expectedSortedArray, actualSortedArray, "The array should be sorted in ascending order.");
        assertSame(unsortedArray, actualSortedArray, "The method should return the same array instance it was given.");
    }

    @Test
    @DisplayName("Sorting a null float array should return null")
    void testSortWithNullFloatArray() {
        // Arrange: A null array is the input.
        final float[] nullArray = null;

        // Act: Call the method with the null input.
        final float[] result = ArraySorter.sort(nullArray);

        // Assert: Verify that the result is null.
        assertNull(result, "Sorting a null array should return null.");
    }
}