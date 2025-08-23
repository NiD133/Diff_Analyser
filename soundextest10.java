package org.apache.commons.codec.language;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.codec.AbstractStringEncoderTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@link Soundex} class.
 */
// The class name has been simplified to follow standard Java testing conventions.
public class SoundexTest extends AbstractStringEncoderTest<Soundex> {

    @Override
    protected Soundex createStringEncoder() {
        return new Soundex();
    }

    @Test
    @DisplayName("encode() should ignore leading and trailing whitespace")
    void encodeShouldIgnoreLeadingAndTrailingWhitespace() {
        // Arrange: The test is structured using the Arrange-Act-Assert pattern for clarity.
        // Descriptive variable names make the test's purpose self-documenting.
        final String inputWithWhitespace = " \t\n\r Washington \t\n\r ";
        final String expectedSoundex = "W252"; // The known Soundex code for "Washington"

        // Act: The action being tested is explicitly performed.
        final String actualSoundex = getStringEncoder().encode(inputWithWhitespace);

        // Assert: The result is checked against the expected outcome.
        // An assertion message is included to provide context if the test fails.
        assertEquals(expectedSoundex, actualSoundex,
            "The Soundex encoder should effectively trim the input string before encoding.");
    }
}