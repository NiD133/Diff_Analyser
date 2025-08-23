package com.google.common.io;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.CharBuffer;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
class CharSequenceReaderTest {

    /**
     * Verifies that read(CharBuffer) throws a NullPointerException when the target buffer is null.
     */
    @Test
    void read_intoNullBuffer_throwsNullPointerException() {
        // Arrange
        CharSequenceReader reader = new CharSequenceReader("test-sequence");

        // Act & Assert
        // The cast to (CharBuffer) is necessary to resolve ambiguity between
        // read(CharBuffer) and other read() overloads.
        assertThrows(NullPointerException.class, () -> reader.read((CharBuffer) null));
    }
}