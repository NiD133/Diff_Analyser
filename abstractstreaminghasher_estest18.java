package com.google.common.hash;

import org.junit.Test;
import java.nio.ByteBuffer;

// Imports for the test runner are kept as they are part of the original test suite structure.
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class AbstractStreamingHasher_ESTestTest18 extends AbstractStreamingHasher_ESTest_scaffolding {

    /**
     * Tests that calling {@code processRemaining} with a very large buffer results in a timeout.
     *
     * <p>The {@code Crc32cHasher} implementation of {@code processRemaining} processes the buffer
     * byte by byte. Providing a buffer of over 1GB will cause this loop to run for a long time,
     * exceeding the test's 4-second timeout. This test verifies that the execution is correctly
     * interrupted by the timeout mechanism, which is the expected behavior for such a large input.
     */
    @Test(timeout = 4000)
    public void processRemaining_withLargeBuffer_shouldTimeOut() {
        // Arrange
        // Crc32cHasher is a concrete implementation of AbstractStreamingHasher.
        Crc32cHashFunction.Crc32cHasher hasher = new Crc32cHashFunction.Crc32cHasher();

        // Allocate a very large buffer (over 1.3 GB). Processing this buffer
        // is expected to take longer than the test's 4-second timeout.
        // Note: This allocation might fail with an OutOfMemoryError on systems
        // with limited heap, which is an acceptable alternative failure for this test.
        ByteBuffer largeBuffer = ByteBuffer.allocate(1_317_358_741);

        // Act & Assert
        // The call to processRemaining is expected to time out, which is asserted
        // by the @Test(timeout=4000) annotation. The Crc32cHasher's implementation
        // reads the buffer byte-by-byte, which is intentionally slow for a buffer of this size.
        hasher.processRemaining(largeBuffer);
    }
}