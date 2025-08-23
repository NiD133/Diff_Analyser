package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArraySorter}.
 */
public class ArraySorterTestTest6 extends AbstractLangTest {

    @Test
    void sortIntArrayShouldCorrectlySortGivenArray() {
        // Arrange
        final int[] inputArray = {2, 1, 4, 3};
        final int[] expectedSortedArray = {1, 2, 3, 4};

        // Act
        final int[] actualSortedArray = ArraySorter.sort(inputArray);

        // Assert
        assertArrayEquals(expectedSortedArray, actualSortedArray);
    }

    @Test
    void sortIntArrayShouldReturnNullForNullInput() {
        // Arrange
        final int[] nullArray = null;

        // Act
        final int[] result = ArraySorter.sort(nullArray);

        // Assert
        assertNull(result);
    }
}