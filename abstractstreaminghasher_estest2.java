package com.google.common.hash;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link AbstractStreamingHasher}, focusing on exception handling for invalid inputs.
 */
public class AbstractStreamingHasherTest {

    @Test
    public void putBytes_withLengthExceedingArrayBounds_throwsIndexOutOfBoundsException() {
        // Arrange: Create a hasher and a small byte array.
        Hasher hasher = new Crc32cHashFunction.Crc32cHasher();
        byte[] sourceBytes = new byte[10];
        int offset = 0;
        int invalidLength = 11; // A length greater than the array's size.

        // Act & Assert: Verify that calling putBytes with a length that extends beyond
        // the array's bounds throws an IndexOutOfBoundsException.
        assertThrows(
                IndexOutOfBoundsException.class,
                () -> hasher.putBytes(sourceBytes, offset, invalidLength));
    }
}