package org.apache.commons.io.input;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that reading stops at the defined bound, even if mark() is called
     * with a larger readAheadLimit. The original test name was misleading as
     * reset() is never called.
     */
    @Test
    void readShouldRespectBoundWhenMarkIsLarger() throws IOException {
        // Arrange
        final int maxCharsToRead = 3;
        final int readAheadLimit = 4; // Intentionally larger than the bound
        final String content = "01234567890";
        final Reader sourceReader = new BufferedReader(new StringReader(content));

        try (final BoundedReader boundedReader = new BoundedReader(sourceReader, maxCharsToRead)) {
            // Act
            // Set a mark with a read-ahead limit greater than the reader's bound.
            boundedReader.mark(readAheadLimit);

            // Assert
            // Read characters up to the bound.
            assertEquals('0', boundedReader.read(), "1st character should be '0'");
            assertEquals('1', boundedReader.read(), "2nd character should be '1'");
            assertEquals('2', boundedReader.read(), "3rd character should be '2'");

            // The next read should return EOF, as the bound of 3 has been reached.
            assertEquals(-1, boundedReader.read(), "Should return EOF after reaching the bound");
        }
    }
}