package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link Entities.EscapeMode} enum.
 */
public class EntitiesEscapeModeTest {

    /**
     * Verifies that codepointForName returns the correct codepoint for the "amp" entity
     * when using the xhtml escape mode. The ampersand entity (&amp;) is one of the
     * core entities defined in this mode.
     */
    @Test
    public void codepointForName_inXhtmlMode_returnsCorrectCodepointForAmp() {
        // Arrange
        Entities.EscapeMode xhtmlMode = Entities.EscapeMode.xhtml;
        final int expectedCodepoint = 38; // The ASCII codepoint for '&'

        // Act
        int actualCodepoint = xhtmlMode.codepointForName("amp");

        // Assert
        assertEquals("The codepoint for 'amp' in xhtml mode should be correct.", expectedCodepoint, actualCodepoint);
    }
}