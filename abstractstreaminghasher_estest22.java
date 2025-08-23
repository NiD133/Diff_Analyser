package com.google.common.hash;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.nio.BufferOverflowException;
import java.nio.charset.Charset;
import org.junit.Test;

/**
 * Tests for {@link AbstractStreamingHasher}.
 */
public class AbstractStreamingHasher_ESTestTest22 extends AbstractStreamingHasher_ESTest_scaffolding {

    @Test
    public void putBytes_afterHashIsCalculated_throwsException() {
        // Arrange: Create a hasher and use it to calculate a hash.
        // A Hasher instance is stateful and not intended for reuse after hash() is called.
        Hasher hasher = new Crc32cHashFunction.Crc32cHasher();
        hasher.putString("some initial data", Charset.defaultCharset());
        hasher.hash(); // This call finalizes the hasher's state.

        // Act & Assert: Attempting to add more data to the finalized hasher should fail.
        try {
            byte[] extraData = new byte[2];
            // This attempts to write to the hasher's internal buffer, which was finalized
            // by the preceding hash() call.
            hasher.putBytes(extraData, 1, 1);
            fail("Expected an exception to be thrown when writing to a hasher after hash() is called.");
        } catch (BufferOverflowException expected) {
            // SUCCESS: The expected exception was thrown.
            // The current implementation throws BufferOverflowException. While an
            // IllegalStateException might be more semantically appropriate, this test
            // verifies the actual, observed behavior.
            assertNull("The exception message was expected to be null.", expected.getMessage());
        }
    }
}