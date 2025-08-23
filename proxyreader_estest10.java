package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Tests for the {@link ProxyReader} class.
 * This test uses {@link TaggedReader}, a concrete subclass, to test the proxying behavior.
 */
public class ProxyReaderTest {

    /**
     * Tests that the read(char[]) method correctly reads a full buffer of characters
     * from the underlying reader when sufficient data is available.
     */
    @Test
    public void readIntoCharArrayShouldFillBufferWhenDataIsAvailable() throws IOException {
        // Arrange
        final String inputData = "abcdefghijkl";
        final Reader sourceReader = new StringReader(inputData);
        final ProxyReader proxyReader = new TaggedReader(sourceReader); // Use a concrete implementation for the test

        final int bufferSize = 6;
        final char[] buffer = new char[bufferSize];
        final char[] expectedContent = "abcdef".toCharArray();

        // Act
        final int charsRead = proxyReader.read(buffer);

        // Assert
        assertEquals("The number of characters read should match the buffer size.", bufferSize, charsRead);
        assertArrayEquals("The buffer should be filled with the first characters from the source.", expectedContent, buffer);
    }
}