package org.apache.commons.cli;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link PatternOptionBuilder}.
 */
public class PatternOptionBuilderTest {

    /**
     * Tests that the character representing a URL is correctly identified as a value code.
     * According to the PatternOptionBuilder documentation, '/' signifies that an
     * option's argument should be parsed as a URL.
     */
    @Test
    public void isValueCodeShouldReturnTrueForUrlCharacter() {
        // Arrange: The character for a URL type.
        final char urlTypeChar = '/';

        // Act: Check if the character is a value code.
        boolean isCode = PatternOptionBuilder.isValueCode(urlTypeChar);

        // Assert: The character should be recognized as a valid value code.
        assertTrue("The URL type character '/' should be a value code.", isCode);
    }
}