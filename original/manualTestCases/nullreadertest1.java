import static org.junit.jupiter.api.Assertions.*;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

import org.junit.jupiter.api.Test;

// Assuming a TestNullReader class exists that behaves as follows:
// - It returns a specified number of characters.
// - It may or may not return null characters (based on a boolean flag).
// - It may or may not throw an EOFException after the specified number of characters (based on a boolean flag).

public class TestNullReaderEOFTest { // Renamed the class for clarity

    @Test
    public void testEOFExceptionIsThrownAfterReadingConfiguredNumberOfChars() throws IOException {
        // Arrange:  Create a TestNullReader that returns 2 characters, then throws EOFException.
        int numberOfCharsToReadBeforeEOF = 2;
        boolean returnNullCharacters = false; // We don't care about null characters for this test
        boolean throwEOFException = true;

        try (Reader reader = new TestNullReader(numberOfCharsToReadBeforeEOF, returnNullCharacters, throwEOFException)) {
            // Act: Read the first two characters.  We expect these to succeed.
            int char1 = reader.read();
            int char2 = reader.read();

            // Assert:  Verify that the first two reads returned valid characters (not -1 for EOF).
            assertTrue(char1 != -1, "First read should return a character, not EOF.");
            assertTrue(char2 != -1, "Second read should return a character, not EOF.");

            // Act & Assert: Verify that the third read throws an EOFException.
            assertThrows(EOFException.class, () -> reader.read(),
                    "Expected EOFException to be thrown after reading " + numberOfCharsToReadBeforeEOF + " characters.");
        }
    }
}