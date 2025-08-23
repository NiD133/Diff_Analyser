package org.apache.commons.cli;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
class PatternOptionBuilderTest {

    /**
     * Tests that parsePattern() throws an IllegalArgumentException when the pattern
     * string contains a character that is not a valid option name.
     */
    @Test
    void parsePatternShouldThrowExceptionForInvalidCharacter() {
        // Arrange: A pattern string containing an invalid character (a single quote).
        // According to the implementation, option names must be valid Java identifiers.
        final String invalidPattern = "a'b";

        // Act & Assert: Verify that parsing the invalid pattern throws an
        // IllegalArgumentException with the expected message.
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            PatternOptionBuilder.parsePattern(invalidPattern);
        });

        assertEquals("Illegal option name '''.", exception.getMessage());
    }
}