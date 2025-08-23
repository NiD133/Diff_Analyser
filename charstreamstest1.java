package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for {@link CharStreams#toString(Readable)}.
 */
public class CharStreamsTest {

    private static final String EXPECTED_STRING = "The quick brown fox jumped over the lazy dog.";

    @Test
    public void toString_givenStringReader_returnsEntireString() throws IOException {
        // Arrange: Create a reader with known content.
        Reader reader = new StringReader(EXPECTED_STRING);

        // Act: Call the method under test.
        String result = CharStreams.toString(reader);

        // Assert: Verify that the result matches the original content.
        assertEquals(EXPECTED_STRING, result);
    }
}