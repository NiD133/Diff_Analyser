package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests for the skip() functionality of {@link SequenceReader}.
 */
public class SequenceReaderTest {

    /**
     * Verifies that the content of the given reader matches the expected string.
     *
     * @param reader   The reader to read from.
     * @param expected The expected string content.
     * @throws IOException if an I/O error occurs.
     */
    private void assertReaderContentEquals(final Reader reader, final String expected) throws IOException {
        for (int i = 0; i < expected.length(); i++) {
            assertEquals(expected.charAt(i), (char) reader.read(), "Character at index " + i + " should match.");
        }
    }

    @Test
    void whenSkipCrossesReaderBoundary_thenNextReadIsFromSecondReader() throws IOException {
        // Arrange: Create a SequenceReader from two separate StringReaders.
        final String firstReaderContent = "Foo";
        final String secondReaderContent = "Bar";
        final Reader reader1 = new StringReader(firstReaderContent);
        final Reader reader2 = new StringReader(secondReaderContent);

        try (Reader sequenceReader = new SequenceReader(reader1, reader2)) {
            // Act: Skip the exact length of the first reader's content.
            final long skippedChars = sequenceReader.skip(firstReaderContent.length());

            // Assert: The correct number of characters were skipped, and the
            // subsequent read operation correctly pulls from the second reader.
            assertEquals(firstReaderContent.length(), skippedChars, "Should skip all characters of the first reader.");
            assertReaderContentEquals(sequenceReader, secondReaderContent);

            // Act & Assert: Attempting to skip again at the end of the stream should result in zero skipped characters.
            final long extraSkippedChars = sequenceReader.skip(10);
            assertEquals(0, extraSkippedChars, "Should not skip any characters once the stream is exhausted.");
        }
    }
}