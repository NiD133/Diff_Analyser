package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SequenceReader} using a List of Readers.
 */
class SequenceReaderTest {

    /**
     * Tests that the SequenceReader reads all characters from a list of readers
     * in the correct sequence and then correctly signals the end of the stream.
     */
    @Test
    void shouldReadCharactersSequentiallyFromListOfReaders() throws IOException {
        // Arrange: Define content for multiple readers and the combined expected output.
        final String content1 = "Hello";
        final String content2 = " World";
        final String expectedFullContent = content1 + content2;

        final List<Reader> readers = new ArrayList<>();
        readers.add(new StringReader(content1));
        readers.add(new StringReader(content2));

        // Act: Use a try-with-resources to ensure the reader is closed.
        try (final Reader sequenceReader = new SequenceReader(readers)) {
            // Assert: Verify that the SequenceReader provides all characters in order.
            for (int i = 0; i < expectedFullContent.length(); i++) {
                final char expectedChar = expectedFullContent.charAt(i);
                assertEquals(expectedChar, sequenceReader.read(), "Character mismatch at index " + i);
            }

            // Assert: Verify that the end of the stream is reached after all content is read.
            assertEquals(EOF, sequenceReader.read(), "Stream should be at its end.");
        }
    }
}