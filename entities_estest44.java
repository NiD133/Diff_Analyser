package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Entities.EscapeMode} enum.
 */
public class EntitiesEscapeModeTest {

    /**
     * Verifies that nameForCodepoint() returns the correct HTML entity name ("quot")
     * for the double-quote character codepoint.
     */
    @Test
    public void nameForCodepointShouldReturnQuotForDoubleQuote() {
        // Arrange
        final int doubleQuoteCodepoint = '"'; // Codepoint for " is 34
        final String expectedEntityName = "quot";
        final Entities.EscapeMode baseMode = Entities.EscapeMode.base;

        // Act
        String actualEntityName = baseMode.nameForCodepoint(doubleQuoteCodepoint);

        // Assert
        assertEquals(expectedEntityName, actualEntityName);
    }
}