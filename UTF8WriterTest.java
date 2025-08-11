package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for UTF8Writer with an emphasis on clarity:
 * - Descriptive test names
 * - Arrange/Act/Assert separation
 * - No magic numbers: compute expected UTF-8 sizes
 * - Consistent use of try-with-resources where appropriate
 * - Helpful assertion messages
 */
class UTF8WriterTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    private static final String MIXED_SAMPLE = "AB\u00A0\u1AE9\uFFFC"; // 2 ASCII + NBSP + 2 BMP non-ASCII
    private static final String ASCII_WITH_TWO_BYTE = "abcdefghijklmnopqrst\u00A0"; // NBSP at the end

    @Test
    @DisplayName("Writing via different APIs produces identical UTF-8 output")
    void writesStringViaVariousAPIs_producesSameUtf8Bytes() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer writer = newWriter(out);

        // Act: write the same content 3 times using different methods
        writer.write(MIXED_SAMPLE);

        char[] chars = MIXED_SAMPLE.toCharArray();
        writer.append(chars[0]);            // 'A'
        writer.write(chars[1]);             // 'B'
        writer.write(chars, 2, chars.length - 2); // rest
        writer.flush();

        writer.write(MIXED_SAMPLE, 0, MIXED_SAMPLE.length());
        writer.close();

        // Assert
        int expectedBytesPerCopy = utf8ByteLength(MIXED_SAMPLE);
        byte[] allBytes = out.toByteArray();
        assertEquals(3 * expectedBytesPerCopy, allBytes.length,
                "Should have 3 copies of the UTF-8-encoded data");

        String decoded = toUtf8String(out);
        assertEquals(3 * MIXED_SAMPLE.length(), decoded.length(),
                "Decoded character count should be 3x original length");
        assertEquals(MIXED_SAMPLE + MIXED_SAMPLE + MIXED_SAMPLE, decoded);
    }

    @Test
    @DisplayName("ASCII with a single 2-byte char encodes as expected")
    void writesMostlyAsciiWithOneTwoByteChar() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer writer = newWriter(out);

        char[] chars = ASCII_WITH_TWO_BYTE.toCharArray();

        // Act
        writer.write(chars, 0, chars.length);
        writer.flush(); // exercise code path that flushes before close
        writer.close();

        // Assert
        int expectedUtf8Bytes = utf8ByteLength(ASCII_WITH_TWO_BYTE);
        assertEquals(expectedUtf8Bytes, out.toByteArray().length,
                "Byte length should match UTF-8 encoding size");
        assertEquals(ASCII_WITH_TWO_BYTE, toUtf8String(out));
    }

    @Test
    @DisplayName("flush() and close() are safe and idempotent after close")
    void flushAndCloseAreIdempotentAfterClose() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer writer = newWriter(out);

        // Act
        writer.write('X');
        writer.write(new char[] { 'Y' });

        writer.close();

        // Assert
        assertEquals(2, out.size(), "Should have written 2 ASCII bytes");

        // These should be no-ops and not throw
        assertDoesNotThrow(writer::flush, "flush() after close should not throw");
        assertDoesNotThrow(writer::close, "close() after close should not throw");
        assertDoesNotThrow(writer::flush, "Multiple flush/close calls should be safe");
    }

    @Test
    @DisplayName("Valid surrogate pair encodes to 4-byte UTF-8 sequence")
    void writesValidSurrogatePair_asCodePointInUtf8() throws Exception {
        // U+1F603 (SMILING FACE WITH OPEN MOUTH)
        final byte[] EXPECTED = new byte[] { (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x83 };

        // Case 1: Write as two UTF-16 code units (char by char)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer writer = newWriter(out)) {
                writer.write(0xD83D); // high surrogate
                writer.write(0xDE03); // low surrogate
            }
            assertEquals(4, out.size(), "Surrogate pair should encode as 4 UTF-8 bytes");
            assertArrayEquals(EXPECTED, out.toByteArray());
        }

        // Case 2: Write as a String containing the surrogate pair
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer writer = newWriter(out)) {
                writer.write("\uD83D\uDE03");
            }
            assertEquals(4, out.size(), "Surrogate pair should encode as 4 UTF-8 bytes");
            assertArrayEquals(EXPECTED, out.toByteArray());
        }
    }

    @Test
    @DisplayName("Invalid surrogate usages fail with clear messages")
    void invalidSurrogates_failWithHelpfulMessages() throws Exception {
        // Unmatched second part (write int)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer writer = newWriter(out)) {
                IOException e = assertThrows(IOException.class, () -> writer.write(0xDE03));
                assertMessageContains(e, "Unmatched second part");
            }
        }

        // Broken surrogate pair: high surrogate not followed by low surrogate (write int)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer writer = newWriter(out)) {
                writer.write(0xD83D); // high surrogate
                IOException e = assertThrows(IOException.class, () -> writer.write('a'));
                assertMessageContains(e, "Broken surrogate pair");
            }
        }

        // Unmatched second part (write String)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer writer = newWriter(out)) {
                IOException e = assertThrows(IOException.class, () -> writer.write("\uDE03"));
                assertMessageContains(e, "Unmatched second part");
            }
        }

        // Broken surrogate pair (write String)
        {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            try (UTF8Writer writer = newWriter(out)) {
                IOException e = assertThrows(IOException.class, () -> writer.write("\uD83Da"));
                assertMessageContains(e, "Broken surrogate pair");
            }
        }
    }

    // For [core#1218] - verifies the constant-time formula for combining surrogates to a code point
    @Test
    @DisplayName("Surrogate combination formula matches step-by-step calculation")
    void surrogateConversion_formulaMatchesReferenceCalculation() {
        for (int first = UTF8Writer.SURR1_FIRST; first <= UTF8Writer.SURR1_LAST; first++) {
            for (int second = UTF8Writer.SURR2_FIRST; second <= UTF8Writer.SURR2_LAST; second++) {
                int expected = 0x10000 + ((first - UTF8Writer.SURR1_FIRST) << 10)
                        + (second - UTF8Writer.SURR2_FIRST);
                int actual = (first << 10) + second + UTF8Writer.SURROGATE_BASE;
                assertEquals(expected, actual,
                        "Mismatch for first=" + Integer.toHexString(first)
                                + ", second=" + Integer.toHexString(second));
            }
        }
    }

    // ------------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------------

    private IOContext newIOContext() {
        return testIOContext();
    }

    private UTF8Writer newWriter(OutputStream out) {
        return new UTF8Writer(newIOContext(), out);
    }

    private static String toUtf8String(ByteArrayOutputStream out) {
        return new String(out.toByteArray(), StandardCharsets.UTF_8);
    }

    private static int utf8ByteLength(String s) {
        return s.getBytes(StandardCharsets.UTF_8).length;
    }

    private static void assertMessageContains(Throwable t, String expectedFragment) {
        String msg = t.getMessage();
        assertNotNull(msg, "Exception message should not be null");
        assertTrue(msg.contains(expectedFragment),
                "Expected exception message to contain '" + expectedFragment + "', but was: " + msg);
    }
}