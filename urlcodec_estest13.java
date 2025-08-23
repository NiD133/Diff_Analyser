package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link URLCodec} class.
 */
public class URLCodecTest {

    /**
     * Tests that encoding an empty string results in an empty string.
     * This is an important edge case to ensure the codec handles it gracefully.
     */
    @Test
    public void shouldReturnEmptyStringWhenEncodingEmptyString() throws EncoderException {
        // Arrange: Create a URLCodec instance and the input string.
        URLCodec codec = new URLCodec();
        String emptyInput = "";

        // Act: Encode the empty string.
        String encodedResult = codec.encode(emptyInput);

        // Assert: Verify that the result is a non-null empty string.
        assertNotNull("The encoded string should not be null.", encodedResult);
        assertEquals("Encoding an empty string should result in an empty string.", emptyInput, encodedResult);
    }
}