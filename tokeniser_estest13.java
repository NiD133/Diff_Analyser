package org.jsoup.parser;

import org.junit.Test;

/**
 * Test suite for the {@link Tokeniser} class.
 */
// The original test class name and inheritance are preserved as per the prompt.
// In a real-world scenario, this would be renamed to TokeniserTest and the scaffolding inheritance removed.
public class Tokeniser_ESTestTest13 extends Tokeniser_ESTest_scaffolding {

    /**
     * Verifies that the emit() method throws a NullPointerException when passed a null token.
     * This is crucial to ensure the method correctly handles invalid input and prevents
     * unexpected null-related errors downstream.
     */
    @Test(expected = NullPointerException.class)
    public void emitShouldThrowNullPointerExceptionForNullToken() {
        // Arrange: Create a Tokeniser instance.
        // The parse() call is a necessary step to initialize the internal state of the TreeBuilder,
        // which is required by the Tokeniser's constructor. The content being parsed is not
        // relevant to this specific test.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<p>", "https://example.com");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);

        // Act: Attempt to emit a null token.
        // The @Test(expected) annotation asserts that a NullPointerException is thrown.
        tokeniser.emit((Token) null);
    }
}