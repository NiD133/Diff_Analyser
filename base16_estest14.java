package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Base16} class, focusing on edge cases and exception handling.
 */
public class Base16Test {

    /**
     * Tests that the internal {@code encode} method throws a NullPointerException
     * when called with a null Context object. The Context object is essential for
     * the encoding process, and the method is expected to fail fast if it's not provided.
     */
    @Test
    public void encodeWithNullContextShouldThrowNullPointerException() {
        // Arrange
        final Base16 base16 = new Base16();
        final byte[] dummyData = new byte[3]; // The data itself is not relevant for this test.
        final BaseNCodec.Context context = null;

        // The specific values for offset (76) and length (0) are from the original
        // generated test. The primary trigger for the exception is the null context.
        final int offset = 76;
        final int length = 0;

        // Act & Assert
        try {
            base16.encode(dummyData, offset, length, context);
            fail("Expected a NullPointerException, but no exception was thrown.");
        } catch (final NullPointerException e) {
            // Success: The expected exception was caught.
            // The original exception has no message, so no further assertions are needed.
        }
    }
}