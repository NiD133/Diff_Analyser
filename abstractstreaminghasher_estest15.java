package com.google.common.hash;

import java.nio.ByteBuffer;
import org.junit.Test;

/**
 * Tests for {@link AbstractStreamingHasher}.
 */
public class AbstractStreamingHasherTest {

    @Test(expected = NullPointerException.class)
    public void putBytes_withNullByteBuffer_throwsNullPointerException() {
        // Arrange: Create a concrete instance of the abstract class under test.
        // Crc32cHasher is a suitable implementation for this purpose.
        Hasher hasher = new Crc32cHashFunction.Crc32cHasher();

        // Act: Call the method under test with a null argument.
        // The test framework will assert that a NullPointerException is thrown.
        hasher.putBytes((ByteBuffer) null);
    }
}