package org.jsoup.parser;

import org.junit.Test;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

/**
 * Test suite for the Tokeniser class.
 * This test focuses on behavior under specific, potentially invalid state conditions.
 */
public class TokeniserTest {

    /**
     * Verifies that calling unescapeEntities on a Tokeniser created from a used
     * TreeBuilder throws a NullPointerException.
     *
     * This scenario tests an edge case where a Tokeniser is initialized with a
     * TreeBuilder that has already completed a parsing operation. This leaves the
     * builder in a state unsuitable for reuse, causing the new Tokeniser to be
     * improperly configured (e.g., without a valid character reader), leading to an NPE.
     */
    @Test
    public void unescapeEntitiesThrowsNPEWhenTokeniserIsCreatedFromUsedBuilder() {
        // Arrange: Create a TreeBuilder and use it to parse a document.
        // This puts the builder in a "used" state, which is not intended for re-initialization.
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("<p>Some content</p>", "https://example.com");

        // Create a new Tokeniser from the already-used builder.
        Tokeniser tokeniser = new Tokeniser(builder);

        // Act & Assert: Expect a NullPointerException when using the improperly configured tokeniser.
        // The assertThrows method clearly defines the expected behavior and pinpoints the action.
        // Note: For JUnit 4.12 or older, a try-catch block is a suitable alternative.
        assertThrows(NullPointerException.class, () -> {
            tokeniser.unescapeEntities(false);
        });
    }

    /**
     * Alternative implementation for older JUnit 4 versions (pre-4.13) that do not have assertThrows.
     */
    @Test
    public void unescapeEntitiesThrowsNPEWhenTokeniserIsCreatedFromUsedBuilder_withTryCatch() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("<p>Some content</p>", "https://example.com");
        Tokeniser tokeniser = new Tokeniser(builder);

        // Act & Assert
        try {
            tokeniser.unescapeEntities(false);
            fail("Expected a NullPointerException to be thrown, but no exception occurred.");
        } catch (NullPointerException expected) {
            // The test passes, as the expected exception was caught.
        }
    }
}