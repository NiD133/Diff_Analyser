package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.EOFException;

public class NullReaderTest {

    @Test
    void testReadBeyondEndOfFileThrowsEOFException() {
        // Arrange:  Create a NullReader that pretends to have 0 bytes available and throws exceptions on close.
        NullReader nullReader = new NullReader(0L, false, true);

        // Act & Assert: Attempting to read from the reader should throw an EOFException.
        char[] buffer = new char[2];
        assertThrows(EOFException.class, () -> {
            nullReader.read(buffer);
        }, "Reading from a NullReader with 0 length should throw EOFException.");
    }
}