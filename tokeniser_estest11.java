package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the {@link Tokeniser} class, focusing on edge cases for the emit methods.
 */
public class TokeniserTest {

    /**
     * Verifies that the emit(int[]) method throws an IllegalArgumentException
     * when provided with an array containing an invalid (negative) Unicode codepoint.
     */
    @Test(expected = IllegalArgumentException.class)
    public void emitWithNegativeCodepointThrowsIllegalArgumentException() {
        // Arrange: Create a Tokeniser and an array with an invalid codepoint.
        // The Tokeniser requires a TreeBuilder for initialization, but the parsed content is irrelevant for this test.
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.parse("", "http://example.com"); // Initialize with empty input
        Tokeniser tokeniser = new Tokeniser(treeBuilder);

        int[] invalidCodepoints = new int[]{-1}; // A negative value is not a valid codepoint.

        // Act: Attempt to emit the invalid codepoints.
        // Assert: An IllegalArgumentException is expected, as declared in the @Test annotation.
        tokeniser.emit(invalidCodepoints);
    }
}