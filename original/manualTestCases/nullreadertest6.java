package org.apache.commons.io.input;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test case for the {@link TestNullReader} class, focusing on its skip functionality.
 * This test verifies that the skip method behaves correctly, including handling end-of-file scenarios.
 */
public class GeneratedTestCase {

    /**
     * Tests the {@link TestNullReader#skip(long)} method.
     * It checks the following scenarios:
     * 1. Skipping a valid number of characters.
     * 2. Skipping past the available characters before the end of the file.
     * 3. Skipping when already at the end of the file.
     * 4. Attempting to skip after the end of the file, which should throw an IOException.
     * @throws Exception If an error occurs during the test.
     */
    @Test
    public void testSkip() throws Exception {
        // Create a TestNullReader instance with a specified size, mark/reset support, and end-of-file behavior.
        try (Reader reader = new TestNullReader(10, true, false)) {

            // Read the first two characters to progress the reader.
            assertEquals(0, reader.read(), "Read 1: Should return the first character (0).");
            assertEquals(1, reader.read(), "Read 2: Should return the second character (1).");

            // Skip 5 characters.  Since we started at 0 and read two characters, the current position is 2.
            // Skipping 5 characters should advance the position to 7.
            assertEquals(5, reader.skip(5), "Skip 1: Should skip 5 characters.");

            // Read the character at position 7.
            assertEquals(7, reader.read(), "Read 3: Should return the character at position 7.");

            // Try to skip 5 more characters. Only 2 are available (positions 8 and 9) before reaching the end of the reader.
            assertEquals(2, reader.skip(5), "Skip 2: Should skip only 2 characters (remaining characters).");

            // Attempt to skip after reaching the end of the file.  This should return -1.
            assertEquals(-1, reader.skip(5), "Skip 3 (EOF): Should return -1 indicating end of file.");

            // Attempt to skip again after already reaching the end of the file.
            // This should throw an IOException with a specific message.
            final IOException e = assertThrows(IOException.class, () -> reader.skip(5), "Should throw an IOException after EOF.");
            assertEquals("Skip after end of file", e.getMessage(), "Skip after EOF IOException message: Should have the correct message.");
        }
    }
}