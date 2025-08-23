package org.jsoup.parser;

import org.junit.Test;

/**
 * Tests edge cases for the {@link Tokeniser} class.
 */
public class Tokeniser_ESTestTest15 extends Tokeniser_ESTest_scaffolding {

    /**
     * Verifies that calling emit() on a Tokeniser before its read loop has started
     * results in a NullPointerException. This tests an invalid use of the internal API.
     */
    @Test(expected = NullPointerException.class)
    public void emitCharOnNewTokeniserWithoutReadingThrowsException() {
        // Arrange: Create a Tokeniser from an initialized TreeBuilder.
        // The TreeBuilder must first be initialized by parsing any string (e.g., an empty one)
        // to ensure it has a CharacterReader.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("", "http://example.com"); // Initializes the builder's internal state.

        // This creates a new Tokeniser instance in its default initial state.
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);

        // Act & Assert:
        // Calling emit() directly, without the read() loop managing the tokeniser's
        // internal state, is an unsupported sequence of operations. This is expected
        // to throw a NullPointerException due to the uninitialized state.
        tokeniser.emit('C');
    }
}