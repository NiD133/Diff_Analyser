package org.apache.commons.io.input;

import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import static org.junit.Assert.assertEquals;

public class BoundedReaderExampleTest {

    @Test
    public void testReadCharacterFromBoundedReader() throws IOException {
        // Arrange: Define a string and create a StringReader from it.
        String data = "Mqy[$oy5nF";
        StringReader stringReader = new StringReader(data);

        // Arrange: Create a BoundedReader with a maximum length of 1601 characters.
        BoundedReader boundedReader = new BoundedReader(stringReader, 1601);

        // Act: Mark the current position in the reader, allowing reset later.
        boundedReader.mark(1601); // Mark the current position (beginning of the string).

        // Act: Read the first character from the BoundedReader.
        int firstCharacter = boundedReader.read();

        // Assert: Verify that the first character read is 'M' (ASCII value 77).
        assertEquals(77, firstCharacter);
    }
}