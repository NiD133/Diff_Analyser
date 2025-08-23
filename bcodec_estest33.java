package org.apache.commons.codec.net;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link BCodec} class.
 */
public class BCodecTest {

    /**
     * Tests that a valid RFC 1522 encoded-word string is correctly decoded
     * back to its original form.
     */
    @Test
    public void shouldDecodeValidRFC1522String() throws Exception {
        // Arrange
        final BCodec bCodec = new BCodec();
        final String encodedWord = "=?UTF-8?B?IGVuY29kZWQgY29udGVudA==?=";
        final String expectedDecodedText = " encoded content";

        // Act
        final String actualDecodedText = bCodec.decode(encodedWord);

        // Assert
        assertEquals(expectedDecodedText, actualDecodedText);
    }
}