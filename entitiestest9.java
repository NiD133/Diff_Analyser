package org.jsoup.nodes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for {@link Entities} unescaping.
 */
public class EntitiesTest {

    /**
     * Tests that the unescape method correctly handles supplementary Unicode characters.
     * Supplementary characters are those outside the Basic Multilingual Plane (BMP),
     * often represented as surrogate pairs in UTF-16.
     */
    @Test
    public void unescapesSupplementaryCharacters() {
        // Arrange
        String htmlWithEntities = "&npolint; &qfr;";

        // Define the expected characters by their Unicode code points for clarity.
        // &npolint; -> U+2A14 (N-ARY LINE INTEGRATION)
        // &qfr;     -> U+1D52E (MATHEMATICAL FRAKTUR SMALL Q)
        String expectedText = new String(Character.toChars(0x2A14)) +
                              " " +
                              new String(Character.toChars(0x1D52E));

        // Act
        String actualText = Entities.unescape(htmlWithEntities);

        // Assert
        assertEquals(expectedText, actualText);
    }
}