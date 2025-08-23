package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link NumericEntityUnescaper}.
 * This class provides a more understandable version of the original test case.
 */
public class NumericEntityUnescaperTest {

    @Test
    public void shouldNotTranslateIncompleteHexadecimalEntity() {
        // Arrange
        // Create an unescaper with default options. The default behavior is to require
        // a semicolon at the end of an entity.
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String malformedInput = "&#x;";

        // Act
        final String result = unescaper.translate(malformedInput);

        // Assert
        // The input string is an incomplete hexadecimal entity because it lacks any hex digits
        // between 'x' and ';'. The unescaper should not perform any translation and should
        // return the original string.
        assertEquals("Incomplete hexadecimal entity should remain unchanged.", malformedInput, result);
    }
}