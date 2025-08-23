package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertSame;

/**
 * Contains tests for the {@link ArraySorter} class.
 * Note: The class name and inheritance are preserved from the original auto-generated test.
 * A more conventional name would be `ArraySorterTest`.
 */
public class ArraySorter_ESTestTest28 extends ArraySorter_ESTest_scaffolding {

    /**
     * Tests that sorting an empty byte array returns the same array instance,
     * confirming the in-place nature of the sort and handling of this edge case.
     */
    @Test
    public void sortByteArray_withEmptyArray_returnsSameInstance() {
        // Arrange: Create an empty byte array.
        final byte[] emptyArray = new byte[0];

        // Act: Call the sort method on the empty array.
        final byte[] resultArray = ArraySorter.sort(emptyArray);

        // Assert: Verify that the returned array is the exact same instance as the input array.
        assertSame("Sorting an empty array should return the same instance", emptyArray, resultArray);
    }
}