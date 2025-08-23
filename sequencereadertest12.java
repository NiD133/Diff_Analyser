package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SequenceReader}.
 */
public class SequenceReaderTest {

    /**
     * Tests that the SequenceReader correctly concatenates the content of multiple
     * underlying readers in the given order.
     */
    @Test
    void shouldReadCharactersSequentiallyFromMultipleReaders() throws IOException {
        // Arrange: Create a sequence of readers, each with a single character.
        final String expectedContent = "0123";
        final Reader[] readers = {
            new StringReader("0"),
            new StringReader("1"),
            new StringReader("2"),
            new StringReader("3")
        };

        // Act & Assert: The SequenceReader should read all characters in order,
        // then report the end of the stream.
        try (Reader sequenceReader = new SequenceReader(readers)) {
            assertReaderContains(sequenceReader, expectedContent);
            assertReaderIsAtEof(sequenceReader);
        }
    }

    /**
     * Asserts that the reader's content matches the expected string.
     *
     * @param reader   The reader to test.
     * @param expected The expected string content.
     * @throws IOException If an I/O error occurs.
     */
    private void assertReaderContains(final Reader reader, final String expected) throws IOException {
        char[] actual = new char[expected.length()];
        int n = reader.read(actual, 0, actual.length);

        assertEquals(expected.length(), n, "Should have read the expected number of characters.");
        assertEquals(expected, new String(actual), "The content read should match the expected string.");
    }

    /**
     * Asserts that the reader has reached the end of the stream (EOF).
     *
     * @param reader The reader to test.
     * @throws IOException If an I/O error occurs.
     */
    private void assertReaderIsAtEof(final Reader reader) throws IOException {
        assertEquals(-1, reader.read(), "Reader should be at the end of the stream (EOF).");
    }
}