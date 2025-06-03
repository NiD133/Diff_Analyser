package org.example;

import org.junit.jupiter.api.Test; // Use JUnit 5 annotation
import static org.junit.jupiter.api.Assertions.*; // Use JUnit 5 assertions

import java.io.IOException;

public class NullReaderTest { // Rename class to be more descriptive

    @Test
    void testResetWithoutMark() { // Rename method to describe the test's purpose
        // Arrange: Create a NullReader with a size of 0.
        NullReader nullReader = new NullReader(0L);

        // Act & Assert: Attempting to reset the reader without marking it should throw an IOException.
        IOException exception = assertThrows(IOException.class, () -> {
            nullReader.reset();
        });

        // Assert: Verify that the exception message is as expected.  This is good practice for robustness.
        assertEquals("No position has been marked", exception.getMessage());
    }
}