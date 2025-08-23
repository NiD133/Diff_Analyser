package org.apache.commons.lang3.text.translate;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.lang3.AbstractLangTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link NumericEntityUnescaper}.
 */
// Renamed class for clarity and to follow standard conventions.
public class NumericEntityUnescaperTest extends AbstractLangTest {

    @Test
    // Renamed test method to clearly describe the scenario under test.
    void translate_shouldUnescapeDecimalSupplementaryCharacter() {
        // Arrange
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

        // The decimal value 68642 corresponds to the Unicode code point U+10C22.
        // This is a supplementary character, which is represented by a surrogate pair in a Java String.
        final int codePoint = 68642;
        final String supplementaryCharEntity = "&#" + codePoint + ";";
        final String expectedString = new String(Character.toChars(codePoint));

        // Act
        final String actualString = unescaper.translate(supplementaryCharEntity);

        // Assert
        assertEquals(expectedString, actualString, "Failed to unescape a supplementary character from its decimal entity.");
    }
}