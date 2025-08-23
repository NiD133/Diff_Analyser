package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.io.IOException;

/**
 * Tests for {@link NumericEntityUnescaper}.
 */
public class NumericEntityUnescaperTest {

    /**
     * Verifies that a standard decimal numeric entity with a semicolon is correctly unescaped.
     */
    @Test
    public void shouldUnescapeDecimalEntityWithSemicolon() throws IOException {
        // Arrange: Set up the test objects and inputs.
        // The default NumericEntityUnescaper requires a semicolon for numeric entities.
        final NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        final String input = "An example: &#3; is a control character.";
        final StringWriter writer = new StringWriter();
        final int entityStartIndex = input.indexOf('&');

        // Act: Call the method under test.
        final int consumedChars = unescaper.translate(input, entityStartIndex, writer);

        // Assert: Verify the results.
        // The entity "&#3;" should be translated to the character with code point 3.
        assertEquals("The unescaped character should be correct", "\u0003", writer.toString());
        
        // The method should report consuming 4 characters: '&', '#', '3', ';'.
        assertEquals("The number of consumed characters should be correct", 4, consumedChars);
    }
}