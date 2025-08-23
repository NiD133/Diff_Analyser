package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.CharBuffer;
import org.junit.Test;

/**
 * Tests for {@link CharSequenceReader}.
 */
public class CharSequenceReaderTest {

    @Test
    public void readIntoCharBuffer_shouldReturnZero_whenTargetBufferHasNoRemainingSpace() throws IOException {
        // Arrange: Create a reader and a target buffer that is already full.
        CharSequenceReader reader = new CharSequenceReader("source data");

        CharBuffer fullTargetBuffer = CharBuffer.allocate(10);
        fullTargetBuffer.position(fullTargetBuffer.limit()); // Advance position to the end, leaving 0 remaining space.

        // Act: Attempt to read from the source into the full buffer.
        int charsRead = reader.read(fullTargetBuffer);

        // Assert: Verify that no characters were read, as expected.
        assertEquals("Should read 0 chars into a buffer with no remaining space", 0, charsRead);
    }
}