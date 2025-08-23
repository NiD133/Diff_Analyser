package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link SequenceReader}.
 */
// Renamed for clarity and convention.
public class SequenceReaderTest {

    @Test
    // A descriptive name and display name clarify the test's purpose.
    @DisplayName("SequenceReader should read all content from multiple readers in order")
    void shouldReadSequentiallyFromMultipleReaders() throws IOException {
        // --- Arrange ---
        // Clearly define the input readers and the expected combined output.
        // This makes the test's intent obvious at a glance.
        final Reader reader1 = new StringReader("Foo");
        final Reader reader2 = new StringReader("Bar");
        final String expectedContent = "FooBar";

        // --- Act ---
        // Use a try-with-resources block for proper resource management.
        try (Reader sequenceReader = new SequenceReader(reader1, reader2)) {
            // Use a utility to read the entire stream. This is more concise
            // and less error-prone than reading character by character.
            final String actualContent = IOUtils.toString(sequenceReader);

            // --- Assert ---
            // A single, clear assertion on the final result is more readable
            // than multiple assertions on individual characters.
            assertEquals(expectedContent, actualContent);

            // Verify that the end of the stream has been reached.
            assertEquals(EOF, sequenceReader.read(), "Should be at the end of the stream after reading all content.");
        }
    }
}