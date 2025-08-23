package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the {@link Entities.EscapeMode} enum.
 */
public class EntitiesEscapeModeTest {

    @Test
    public void nameForCodepointShouldReturnEmptyStringForUnmappedCodepointInXhtmlMode() {
        // Rationale: The 'xhtml' escape mode is highly restricted and only defines named
        // entities for essential XML characters like '<', '>', '&', and '"'.
        // This test verifies that a common but unmapped character, like the horizontal tab,
        // correctly returns no entity name.

        // Arrange
        final int tabCodepoint = 9; // The codepoint for the horizontal tab character ('\t')

        // Act
        String entityName = Entities.EscapeMode.xhtml.nameForCodepoint(tabCodepoint);

        // Assert
        assertTrue("Expected an empty string for an unmapped codepoint, but got: " + entityName, entityName.isEmpty());
        // Or, for a more direct comparison:
        // assertEquals("", entityName);
    }
}