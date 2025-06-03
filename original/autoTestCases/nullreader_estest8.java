package org.example;

import org.junit.jupiter.api.Test; // Changed import to JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Changed import to JUnit 5

public class NullReaderTest { // Renamed class for clarity

    @Test
    void testReadCharArray_WithEmptyArray_ReturnsZeroAndSupportsMark() throws IOException {
        // Arrange: Create a NullReader and an empty character array.
        NullReader nullReader = new NullReader(1254L);
        char[] emptyCharArray = new char[0];

        // Act: Read from the NullReader into the empty character array.
        int bytesRead = nullReader.read(emptyCharArray);

        // Assert:  Verify that the read method returns 0 (nothing was read) and that the NullReader supports marking.
        assertEquals(0, bytesRead, "Reading into an empty array should return 0.");
        assertTrue(nullReader.markSupported(), "NullReader should support marking.");
    }
}