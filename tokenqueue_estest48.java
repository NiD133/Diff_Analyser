package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the static utility methods in {@link TokenQueue}.
 */
public class TokenQueueTest {

    /**
     * Tests that the escapeCssIdentifier method correctly prepends a backslash
     * to an identifier that starts with a non-alphanumeric character.
     */
    @Test
    public void escapeCssIdentifierShouldEscapeLeadingSpecialCharacter() {
        // Arrange: An identifier starting with a character that needs escaping.
        String identifier = "%WApE_";
        String expectedEscapedIdentifier = "\\%WApE_";

        // Act: Call the method to escape the identifier.
        String actualEscapedIdentifier = TokenQueue.escapeCssIdentifier(identifier);

        // Assert: Verify that the identifier was escaped correctly.
        assertEquals(expectedEscapedIdentifier, actualEscapedIdentifier);
    }
}