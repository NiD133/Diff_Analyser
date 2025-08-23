package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for QuotedPrintableCodec in "strict" mode, focusing on soft line breaks.
 * In strict mode, the codec must insert soft line breaks (=\r\n) to ensure
 * that encoded lines do not exceed the 76-character limit.
 */
public class QuotedPrintableCodecStrictLineBreakTest {

    private QuotedPrintableCodec strictCodec;

    @BeforeEach
    void setUp() {
        // The 'true' argument enables strict encoding, which includes soft line breaks.
        strictCodec = new QuotedPrintableCodec(true);
    }

    @Test
    @DisplayName("Encoding '=' near line limit in strict mode should insert a soft line break")
    void testEqualsSignNearLineLimitIsEncodedWithSoftLineBreak() throws EncoderException {
        // Arrange
        // The '=' character is at a position that requires a soft line break after encoding in strict mode.
        final String inputString = "This is a example of a quoted-printable text file. This might contain sp=cial chars.";
        final String expectedEncoding = "This is a example of a quoted-printable text file. This might contain sp=3D=\r\ncial chars.";

        // Act
        final String actualEncoding = strictCodec.encode(inputString);

        // Assert
        assertEquals(expectedEncoding, actualEncoding);
    }

    @Test
    @DisplayName("Encoding a TAB near line limit in strict mode should insert a soft line break")
    void testTabNearLineLimitIsEncodedWithSoftLineBreak() throws EncoderException {
        // Arrange
        // The TAB character ('\t') is at a position that requires a soft line break after encoding in strict mode.
        final String inputString = "This is a example of a quoted-printable text file. This might contain ta\tbs as well.";
        final String expectedEncoding = "This is a example of a quoted-printable text file. This might contain ta=09=\r\nbs as well.";

        // Act
        final String actualEncoding = strictCodec.encode(inputString);

        // Assert
        assertEquals(expectedEncoding, actualEncoding);
    }
}