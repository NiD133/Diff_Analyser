package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * This test class contains an improved version of a test for the Tokeniser.
 * The original was auto-generated and has been refactored for clarity.
 */
public class Tokeniser_ESTestTest25 extends Tokeniser_ESTest_scaffolding {

    /**
     * Verifies that isAppropriateEndTagToken() returns false when the tokeniser
     * is in its initial state, before any start tags have been processed.
     */
    @Test
    public void isAppropriateEndTagTokenReturnsFalseWhenNoStartTagProcessed() {
        // Arrange: Create a new Tokeniser. In its initial state, no start tag has been
        // recorded, so its internal `lastStartTag` field is null.
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Tokeniser tokeniser = new Tokeniser(treeBuilder);

        // Act: Call the method under test.
        boolean isAppropriate = tokeniser.isAppropriateEndTagToken();

        // Assert: The result should be false because no opening tag has been seen yet
        // for an end tag to be considered "appropriate".
        assertFalse("isAppropriateEndTagToken() should be false when no start tag has been processed", isAppropriate);
    }
}