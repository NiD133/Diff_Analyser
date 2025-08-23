package org.jsoup.nodes;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.nodes.Document.OutputSettings;
import static org.jsoup.nodes.Entities.EscapeMode.base;
import static org.jsoup.nodes.Entities.EscapeMode.extended;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link Entities} class, focusing on escaping supplementary Unicode characters.
 */
@DisplayName("Entities Escaping of Supplementary Characters")
class EntitiesTest {

    // The test character is "MATHEMATICAL DOUBLE-STRUCK SMALL H" (U+1D559, 𝕙).
    // Using its codepoint is more explicit than the surrogate pair "\uD835\uDD59".
    private static final String SUPPLEMENTARY_CHAR = new String(Character.toChars(0x1D559));

    @Test
    @DisplayName("should escape to a numeric entity for ASCII charset with 'base' mode")
    void escapeSupplementaryCharToNumericEntityInAsciiBaseMode() {
        // Arrange
        OutputSettings settings = new OutputSettings().charset("ascii").escapeMode(base);

        // Act
        String escaped = Entities.escape(SUPPLEMENTARY_CHAR, settings);

        // Assert
        assertEquals("&#x1d559;", escaped, "Should be a hexadecimal numeric entity");
    }

    @Test
    @DisplayName("should escape to a named entity for ASCII charset with 'extended' mode")
    void escapeSupplementaryCharToNamedEntityInAsciiExtendedMode() {
        // Arrange
        OutputSettings settings = new OutputSettings().charset("ascii").escapeMode(extended);

        // Act
        String escaped = Entities.escape(SUPPLEMENTARY_CHAR, settings);

        // Assert
        assertEquals("&hopf;", escaped, "Should be a named entity ('&hopf;')");
    }

    @Test
    @DisplayName("should not be escaped when the charset (e.g., UTF-8) supports it")
    void noEscapeForSupplementaryCharInSupportedCharset() {
        // Arrange: Use 'extended' mode to confirm the charset is the deciding factor.
        OutputSettings settings = new OutputSettings().charset("UTF-8").escapeMode(extended);

        // Act
        String escaped = Entities.escape(SUPPLEMENTARY_CHAR, settings);

        // Assert
        assertEquals(SUPPLEMENTARY_CHAR, escaped, "Should remain unescaped in UTF-8");
    }
}