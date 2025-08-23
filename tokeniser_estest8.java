package org.jsoup.parser;

import org.junit.Test;

/**
 * Tests for the {@link Tokeniser} class.
 */
public class TokeniserTest {

    /**
     * This test verifies that the Tokeniser crashes as expected when it is created from a
     * TreeBuilder that has already completed a parsing operation. This is an improper use
     * of the API, as the builder's internal state (like its character reader) is exhausted.
     * The test documents the resulting NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void readThrowsNPEWhenCreatedFromConsumedBuilder() {
        // ARRANGE: Create a builder and run a parse, which consumes its internal reader.
        // The input "< " represents an incomplete and invalid tag.
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.parse("< ", "http://example.com");

        // ACT: Create a new Tokeniser from the already-used builder.
        Tokeniser tokeniser = new Tokeniser(builder);

        // The call to read() is expected to throw a NullPointerException because the
        // Tokeniser is in an invalid state, having been initialized from a consumed builder.
        tokeniser.read();

        // ASSERT: The expected exception is declared in the @Test annotation.
    }
}