package org.apache.commons.codec.net;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for soft line breaks ('soft breaks') in Quoted-Printable encoding,
 * as specified by RFC 1521. These tests use the strict codec, which enforces
 * a maximum line length of 76 characters.
 */
class QuotedPrintableCodecTest {

    // A codec configured for strict encoding, which enables soft line breaks.
    private QuotedPrintableCodec strictCodec;

    @BeforeEach
    void setUp() {
        strictCodec = new QuotedPrintableCodec(true);
    }

    @Test
    @DisplayName("Encoding with soft break should correctly handle a trailing tab")
    void testStrictEncodeWithSoftBreakAndTrailingTab() throws EncoderException {
        // A soft break is inserted to avoid the line exceeding the safe length.
        // The trailing tab is then encoded on the new line.
        final String input = "This is a example of a quoted-printable text file. There is no end to it\t";
        final String expected = "This is a example of a quoted-printable text file. There is no end to i=\r\nt=09";

        assertEquals(expected, strictCodec.encode(input));
    }

    @Test
    @DisplayName("Encoding with soft break should correctly handle a trailing space")
    void testStrictEncodeWithSoftBreakAndTrailingSpace() throws EncoderException {
        // Similar to the tab test, a soft break is inserted, and the trailing
        // space is encoded on the new line.
        final String input = "This is a example of a quoted-printable text file. There is no end to it ";
        final String expected = "This is a example of a quoted-printable text file. There is no end to i=\r\nt=20";

        assertEquals(expected, strictCodec.encode(input));
    }

    @Test
    @DisplayName("Encoding should handle multiple trailing spaces before a soft break")
    void testStrictEncodeWithSoftBreakAndMultipleTrailingSpaces() throws EncoderException {
        // This tests a more complex case where a space must be encoded to allow a soft
        // break, preventing a line from ending in whitespace before the break.
        final String input = "This is a example of a quoted-printable text file. There is no end to   ";
        final String expected = "This is a example of a quoted-printable text file. There is no end to=20=\r\n =20";

        assertEquals(expected, strictCodec.encode(input));
    }

    @Test
    @DisplayName("Encoding should handle a non-printable character before a soft break")
    void testStrictEncodeWithSoftBreakAndTrailingNonPrintableChar() throws EncoderException {
        // An '=' sign must be encoded. This test ensures the soft break is correctly
        // placed after the encoded character.
        final String input = "This is a example of a quoted-printable text file. There is no end to=  ";
        final String expected = "This is a example of a quoted-printable text file. There is no end to=3D=\r\n =20";

        assertEquals(expected, strictCodec.encode(input));
    }
}