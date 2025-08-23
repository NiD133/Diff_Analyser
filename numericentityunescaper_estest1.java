package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    @Test
    public void shouldNotTranslateMalformedEntityWithNonDigitCharacter() {
        // Arrange
        // The default constructor creates an unescaper that requires a semicolon.
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String inputWithMalformedEntity = "Input with a malformed entity &#y should not be changed.";

        // Act
        final String translated = unescaper.translate(inputWithMalformedEntity);

        // Assert
        // The unescaper should not find a valid numeric entity and should return the original string.
        assertEquals(inputWithMalformedEntity, translated);
    }
}