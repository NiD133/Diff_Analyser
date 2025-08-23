package com.google.common.io;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import org.junit.Test;

/**
 * Tests for the {@link CharStreams#exhaust(Readable)} method.
 */
public class CharStreamsExhaustTest {

    @Test
    public void exhaust_withEmptyReader_returnsZero() throws IOException {
        // Arrange: Create an empty reader, which has no characters to read.
        StringReader emptyReader = new StringReader("");

        // Act: Call the exhaust method to read all characters from the reader.
        long charsRead = CharStreams.exhaust(emptyReader);

        // Assert: Verify that zero characters were read, as the reader was empty.
        assertEquals("The number of characters exhausted from an empty reader should be 0.", 0L, charsRead);
    }
}