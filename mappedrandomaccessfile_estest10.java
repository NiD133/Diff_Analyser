package com.itextpdf.text.pdf;

import org.junit.Test;
import java.nio.ByteBuffer;
import static org.junit.Assert.assertFalse;

/**
 * Test suite for the {@link MappedRandomAccessFile} class.
 */
public class MappedRandomAccessFileTest {

    /**
     * Verifies that the {@code clean} method returns false when called with a
     * standard heap-based ByteBuffer.
     * <p>
     * The {@code clean} method is designed to work specifically with direct,
     * memory-mapped buffers to release their underlying resources. For any other
     * type of buffer, it should fail gracefully.
     * </p>
     */
    @Test
    public void clean_shouldReturnFalse_whenBufferIsNotDirect() {
        // Arrange: Create a standard heap-based ByteBuffer. This is different from
        // a memory-mapped (direct) buffer that the clean() method is designed for.
        ByteBuffer heapByteBuffer = ByteBuffer.allocate(1024);

        // Act: Attempt to "clean" the heap-based buffer.
        boolean wasCleaned = MappedRandomAccessFile.clean(heapByteBuffer);

        // Assert: The operation should not succeed, as the buffer is not a
        // cleanable, direct buffer.
        assertFalse("clean() should return false for a non-direct (heap) ByteBuffer.", wasCleaned);
    }
}