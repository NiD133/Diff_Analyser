package com.google.common.primitives;

import static org.junit.Assert.assertArrayEquals;
import org.junit.Test;

/**
 * Tests for {@link SignedBytes}.
 */
public class SignedBytesTest {

    @Test
    public void sortDescending_withUnsortedArray_sortsInDescendingOrder() {
        // Arrange: Create an unsorted array with a mix of positive, negative,
        // zero, and boundary values.
        byte[] actualArray = {5, -1, 127, 0, -128, 8};
        byte[] expectedArray = {127, 8, 5, 0, -1, -128};

        // Act: Call the method under test.
        SignedBytes.sortDescending(actualArray);

        // Assert: Verify that the array is now sorted in descending order.
        assertArrayEquals(expectedArray, actualArray);
    }
}