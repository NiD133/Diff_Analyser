package com.google.common.io;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    /**
     * Verifies that CharSequenceReader correctly reports that it supports
     * the mark() and reset() operations.
     */
    @Test
    public void markSupported_shouldAlwaysReturnTrue() {
        // Arrange: Create a reader with any CharSequence. A simple string is sufficient.
        CharSequenceReader reader = new CharSequenceReader("test-data");

        // Act & Assert: The reader should consistently support marking.
        assertTrue(
                "CharSequenceReader should always support mark and reset operations.",
                reader.markSupported());
    }
}