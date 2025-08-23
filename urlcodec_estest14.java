package org.apache.commons.codec.net;

import static org.junit.Assert.assertArrayEquals;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

/**
 * Tests for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that {@link URLCodec#decodeUrl(byte[])} correctly handles an empty byte array
     * input by returning an empty byte array.
     */
    @Test
    public void testDecodeUrlWithEmptyByteArrayReturnsEmptyByteArray() throws DecoderException {
        // Arrange: Define the empty input and the expected empty output.
        final byte[] emptyInput = new byte[0];
        final byte[] expectedOutput = new byte[0];

        // Act: Call the method under test.
        final byte[] actualOutput = URLCodec.decodeUrl(emptyInput);

        // Assert: Verify that the actual output matches the expected output.
        assertArrayEquals("Decoding an empty byte array should yield an empty byte array.",
                          expectedOutput, actualOutput);
    }
}