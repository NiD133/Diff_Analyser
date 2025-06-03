import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.apache.commons.io.input.BoundedReader;

/**
 * Test case for the BoundedReader class.  This test focuses on
 * verifying the behavior when providing a null character array to the `read` method.
 */
public class BoundedReaderNullCharArrayTest {

    /**
     * Test case to verify that a NullPointerException is thrown when a null
     * character array is passed to the `read(char[] cbuf, int off, int len)` method.
     * This test simulates a scenario where the BoundedReader is initialized with a StringReader
     * and then an attempt is made to read into a null character array.
     */
    @Test
    public void testReadWithNullCharArray() {
        // 1. Setup: Create a StringReader with some sample text.
        StringReader stringReader = new StringReader("Sample Text");

        // 2. Setup: Create a BoundedReader that wraps the StringReader with a character limit.
        //   The limit (820) is larger than the string length, so it won't immediately affect the read operation.
        BoundedReader boundedReader = new BoundedReader(stringReader, 820);

        // 3. Exercise: Attempt to read into a null character array.
        //   This is expected to throw a NullPointerException.
        assertThrows(NullPointerException.class, () -> {
            boundedReader.read((char[]) null, 103, 78);
        }, "Expected NullPointerException when reading into a null char array.");
    }
}