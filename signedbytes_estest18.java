package com.google.common.primitives;

import org.junit.Test;

/**
 * Tests for {@link SignedBytes#sortDescending(byte[], int, int)}.
 */
public class SignedBytesTest {

    /**
     * This test verifies that sortDescending throws an IndexOutOfBoundsException
     * when the specified 'fromIndex' is outside the bounds of the array.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void sortDescending_withRange_fromIndexOutOfBounds_throwsException() {
        // GIVEN: An array and a 'fromIndex' that is greater than the array's length.
        byte[] array = new byte[10];
        int outOfBoundsIndex = array.length + 1;

        // WHEN: sortDescending is called with the out-of-bounds index.
        // The 'toIndex' must be >= 'fromIndex', so we set it to the same value.
        SignedBytes.sortDescending(array, outOfBoundsIndex, outOfBoundsIndex);

        // THEN: An IndexOutOfBoundsException is expected, as declared by the @Test annotation.
    }
}