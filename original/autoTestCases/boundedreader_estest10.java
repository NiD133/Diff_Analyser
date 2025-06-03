import org.junit.jupiter.api.Test; // Changed to JUnit 5
import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*; // Changed to JUnit 5

class BoundedReaderTest {

    @Test
    void testReadAfterClose() throws IOException {
        // Arrange: Create an empty StringReader and a BoundedReader wrapping it.
        StringReader stringReader = new StringReader("");
        BoundedReader boundedReader = new BoundedReader(stringReader, 2140);

        // Act: Close the BoundedReader.  This should also close the underlying StringReader.
        boundedReader.close();

        // Assert: Attempting to read from the closed BoundedReader should throw an IOException.
        //          Specifically, the StringReader, which is now closed, will throw the exception.
        IOException exception = assertThrows(IOException.class, () -> {
            boundedReader.read();
        });

        // Assert: Verify that the exception message is "Stream closed". This confirms that the
        // underlying stream was indeed closed.  This is optional but a good practice.
        assertEquals("Stream closed", exception.getMessage());
    }
}