import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

public class BoundedReaderTest {

    @Test
    public void testReadWithNegativeLength() throws IOException {
        // Arrange:  Create a StringReader with some text.
        StringReader stringReader = new StringReader("pI2");

        // Arrange: Create a BoundedReader with a negative limit.  This is likely invalid, but we're testing the behavior.
        BoundedReader boundedReader = new BoundedReader(stringReader, -2049);

        // Arrange: Create a character array to read into.
        char[] charArray = new char[14];

        // Act: Attempt to read into the charArray with a negative offset and negative length.  This is likely invalid.
        int bytesRead = boundedReader.read(charArray, -2049, -1);

        // Assert:  The read method should return -1, indicating that no characters were read because of the invalid parameters.  This aligns with the expected behavior for this specific test case.
        assertEquals(-1, bytesRead);
    }
}