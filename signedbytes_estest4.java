package com.google.common.primitives;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    @Test
    public void sortDescending_withSingleElementRange_doesNotModifyArray() {
        // Arrange: Create an array with unsorted values.
        byte[] array = {10, 5, 20, 15};
        byte[] expected = {10, 5, 20, 15};

        // Act: Sort the sub-array from index 1 (inclusive) to 2 (exclusive).
        // This range contains only the single element '5', so sorting it is a no-op.
        SignedBytes.sortDescending(array, 1, 2);

        // Assert: The array should remain completely unchanged.
        assertArrayEquals(expected, array);
    }
}