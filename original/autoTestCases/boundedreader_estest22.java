package org.apache.commons.io.input;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.StringReader;

public class BoundedReaderTest {

    @Test
    void testMarkAfterCloseThrowsIOException() throws IOException {
        // Arrange: Create a StringReader with an empty string and a BoundedReader with a maximum length of 1.
        StringReader stringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(stringReader, 1);

        // Act: Close the BoundedReader.  This also closes the underlying StringReader.
        boundedReader.close();

        // Assert: Attempting to call mark() on a closed reader should throw an IOException.
        IOException exception = assertThrows(IOException.class, () -> {
            boundedReader.mark(0); // Mark is not supported after closing the stream.
        });

        // Assert: Verify the exception message. This ensures the correct exception is thrown.
        assertEquals("Stream closed", exception.getMessage());
    }
}