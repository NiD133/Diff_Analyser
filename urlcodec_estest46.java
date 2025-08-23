package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that the {@code encode(byte[])} method returns null when the input
     * byte array is null. This behavior is consistent with other encoders in the
     * Commons Codec library for null inputs.
     */
    @Test
    public void encodeByteArrayShouldReturnNullForNullInput() {
        // Arrange: Create an instance of the codec.
        URLCodec codec = new URLCodec();

        // Act: Call the encode method with a null input.
        byte[] encodedBytes = codec.encode(null);

        // Assert: Verify that the output is null.
        assertNull("Encoding a null byte array should return null.", encodedBytes);
    }
}