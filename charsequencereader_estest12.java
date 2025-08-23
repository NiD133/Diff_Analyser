package com.google.common.io;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 * This class contains the improved test case.
 */
public class CharSequenceReader_ESTestTest12 {

    @Test
    public void read_whenBufferIsNull_throwsNullPointerException() {
        // Arrange: Create a reader with any valid CharSequence.
        // The content of the sequence is irrelevant for this test.
        CharSequenceReader reader = new CharSequenceReader("test data");

        // Act & Assert: Verify that calling read() with a null buffer throws a NullPointerException.
        // The Guava library's convention is to fail fast with a NullPointerException for null arguments.
        assertThrows(
                NullPointerException.class,
                () -> {
                    // The offset and length values do not matter here, as the null check on the
                    // buffer should happen first. We use 0 and 1 as standard placeholders.
                    reader.read(null, 0, 1);
                });
    }
}