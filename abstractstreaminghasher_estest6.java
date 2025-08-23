package com.google.common.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.google.common.hash.Crc32cHashFunction.Crc32cHasher;
import org.junit.Test;

// The scaffolding class is kept for structural compatibility with the original test suite.
public class AbstractStreamingHasher_ESTestTest6 extends AbstractStreamingHasher_ESTest_scaffolding {

    /**
     * Tests that attempting to add data to a hasher after it has been finalized
     * throws an IllegalStateException when its internal buffer is flushed.
     */
    @Test
    public void putLong_afterHashFinalized_throwsIllegalStateExceptionOnBufferFlush() {
        // Arrange: Create a hasher and finalize it by calling the protected makeHash() method.
        // This test targets a specific internal behavior. It simulates the state after a hash
        // has been computed, which should prevent further data processing.
        Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.makeHash();

        // Act 1: The first call to putLong() succeeds. It writes to the hasher's
        // internal buffer but does not fill it enough to trigger a processing cycle (a "flush").
        hasher.putLong(-4265267296055464877L);

        // Act 2 & Assert: The second call to putLong() is designed to overfill the buffer,
        // forcing a flush. Since the hasher has already been finalized, this operation is
        // expected to fail by throwing an IllegalStateException.
        try {
            hasher.putLong(2304L);
            fail("Expected an IllegalStateException, but it was not thrown.");
        } catch (IllegalStateException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals(
                "The behavior of calling any method after calling hash() is undefined.", e.getMessage());
        }
    }
}