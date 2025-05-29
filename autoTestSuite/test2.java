import org.junit.jupiter.api.Test; // Use JUnit 5
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.assertEquals; // Use JUnit 5 Assertions

/**
 * Test case for the BoundedReader class.  This test focuses on reading an empty StringReader
 * with a BoundedReader and verifies that reading zero characters from an empty reader
 * returns 0, as expected.
 */
public class BoundedReaderTest {

    /**
     * Tests reading zero characters from a BoundedReader wrapping an empty StringReader.
     *
     * @throws IOException if an I/O error occurs (though it shouldn't in this case).
     */
    @Test
    public void testReadZeroCharsFromEmptyReader() throws IOException {
        // 1. Setup: Create an empty StringReader
        StringReader emptyReader = new StringReader("");

        // 2. Setup: Create a BoundedReader with a large bound (larger than the string).
        //    This ensures the bound doesn't affect the test.
        BoundedReader boundedReader = new BoundedReader(emptyReader, 1148);

        // 3. Setup: Create a character array to read into (size 1 is sufficient for this test).
        char[] buffer = new char[1];

        // 4. Execution: Attempt to read 0 characters from the BoundedReader into the buffer, starting at index 0.
        int bytesRead = boundedReader.read(buffer, 0, 0);

        // 5. Assertion: Verify that the read() method returns 0, indicating that 0 characters were read.
        assertEquals(0, bytesRead, "Reading 0 characters from an empty BoundedReader should return 0.");
    }
}