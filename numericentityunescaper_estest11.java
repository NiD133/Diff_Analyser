package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    /**
     * Tests that an incomplete hexadecimal entity, which is missing its hex digits,
     * is not translated and is passed through unchanged.
     */
    @Test
    public void shouldNotTranslateIncompleteHexEntityMissingDigits() {
        // Arrange
        // The default constructor creates an unescaper that requires a semicolon,
        // which is a standard configuration for this test.
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String input = "This text contains an incomplete entity &#x and should not be changed.";

        // Act
        final String result = unescaper.translate(input);

        // Assert
        // The input string should be returned as-is because the entity is malformed.
        assertEquals(input, result);
    }
}