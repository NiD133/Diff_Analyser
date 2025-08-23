package org.apache.commons.io.input;

import org.junit.Test;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

/**
 * Tests for {@link BoundedReader}.
 */
public class BoundedReaderTest {

    /**
     * Tests that attempting to read from a BoundedReader that wraps an already
     * closed reader will propagate the IOException from the underlying reader.
     */
    @Test(expected = IOException.class)
    public void testReadOnClosedReaderThrowsIOException() throws IOException {
        // Arrange: Create a reader and close it.
        Reader underlyingReader = new StringReader("test data");
        underlyingReader.close();

        // The bound value is not relevant for this test case.
        BoundedReader boundedReader = new BoundedReader(underlyingReader, 100);

        // Act: Attempt to read from the BoundedReader.
        // This is expected to throw an IOException because the underlying reader is closed.
        boundedReader.read();
    }
}