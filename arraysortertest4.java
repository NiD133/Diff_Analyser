package org.apache.commons.lang3;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ArraySorter}.
 */
class ArraySorterTest extends AbstractLangTest {

    @Test
    @DisplayName("sort() with a valid double array should return a sorted array")
    void sort_withUnsortedDoubleArray_returnsSortedArray() {
        // Arrange
        final double[] array = {2d, 1d, 3d};
        final double[] expected = {1d, 2d, 3d};

        // Act
        final double[] actual = ArraySorter.sort(array);

        // Assert
        assertArrayEquals(expected, actual);
    }

    @Test
    @DisplayName("sort() with a null array should return null")
    void sort_withNullArray_returnsNull() {
        // Arrange
        final double[] array = null;

        // Act
        final double[] result = ArraySorter.sort(array);

        // Assert
        assertNull(result);
    }
}