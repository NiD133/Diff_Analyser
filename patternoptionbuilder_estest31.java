package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    @Test
    public void parsePatternShouldThrowExceptionForInvalidOptionName() {
        // GIVEN a pattern string that includes an invalid character for an option name.
        // The '=' character is not a valid option name.
        final String invalidPattern = "a=b";

        try {
            // WHEN parsing the pattern
            PatternOptionBuilder.parsePattern(invalidPattern);
            fail("Expected an IllegalArgumentException to be thrown.");
        } catch (final IllegalArgumentException e) {
            // THEN an exception is thrown with a message indicating the invalid character.
            assertEquals("Illegal option name '='.", e.getMessage());
        }
    }
}