package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the {@link Tokeniser} class.
 * This test focuses on the behavior of the emit() method.
 */
// The original test class name and inheritance are kept for context.
public class Tokeniser_ESTestTest10 extends Tokeniser_ESTest_scaffolding {

    /**
     * Verifies that calling the emit() method with a null codepoints array
     * results in a NullPointerException.
     */
    @Test(timeout = 4000, expected = NullPointerException.class)
    public void emitWithNullCodepointsArrayShouldThrowNullPointerException() {
        // Arrange: Create a Tokeniser instance.
        // The Tokeniser requires an initialized TreeBuilder for its construction.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("", ""); // Initializes the internal reader
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);

        // Act: Call the emit method with a null argument.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        tokeniser.emit((int[]) null);
    }
}