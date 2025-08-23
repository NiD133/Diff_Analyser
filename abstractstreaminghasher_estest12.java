package com.google.common.hash;

import static org.junit.Assert.assertThrows;

import com.google.common.hash.Crc32cHashFunction.Crc32cHasher;
import org.junit.Test;

/**
 * Tests for {@link AbstractStreamingHasher}.
 *
 * <p>This test focuses on the input validation of the {@code putBytes} method.
 */
public class AbstractStreamingHasherTest {

    @Test
    public void putBytes_givenNullArray_throwsNullPointerException() {
        // Arrange: Create a concrete instance of the abstract class under test.
        // Crc32cHasher is a simple, available implementation.
        Hasher hasher = new Crc32cHasher();
        byte[] nullInput = null;
        
        // The specific offset and length values do not matter when the input array is null.
        int offset = 703;
        int length = 703;

        // Act & Assert: Verify that calling putBytes with a null array throws a NullPointerException.
        // This is the expected behavior because the underlying implementation delegates to
        // ByteBuffer.wrap(), which does not accept null for the array parameter.
        assertThrows(
                NullPointerException.class,
                () -> hasher.putBytes(nullInput, offset, length));
    }
}