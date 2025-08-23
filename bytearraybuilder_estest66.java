package com.fasterxml.jackson.core.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link ByteArrayBuilder} class.
 */
public class ByteArrayBuilderTest {

    /**
     * Tests that the size of a ByteArrayBuilder, created with an initial buffer
     * and a specified length, remains unchanged after calling the flush() method.
     *
     * The flush() method in ByteArrayBuilder is a no-op, so it should not
     * affect the internal state, including the reported size. This test verifies
     * that the size correctly reflects the length provided during construction.
     */
    @Test
    public void testSizeIsUnaffectedByFlushWhenCreatedWithInitialBuffer() {
        // Arrange: Create a ByteArrayBuilder with an initial buffer and a specified length.
        // The builder is initialized to believe it contains one byte, even though the
        // provided buffer is empty. This tests that the builder trusts the provided length.
        byte[] initialBuffer = new byte[0];
        int initialSize = 1;
        ByteArrayBuilder builder = ByteArrayBuilder.fromInitial(initialBuffer, initialSize);

        // Act: Call the flush() method, which is expected to be a no-op.
        builder.flush();

        // Assert: The size of the builder should still be the specified initial length.
        assertEquals("flush() should not change the size of the builder.",
                initialSize, builder.size());
    }
}