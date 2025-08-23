package org.apache.commons.codec.language;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link Soundex} class, focusing on encoding behavior and default configuration.
 */
public class SoundexTest {

    /**
     * Tests that the encode method correctly processes a complex string containing mixed case letters,
     * non-alphabetic characters, and characters that test various Soundex rules.
     */
    @Test
    public void encodeShouldHandleComplexStringWithMixedCaseAndNonLetters() {
        // Arrange
        Soundex soundex = new Soundex();
        // This input string tests several Soundex rules:
        // 1. The first letter ('d') is kept and capitalized -> "D".
        // 2. Non-alphabetic characters (']', '=', '[') are ignored.
        // 3. Vowels ('a', 'i', 'e') and the special character 'w' are ignored.
        // 4. Consonants are mapped to codes: v->1, c->2, L->4.
        // 5. The resulting code is "D124", truncated to the default length of 4.
        String input = "dAiwv)]=F=ceL=T[CcF";
        String expectedCode = "D124";

        // Act
        String actualCode = soundex.encode(input);

        // Assert
        assertEquals(expectedCode, actualCode);
    }

    /**
     * Verifies that a Soundex instance created with the default constructor has a max length of 4.
     * Note: The getMaxLength() method is deprecated, but this test confirms the longstanding default behavior.
     */
    @Test
    public void defaultSoundexInstanceShouldHaveMaxLengthOfFour() {
        // Arrange
        Soundex soundex = new Soundex();
        int expectedMaxLength = 4;

        // Act
        int actualMaxLength = soundex.getMaxLength();

        // Assert
        assertEquals(expectedMaxLength, actualMaxLength);
    }
}