package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Tests for {@link ArraySorter}.
 */
public class ArraySorterTest {

    /**
     * Tests that the sort(char[]) method sorts the array in-place
     * and returns a reference to the same array instance.
     * This fluent-style return is a key feature of the class.
     */
    @Test
    public void sortCharArrayShouldSortInPlaceAndReturnSameInstance() {
        // Arrange
        char[] inputArray = {'c', 'a', 'd', 'b'};
        char[] expectedSortedArray = {'a', 'b', 'c', 'd'};

        // Act
        char[] resultArray = ArraySorter.sort(inputArray);

        // Assert
        // 1. Verify that the method returns the same array instance, not a copy.
        assertSame("The returned array should be the same instance as the input array.", inputArray, resultArray);

        // 2. Verify that the original array has been sorted correctly.
        assertArrayEquals("The input array should be sorted in ascending order.", expectedSortedArray, inputArray);
    }
}