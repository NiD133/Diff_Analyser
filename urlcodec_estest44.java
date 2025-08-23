package org.apache.commons.codec.net;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link URLCodec}.
 */
public class URLCodecTest {

    @Test
    public void whenEncodingStringWithOnlyUrlSafeCharacters_thenStringRemainsUnchanged() throws EncoderException {
        // Arrange
        // The input string contains only alphanumeric characters and periods,
        // which are considered safe for URL encoding and should not be modified.
        final String safeString = "org.apache.commons.codec.DecoderException";
        final URLCodec urlCodec = new URLCodec();

        // Act
        final String encodedString = urlCodec.encode(safeString);

        // Assert
        assertEquals("Encoding a string composed entirely of safe characters should not alter it.",
                     safeString, encodedString);
    }
}