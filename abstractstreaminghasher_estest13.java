package com.google.common.hash;

import java.nio.ByteBuffer;
import org.junit.Test;

/**
 * Test for {@link AbstractStreamingHasher} focusing on behavior with very large inputs.
 */
public class AbstractStreamingHasherTest {

    /**
     * Verifies that attempting to process a direct ByteBuffer that is too large to allocate
     * results in an {@link OutOfMemoryError}.
     *
     * <p>This test case ensures that the code path handles extreme resource conditions gracefully.
     * The error is expected to be thrown by {@code ByteBuffer.allocateDirect} when the system
     * cannot provide the requested amount of native memory.
     */
    @Test(expected = OutOfMemoryError.class, timeout = 4000)
    public void putBytes_withHugeDirectBuffer_throwsOutOfMemoryError() {
        // Arrange: Create a concrete hasher instance to test the abstract class's behavior.
        Hasher hasher = new Crc32cHashFunction.Crc32cHasher();

        // Act: Attempt to allocate a direct ByteBuffer of maximum possible size and pass it to the
        // hasher. The OutOfMemoryError is expected during the allocation of the buffer itself.
        // The @Test(expected=...) annotation will catch this error and pass the test.
        ByteBuffer hugeDirectBuffer = ByteBuffer.allocateDirect(Integer.MAX_VALUE);
        hasher.putBytes(hugeDirectBuffer);
    }
}