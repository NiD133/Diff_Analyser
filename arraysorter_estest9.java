package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for {@link org.apache.commons.lang3.ArraySorter}.
 */
public class ArraySorterTest {

    @Test
    public void testSortByteArrayOfZeros() {
        // Arrange
        // An array where all elements are the same is, by definition, already sorted.
        final byte[] inputArray = new byte[4]; // Initialized to {0, 0, 0, 0}
        final byte[] expectedArray = {0, 0, 0, 0};

        // Act
        final byte[] resultArray = ArraySorter.sort(inputArray);

        // Assert
        // Check that the content is as expected.
        assertArrayEquals("An array of zeros should remain unchanged after sorting.", expectedArray, resultArray);
        
        // Also, verify that the sort is done in-place, as the method contract states it returns the given array.
        assertSame("The returned array should be the same instance as the input array.", inputArray, resultArray);
    }
}