package org.apache.commons.codec.net;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

/**
 * Contains tests for the URLCodec class.
 * This class focuses on providing clear, understandable, and maintainable tests.
 */
public class URLCodecTest {

    /**
     * Tests that decoding an empty byte array produces a new, empty byte array.
     * This verifies the correct handling of an edge case.
     */
    @Test
    public void testDecodeEmptyByteArrayReturnsNewEmptyArray() throws DecoderException {
        // Arrange: Create a URLCodec instance and an empty byte array to decode.
        final URLCodec urlCodec = new URLCodec();
        final byte[] emptyInput = new byte[0];

        // Act: Decode the empty byte array.
        final byte[] decodedResult = urlCodec.decode(emptyInput);

        // Assert: Verify the result is a new, non-null, and empty byte array.
        assertNotNull("The decoded array should not be null.", decodedResult);
        assertNotSame("The decoded array should be a new instance, not the original.", emptyInput, decodedResult);
        assertArrayEquals("Decoding an empty array should result in an empty array.", new byte[0], decodedResult);
    }
}