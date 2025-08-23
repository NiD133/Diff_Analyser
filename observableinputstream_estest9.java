package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

/**
 * Test suite for the ObservableInputStream class.
 */
public class ObservableInputStreamTest {

    /**
     * Tests that calling read(byte[], int, int) with a length of zero
     * returns zero, as specified by the java.io.InputStream contract.
     * This should happen regardless of whether the underlying stream has data.
     */
    @Test(timeout = 4000)
    public void testReadWithZeroLengthShouldReturnZero() throws IOException {
        // Arrange: Set up an ObservableInputStream. The source is empty, but any
        // source would suffice since a zero-length read should not access it.
        StringWriter emptySource = new StringWriter();
        ObservableInputStream.Builder builder = new ObservableInputStream.Builder();
        builder.setCharSequence(emptySource.getBuffer());
        ObservableInputStream inputStream = new ObservableInputStream(builder);

        byte[] buffer = new byte[2];

        // Act: Attempt to read zero bytes from the stream.
        int bytesRead = inputStream.read(buffer, 0, 0);

        // Assert: The read method should return 0, confirming it adheres to the
        // InputStream contract for a zero-length read.
        assertEquals("Reading zero bytes should return 0.", 0, bytesRead);
    }
}