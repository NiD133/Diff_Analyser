package org.apache.commons.io.input;

import org.junit.Test;
import java.io.IOException;
import java.io.StringReader;
import static org.junit.Assert.*;

public class BoundedReaderTest {

    @Test
    public void testResetEmptyStringReader() throws IOException {
        // Create a StringReader with an empty string.
        StringReader stringReader = new StringReader("");

        // Create a BoundedReader that wraps the StringReader and limits the read length to 1 character.
        BoundedReader boundedReader = new BoundedReader(stringReader, 1);

        // Reset the BoundedReader.  This should reset the underlying StringReader.
        boundedReader.reset();

        // The purpose of this test is to ensure that reset() works correctly with an empty StringReader.
        // Since there's nothing to read, no assertions are really needed beyond confirming that the reset()
        // method doesn't throw an exception. The call confirms that BoundedReader can handle reset on an empty reader.
    }
}