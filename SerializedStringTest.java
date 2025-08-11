package com.fasterxml.jackson.core.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.core.io.SerializedString;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link SerializedString} focused on readability and intent:
 * - make quoted vs. unquoted expectations explicit
 * - avoid "magic numbers" by naming offsets
 * - use UTF-8 helpers for clarity
 * - group related assertions
 */
class SerializedStringTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    // Raw input containing quotes and a backslash
    private static final String RAW_INPUT = "\"quo\\ted\"";
    // RAW_INPUT escaped per JSON rules
    private static final String JSON_ESCAPED_INPUT = "\\\"quo\\\\ted\\\"";
    // JSON-escape of JSON_ESCAPED_INPUT (i.e. "escaped twice")
    private static final String JSON_ESCAPED_TWICE = "\\\\\\\"quo\\\\\\\\ted\\\\\\\"";

    private static String utf8(byte[] bytes) {
        return new String(bytes, StandardCharsets.UTF_8);
    }

    private static String utf8(byte[] bytes, int offset, int length) {
        return new String(bytes, offset, length, StandardCharsets.UTF_8);
    }

    @Test
    @DisplayName("Quoted/unquoted: chars, UTF-8 writing, and buffer appending")
    void quotedAndUnquotedRoundTrip() throws IOException {
        final SerializableString sstr = new SerializedString(RAW_INPUT);

        // Sanity checks
        assertAll("Sanity",
                () -> assertEquals(RAW_INPUT, sstr.getValue(), "getValue should return original input"),
                () -> assertEquals(JSON_ESCAPED_INPUT, new String(sstr.asQuotedChars()), "asQuotedChars should match JSON-escaped form")
        );

        // Write quoted/unquoted to OutputStream
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final int quotedBytesWritten = sstr.writeQuotedUTF8(out);
        assertAll("writeQuotedUTF8",
                () -> assertEquals(JSON_ESCAPED_INPUT.length(), quotedBytesWritten, "byte count should match UTF-8 length"),
                () -> assertEquals(JSON_ESCAPED_INPUT, utf8(out.toByteArray()), "stream content should be JSON-escaped")
        );

        out.reset();
        final int unquotedBytesWritten = sstr.writeUnquotedUTF8(out);
        assertAll("writeUnquotedUTF8",
                () -> assertEquals(RAW_INPUT.length(), unquotedBytesWritten, "byte count should match UTF-8 length"),
                () -> assertEquals(RAW_INPUT, utf8(out.toByteArray()), "stream content should be raw input")
        );

        // Append quoted/unquoted into byte[] with offsets
        final byte[] buffer = new byte[100];

        final int QUOTED_OFFSET = 3;
        final int quotedLen = sstr.appendQuotedUTF8(buffer, QUOTED_OFFSET);
        assertAll("appendQuotedUTF8",
                () -> assertEquals(JSON_ESCAPED_INPUT.length(), quotedLen, "returned length should equal appended bytes"),
                () -> assertEquals(JSON_ESCAPED_INPUT, utf8(buffer, QUOTED_OFFSET, quotedLen), "buffer content should be JSON-escaped")
        );

        Arrays.fill(buffer, (byte) 0);

        final int UNQUOTED_OFFSET = 5;
        final int unquotedLen = sstr.appendUnquotedUTF8(buffer, UNQUOTED_OFFSET);
        assertAll("appendUnquotedUTF8",
                () -> assertEquals(RAW_INPUT.length(), unquotedLen, "returned length should equal appended bytes"),
                () -> assertEquals(RAW_INPUT, utf8(buffer, UNQUOTED_OFFSET, unquotedLen), "buffer content should be raw input")
        );
    }

    @Test
    @DisplayName("Insufficient target capacity returns -1 for all append/put variants")
    void insufficientCapacitySignalsFailure() {
        final String input = "Bit longer text";
        final SerializedString sstr = new SerializedString(input);

        // Make targets intentionally too small
        final byte[] smallBytes = new byte[input.length() - 2];
        final char[] smallChars = new char[input.length() - 2];
        final ByteBuffer smallBuf = ByteBuffer.allocate(input.length() - 2);

        assertAll("Quoted writes should fail",
                () -> assertEquals(-1, sstr.appendQuotedUTF8(smallBytes, 0), "appendQuotedUTF8 should signal insufficient space with -1"),
                () -> assertEquals(-1, sstr.appendQuoted(smallChars, 0), "appendQuoted should signal insufficient space with -1"),
                () -> assertEquals(-1, sstr.putQuotedUTF8(smallBuf), "putQuotedUTF8 should signal insufficient space with -1")
        );

        smallBuf.rewind();

        assertAll("Unquoted writes should fail",
                () -> assertEquals(-1, sstr.appendUnquotedUTF8(smallBytes, 0), "appendUnquotedUTF8 should signal insufficient space with -1"),
                () -> assertEquals(-1, sstr.appendUnquoted(smallChars, 0), "appendUnquoted should signal insufficient space with -1"),
                () -> assertEquals(-1, sstr.putUnquotedUTF8(smallBuf), "putUnquotedUTF8 should signal insufficient space with -1")
        );
    }

    @Test
    @DisplayName("appendQuotedUTF8 escapes already-escaped input again (double-escape)")
    void appendQuotedUTF8_doubleEscapes() {
        final SerializedString sstr = new SerializedString(JSON_ESCAPED_INPUT);
        final byte[] buffer = new byte[100];
        final int offset = 3;

        final int len = sstr.appendQuotedUTF8(buffer, offset);
        assertEquals(JSON_ESCAPED_TWICE, utf8(buffer, offset, len),
                "Quoted output should be the JSON-escape of the original (already-escaped) value");
    }

    @Test
    @DisplayName("JDK serialization preserves value and escaping behavior")
    void jdkSerializationPreservesBehavior() throws IOException {
        final byte[] bytes = jdkSerialize(new SerializedString(JSON_ESCAPED_INPUT));
        final SerializedString sstr = jdkDeserialize(bytes);

        assertEquals(JSON_ESCAPED_INPUT, sstr.getValue(), "Deserialized value should match original");

        final byte[] buffer = new byte[100];
        final int offset = 3;
        final int len = sstr.appendQuotedUTF8(buffer, offset);

        assertEquals(JSON_ESCAPED_TWICE, utf8(buffer, offset, len),
                "Behavior after deserialization should match: quoting should double-escape");
    }
}