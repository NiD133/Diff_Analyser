package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that ArraySorter.sort(long[]) sorts the array in-place
     * and returns the same array instance, confirming the fluent API behavior.
     */
    @Test
    public void sortLongArray_shouldSortInPlaceAndReturnSameInstance() {
        // Arrange
        final long[] inputArray = {5L, 1L, 4L, 2L, 3L};
        final long[] expectedSortedArray = {1L, 2L, 3L, 4L, 5L};

        // Act
        final long[] result = ArraySorter.sort(inputArray);

        // Assert
        // 1. Verify that the method returns the same instance (fluent style)
        assertSame("The method should return the same array instance it was given.", inputArray, result);

        // 2. Verify that the original array was sorted correctly
        assertArrayEquals("The array should be sorted in ascending order.", expectedSortedArray, inputArray);
    }
}