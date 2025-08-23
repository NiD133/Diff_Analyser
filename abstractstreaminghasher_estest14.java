package com.google.common.hash;

import org.junit.Test;
import java.nio.ByteBuffer;

/**
 * Tests for {@link AbstractStreamingHasher}.
 */
public class AbstractStreamingHasherTest {

    /**
     * Verifies that attempting to add data to a hasher after {@code hash()} has been called
     * results in an {@link IllegalStateException}. Once a hasher is finalized, it cannot be
     * reused.
     */
    @Test(expected = IllegalStateException.class)
    public void putBytes_afterCallingHash_throwsIllegalStateException() {
        // Arrange: Create a hasher and finalize it by calling hash().
        // We use a concrete implementation for this test.
        AbstractStreamingHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.hash(); // This call finalizes the hasher's state.

        ByteBuffer dataToAdd = ByteBuffer.allocate(16);

        // Act: Attempt to add more bytes to the already finalized hasher.
        // This action is expected to throw an IllegalStateException, which is
        // verified by the `expected` parameter in the @Test annotation.
        hasher.putBytes(dataToAdd);
    }
}