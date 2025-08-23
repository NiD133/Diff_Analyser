package org.apache.commons.io.input;

import static org.apache.commons.io.IOUtils.EOF;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Reader;
import org.junit.Test;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that read() returns EOF (-1) when the BoundedReader is constructed
     * with a null underlying reader. The boundary limit should have no effect in this case.
     */
    @Test
    public void testReadWithNullReaderReturnsEof() throws IOException {
        // Arrange: Create a BoundedReader with a null underlying reader.
        // The original test used a limit of -1, but any limit should yield the same result.
        // Using a positive limit makes the test's focus on the null reader more explicit.
        final Reader nullReader = null;
        final BoundedReader boundedReader = new BoundedReader(nullReader, 10);

        // Act: Attempt to read a character from the reader.
        final int result = boundedReader.read();

        // Assert: The result should be EOF, as there is no underlying reader to read from.
        assertEquals(EOF, result);
    }
}