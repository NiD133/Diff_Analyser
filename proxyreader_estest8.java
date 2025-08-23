package org.apache.commons.io.input;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Tests for the abstract {@link ProxyReader} class, using {@link CloseShieldReader}
 * as a concrete implementation for testing delegation behavior.
 */
public class ProxyReaderTest {

    /**
     * Tests that calling read(buffer, offset, length) on a ProxyReader
     * correctly delegates the call, reads the specified number of characters
     * into the buffer at the correct offset, and returns the correct count.
     */
    @Test
    public void testReadIntoBufferWithOffsetAndLength() throws IOException {
        // Arrange: Set up the input data and the proxy reader
        final String inputData = "ABCDE";
        // Use CloseShieldReader as a concrete implementation of the abstract ProxyReader
        final Reader proxyReader = new CloseShieldReader(new StringReader(inputData));

        final char[] buffer = new char[5];
        final int offset = 2;
        final int lengthToRead = 1;

        // Act: Call the method under test
        final int charsRead = proxyReader.read(buffer, offset, lengthToRead);

        // Assert: Verify the outcome
        // 1. Check that the method returned the correct number of characters read.
        assertEquals(1, charsRead);

        // 2. Check that the character was written to the correct position in the buffer,
        //    leaving other elements untouched (i.e., at their default char value '\u0000').
        final char[] expectedBuffer = {'\u0000', '\u0000', 'A', '\u0000', '\u0000'};
        assertArrayEquals(expectedBuffer, buffer);
    }
}