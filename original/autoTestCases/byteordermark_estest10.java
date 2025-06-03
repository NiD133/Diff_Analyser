package org.apache.commons.io;

import org.junit.jupiter.api.Test; // Changed import to JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Changed import to JUnit 5

public class ByteOrderMarkArrayOutOfBoundsTest { // Renamed class for clarity

    @Test
    void testGetByteOrderMarkBeyondLengthThrowsException() { // Renamed method for clarity
        // Arrange: Create a ByteOrderMark. UTF-16BE uses 2 bytes.
        ByteOrderMark byteOrderMark = ByteOrderMark.UTF_16BE;

        // Act & Assert:  Attempting to access an element beyond the valid range (index 2)
        // should result in an ArrayIndexOutOfBoundsException.
        ArrayIndexOutOfBoundsException exception = assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
            byteOrderMark.get(2);
        });

        // Optional: Verify the exception message.  While EvoSuite includes this, it can increase clarity.
        assertEquals("2", exception.getMessage());  // Or a more specific message if available
    }
}