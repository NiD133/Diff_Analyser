package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

/**
 * Tests for the {@link ProxyReader} class.
 */
public class ProxyReaderTest {

    /**
     * Tests that reading from an empty underlying reader into a CharBuffer
     * correctly returns EOF (-1), indicating the end of the stream.
     */
    @Test
    public void readIntoCharBufferShouldReturnEofForEmptyReader() throws IOException {
        // Arrange
        // Use TaggedReader as a concrete implementation of the abstract ProxyReader
        final StringReader emptySourceReader = new StringReader("");
        final TaggedReader proxyReader = new TaggedReader(emptySourceReader);
        final CharBuffer destinationBuffer = CharBuffer.allocate(4);

        // Act
        final int charsRead = proxyReader.read(destinationBuffer);

        // Assert
        assertEquals("Reading from an empty reader should return EOF", IOUtils.EOF, charsRead);
    }
}