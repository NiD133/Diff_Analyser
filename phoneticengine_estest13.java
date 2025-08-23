package org.apache.commons.codec.language.bm;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains an improved version of a test case for the PhoneticEngine.
 * The original class name `PhoneticEngine_ESTestTest13` is preserved.
 */
public class PhoneticEngine_ESTestTest13 {

    /**
     * Tests that the encode method returns an empty string when the input contains
     * special characters and no specific language rules are provided. This behavior
     * suggests that characters without a phonetic rule are effectively ignored.
     */
    @Test
    public void encodeShouldReturnEmptyStringForInputWithSpecialCharactersAndNoLanguageSet() {
        // Arrange: Create a PhoneticEngine with generic, non-concatenating rules.
        final PhoneticEngine phoneticEngine = new PhoneticEngine(NameType.GENERIC, RuleType.EXACT, false);

        // An empty language set means the engine has no language-specific context.
        final Languages.LanguageSet emptyLanguageSet = Languages.LanguageSet.from(Collections.emptySet());

        final String inputWithSpecialChars = "X&K[T6mL;i'";

        // Act: Encode the input string.
        final String result = phoneticEngine.encode(inputWithSpecialChars, emptyLanguageSet);

        // Assert: The encoded result should be an empty string.
        assertEquals("The encoded string should be empty for the given input and configuration", "", result);
    }
}