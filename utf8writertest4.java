package com.fasterxml.jackson.core.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

/**
 * Tests for {@link UTF8Writer} focusing on its ability to correctly handle
 * and encode Unicode surrogate pairs.
 */
class UTF8WriterTest extends com.fasterxml.jackson.core.JUnit5TestBase {

    // The "GRINNING FACE WITH BIG EYES" emoji (U+1F603) is used for testing surrogates.
    private static final String EMOJI_STRING = "\uD83D\uDE03";
    private static final char HIGH_SURROGATE = '\uD83D';
    private static final char LOW_SURROGATE = '\uDE03';

    // The expected 4-byte UTF-8 representation of the emoji.
    private static final byte[] EMOJI_UTF8_BYTES = new byte[]{
            (byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x83
    };

    private IOContext _ioContext() {
        return testIOContext();
    }

    @Test
    @DisplayName("should correctly encode a valid surrogate pair written as a single String")
    void writeValidSurrogatePairAsString() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(_ioContext(), out);

        // Act
        writer.write(EMOJI_STRING);
        writer.close();

        // Assert
        assertArrayEquals(EMOJI_UTF8_BYTES, out.toByteArray(),
                "UTF8Writer should correctly encode a surrogate pair from a String into 4 bytes.");
    }

    @Test
    @DisplayName("should correctly encode a valid surrogate pair written character by character")
    void writeValidSurrogatePairCharByChar() throws Exception {
        // Arrange
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        UTF8Writer writer = new UTF8Writer(_ioContext(), out);

        // Act
        writer.write(HIGH_SURROGATE);
        writer.write(LOW_SURROGATE);
        writer.close();

        // Assert
        assertArrayEquals(EMOJI_UTF8_BYTES, out.toByteArray(),
                "UTF8Writer should buffer the high surrogate and combine it with the low surrogate for correct encoding.");
    }
}