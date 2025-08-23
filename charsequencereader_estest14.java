package com.google.common.io;

import static org.junit.Assert.assertThrows;

import java.nio.CharBuffer;
import java.nio.ReadOnlyBufferException;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    @Test
    public void read_intoReadOnlyBuffer_throwsReadOnlyBufferException() {
        // Arrange: Create a reader and a read-only buffer to serve as the destination.
        CharSequenceReader reader = new CharSequenceReader("source data");
        
        // CharBuffer.wrap(CharSequence) is a standard way to create a read-only buffer.
        CharBuffer readOnlyTargetBuffer = CharBuffer.wrap("some text");

        // Act & Assert: Verify that attempting to write to the read-only buffer throws the
        // correct exception.
        assertThrows(ReadOnlyBufferException.class, () -> reader.read(readOnlyTargetBuffer));
    }
}