package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Unit tests for the {@link ArraySorter} class.
 */
public class ArraySorterTest {

    /**
     * Tests that calling sort() on an empty double array returns the same array instance,
     * confirming the in-place nature of the sort for this edge case.
     */
    @Test
    public void sort_withEmptyDoubleArray_returnsSameInstance() {
        // Arrange: Create an empty array.
        final double[] emptyArray = new double[0];

        // Act: Call the sort method under test.
        final double[] resultArray = ArraySorter.sort(emptyArray);

        // Assert: The returned array should be the exact same instance as the input array.
        assertSame("The returned array should be the same instance as the input", emptyArray, resultArray);
    }
}