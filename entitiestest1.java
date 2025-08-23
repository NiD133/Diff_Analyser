package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link Entities.EscapeMode} enum.
 * This class focuses on verifying the correct mapping of entity names to their corresponding codepoints.
 */
public class EntitiesEscapeModeTest {

    /**
     * Verifies that the {@code codepointForName} method returns the correct codepoint
     * for a well-known named entity ("amp") within the XHTML escape mode.
     */
    @Test
    public void givenXhtmlEscapeMode_whenCodepointForNameIsCalledWithAmp_thenReturnsCorrectCodepoint() {
        // Arrange: Define the expected entity name and its corresponding codepoint.
        // The entity "amp" corresponds to the ampersand character '&'.
        final String entityName = "amp";
        final int expectedCodepoint = 38; // Unicode codepoint for '&'
        final Entities.EscapeMode xhtmlMode = Entities.EscapeMode.xhtml;

        // Act: Retrieve the codepoint for the given entity name.
        int actualCodepoint = xhtmlMode.codepointForName(entityName);

        // Assert: Check if the retrieved codepoint matches the expected value.
        assertEquals(expectedCodepoint, actualCodepoint);
    }
}