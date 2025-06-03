package org.apache.commons.io.input;

import org.junit.jupiter.api.Test; // Updated import for JUnit 5
import static org.junit.jupiter.api.Assertions.*; // Updated import for JUnit 5
import java.io.IOException;
import java.io.Reader;

public class BoundedReaderTest { // More descriptive class name

    @Test
    void testResetWithNullReader() { // More descriptive method name
        // Arrange: Create a BoundedReader with a null underlying reader and a limit of 0.
        BoundedReader boundedReader = new BoundedReader(null, 0);

        // Act & Assert: Attempting to reset the reader should throw a NullPointerException.
        // This is because the reset operation attempts to delegate to the underlying reader, which is null.
        assertThrows(NullPointerException.class, () -> {
            boundedReader.reset();
        }, "Resetting a BoundedReader with a null reader should throw a NullPointerException.");
    }
}