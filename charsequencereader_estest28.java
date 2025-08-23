package com.google.common.io;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    @Test
    public void markSupported_shouldAlwaysReturnTrue() {
        // Arrange: Create a reader with any CharSequence. The content is irrelevant for this test.
        CharSequenceReader reader = new CharSequenceReader("test-sequence");

        // Act: Check if the reader supports the mark() operation.
        boolean isMarkSupported = reader.markSupported();

        // Assert: The reader is expected to support marking.
        assertTrue("CharSequenceReader should support the mark() operation.", isMarkSupported);
    }
}