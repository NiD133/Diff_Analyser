package org.apache.commons.lang3;

import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Unit tests for the {@link ArraySorter} class.
 */
public class ArraySorterTest {

    /**
     * Tests that ArraySorter.sort() returns null when the input double array is null.
     * This verifies the null-safe behavior of the method.
     */
    @Test
    public void sort_shouldReturnNull_whenInputDoubleArrayIsNull() {
        // Arrange: No arrangement needed as the input is null.
        // The cast to (double[]) is necessary to resolve ambiguity between
        // the overloaded sort() methods.

        // Act
        final double[] sortedArray = ArraySorter.sort((double[]) null);

        // Assert
        assertNull("The result of sorting a null array should be null.", sortedArray);
    }
}