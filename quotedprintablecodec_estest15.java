package org.apache.commons.codec.net;

import static org.junit.Assert.assertEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

/**
 * Tests for {@link QuotedPrintableCodec}.
 */
public class QuotedPrintableCodecTest {

    @Test
    public void encodeEmptyStringShouldReturnEmptyString() throws EncoderException {
        // Arrange
        final QuotedPrintableCodec codec = new QuotedPrintableCodec();
        final String input = "";

        // Act
        final String encodedString = codec.encode(input);

        // Assert
        assertEquals("Encoding an empty string should result in an empty string.", "", encodedString);
    }
}