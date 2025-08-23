package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the RFC1522Codec class, using BCodec as a concrete implementation.
 */
public class BCodecTest {

    /**
     * Tests that encodeText(String, String) throws an IllegalArgumentException
     * when the provided charset name is null. The underlying java.nio.charset.Charset
     * class is responsible for this behavior.
     */
    @Test
    public void encodeTextWithNullCharsetNameShouldThrowIllegalArgumentException() {
        // Arrange
        final BCodec bCodec = new BCodec();
        final String textToEncode = "Any text can be used here";

        // Act & Assert
        try {
            // The cast to (String) is necessary to avoid ambiguity with the
            // encodeText(String, Charset) method overload.
            bCodec.encodeText(textToEncode, (String) null);
            fail("Expected an IllegalArgumentException to be thrown for a null charset name.");
        } catch (final IllegalArgumentException e) {
            // This is the expected outcome.
            // We verify the exception message to ensure it's the one we expect
            // from Charset.forName(null).
            assertEquals("Null charset name", e.getMessage());
        }
    }
}