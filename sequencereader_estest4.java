package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Tests for {@link SequenceReader}.
 */
// Note: The original test class name "SequenceReader_ESTestTest4" and scaffolding
// have been removed for clarity in this standalone example.
public class SequenceReaderTest {

    /**
     * Tests that an IOException is thrown when the same reader instance is used
     * multiple times in a sequence.
     * <p>
     * The SequenceReader is expected to close a reader once it's exhausted (reaches EOF).
     * A subsequent attempt to read from the same, now-closed instance should fail.
     * </p>
     */
    @Test
    public void shouldThrowIOExceptionWhenSameReaderInstanceIsReused() throws IOException {
        // Arrange
        // A single, empty reader instance that will be used twice in the sequence.
        final Reader reusedEmptyReader = new StringReader("");
        final SequenceReader sequenceReader = new SequenceReader(reusedEmptyReader, reusedEmptyReader);
        final char[] buffer = new char[10];

        // Act & Assert
        try {
            // The first read attempt on the sequence will exhaust the first (empty) reader
            // and close it. The sequence then advances to the second reader, which is the
            // same instance, and attempts to read again. This will fail.
            sequenceReader.read(buffer, 0, buffer.length);
            fail("Expected an IOException because the underlying reader is closed after its first use.");
        } catch (final IOException e) {
            // Verify that the exception is the expected one from a closed stream.
            assertEquals("Stream closed", e.getMessage());
        }
    }
}