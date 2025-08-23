package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the Tokeniser class.
 */
public class TokeniserTest {

    /**
     * Verifies that calling transition() on a Tokeniser created from a completed
     * TreeBuilder throws a NullPointerException.
     * <p>
     * This is an edge case test. When a TreeBuilder completes parsing, it nullifies its
     * internal CharacterReader. If a Tokeniser is then initialized with this "spent"
     * TreeBuilder, it will inherit the null reader. Any subsequent state transition
     * that attempts to access the reader (e.g., to get the current position) will
     * result in an NPE.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void transitionThrowsNPEWhenTokeniserIsCreatedFromACompletedParser() {
        // Arrange:
        // 1. Create a TreeBuilder and run a full parse on a simple XML string.
        // After parsing, the builder's internal reader is set to null.
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.parse("<root/>", "http://example.com");

        // 2. Create a Tokeniser from the completed TreeBuilder.
        // This Tokeniser will now have a null CharacterReader.
        Tokeniser tokeniser = new Tokeniser(treeBuilder);
        TokeniserState targetState = TokeniserState.TagOpen;

        // Act:
        // 3. Attempt to transition to a new state. This will trigger an NPE
        // when the Tokeniser tries to access its null reader.
        tokeniser.transition(targetState);

        // Assert:
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
    }
}