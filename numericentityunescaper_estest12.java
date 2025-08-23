package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link NumericEntityUnescaper}.
 */
// Note: The original test class name and structure suggest it was auto-generated.
// This refactored version is presented as a standalone, human-written test.
public class NumericEntityUnescaperTest {

    @Test
    public void shouldNotTranslateIncompleteHexadecimalEntity() {
        // Arrange
        // The unescaper is created with default options, which means a semicolon is required for a valid entity.
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String input = "&#X"; // An incomplete hexadecimal entity (missing digits and semicolon).

        // Act
        final String result = unescaper.translate(input);

        // Assert
        // The input string should be returned unchanged because it does not form a valid numeric entity.
        assertEquals("The incomplete entity should not be translated", input, result);
    }
}