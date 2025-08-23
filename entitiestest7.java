package org.jsoup.nodes;

import org.junit.jupiter.api.Test;
import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.base;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link Entities} escaping of supplementary characters.
 */
public class EntitiesTest {

    // A supplementary character is a Unicode character represented by a surrogate pair.
    // This specific character (U+210C1) cannot be encoded in ASCII.
    private static final String SUPPLEMENTARY_CHAR = new String(Character.toChars(0x210C1));

    @Test
    public void supplementaryCharIsEscapedWhenCharsetCannotEncode() {
        // Arrange: Configure output for ASCII, which cannot represent the supplementary character.
        OutputSettings asciiSettings = new OutputSettings().charset("ascii").escapeMode(base);
        String expectedEscapedHtml = "&#x210c1;";

        // Act: Escape the character using ASCII settings.
        String actualEscapedHtml = Entities.escape(SUPPLEMENTARY_CHAR, asciiSettings);

        // Assert: The character should be escaped as a hexadecimal numeric entity.
        assertEquals(expectedEscapedHtml, actualEscapedHtml);
    }

    @Test
    public void supplementaryCharIsNotEscapedWhenCharsetCanEncode() {
        // Arrange: Configure output for UTF-8, which can represent the supplementary character.
        OutputSettings utf8Settings = new OutputSettings().charset("UTF-8").escapeMode(base);

        // Act: Escape the character using UTF-8 settings.
        String actualEscapedHtml = Entities.escape(SUPPLEMENTARY_CHAR, utf8Settings);

        // Assert: The character should not be escaped, as it's supported by the charset.
        assertEquals(SUPPLEMENTARY_CHAR, actualEscapedHtml);
    }
}