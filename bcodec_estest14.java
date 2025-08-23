package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.nio.charset.StandardCharsets;
import org.apache.commons.codec.CodecPolicy;
import org.junit.Test;

/**
 * Tests for {@link BCodec}.
 *
 * Note: The original test class name 'BCodec_ESTestTest14' was auto-generated.
 * It has been renamed to 'BCodecTest' for clarity.
 */
public class BCodecTest {

    @Test
    public void doDecodingWithNullCodecPolicyShouldThrowNullPointerException() {
        // Arrange: The BCodec constructor allows a null CodecPolicy, deferring the null check.
        final BCodec codec = new BCodec(StandardCharsets.UTF_8, null);
        final byte[] dummyInput = new byte[]{1, 2, 3}; // Content is irrelevant for this test.

        // Act & Assert: The doDecoding method internally creates a Base64 decoder that
        // requires a non-null CodecPolicy, which should trigger the exception.
        final NullPointerException thrown = assertThrows(
            NullPointerException.class,
            () -> codec.doDecoding(dummyInput)
        );

        // Verify the exception message to confirm the cause. The underlying Base64
        // implementation uses Objects.requireNonNull("codecPolicy").
        assertEquals("codecPolicy", thrown.getMessage());
    }
}