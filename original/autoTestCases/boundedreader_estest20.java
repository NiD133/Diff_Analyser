package org.apache.commons.io.input;

import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;

public class UnderstandableBoundedReaderTest {

    @Test
    public void testReadWithNegativeMaxLength() throws IOException {
        // Arrange: Create a StringReader with some content.
        String data = "pI2";
        StringReader stringReader = new StringReader(data);

        // Arrange: Create a BoundedReader with a negative maximum length.
        //          This should effectively treat the reader as empty.
        BoundedReader boundedReader = new BoundedReader(stringReader, -2049);

        // Act: Attempt to read a character from the BoundedReader.
        int result = boundedReader.read();

        // Assert:  The read method should return -1, indicating the end of the stream
        //          because the maximum length is negative (meaning no characters are allowed).
        assertEquals("Reading from BoundedReader with negative length should return -1 (end of stream)", -1, result);
    }
}