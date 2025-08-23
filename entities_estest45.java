package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the inner enum {@link Entities.EscapeMode}.
 */
public class EntitiesEscapeModeTest {

    @Test
    public void nameForCodepointShouldReturnCorrectEntityNameInExtendedMode() {
        // Arrange
        // The codepoint 340 corresponds to the Unicode character 'Ŕ' (LATIN CAPITAL LETTER R WITH ACUTE).
        final int codepoint = 340;
        final String expectedEntityName = "Racute";
        final Entities.EscapeMode escapeMode = Entities.EscapeMode.extended;

        // Act
        String actualEntityName = escapeMode.nameForCodepoint(codepoint);

        // Assert
        assertEquals("The entity name for codepoint 340 should be 'Racute'", expectedEntityName, actualEntityName);
    }
}