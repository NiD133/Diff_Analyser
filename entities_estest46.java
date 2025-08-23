package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Entities} class, focusing on the {@link Entities.EscapeMode} enum.
 */
public class EntitiesTest {

    @Test
    public void nameForCodepointShouldReturnGtForGreaterThanInXhtmlMode() {
        // Arrange
        final Entities.EscapeMode xhtmlMode = Entities.EscapeMode.xhtml;
        final int greaterThanCodepoint = '>'; // Use character literal for clarity instead of magic number 62

        // Act
        final String entityName = xhtmlMode.nameForCodepoint(greaterThanCodepoint);

        // Assert
        assertEquals("The entity name for '>' in XHTML mode should be 'gt'", "gt", entityName);
    }
}