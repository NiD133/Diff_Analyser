package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * This test class is a refactored version of an auto-generated test.
 * The original test was named 'test29' and lacked assertions.
 */
public class Tokeniser_ESTestTest30 extends Tokeniser_ESTest_scaffolding {

    /**
     * Verifies that createDoctypePending() resets the internal doctype token to its default state.
     */
    @Test
    public void createDoctypePending_should_resetTheInternalDoctypeToken() {
        // Arrange: Create a tokeniser and give its internal doctype token a non-default state.
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.parse("", ""); // Initializes the reader required by the Tokeniser
        Tokeniser tokeniser = new Tokeniser(treeBuilder);

        // "Dirty" the doctypePending token to ensure the reset operation is being tested.
        Token.Doctype doctypeToken = tokeniser.doctypePending;
        doctypeToken.name.append("html");
        doctypeToken.publicIdentifier.append("some-public-id");
        doctypeToken.systemIdentifier.append("some-system-id");
        doctypeToken.forceQuirks = true;

        // Act: Call the method under test.
        tokeniser.createDoctypePending();

        // Assert: Confirm that all fields of the doctype token have been reset.
        // The Tokeniser reuses the same token instance, so we can assert on the original object.
        assertEquals("Doctype name should be cleared", "", doctypeToken.name.toString());
        assertEquals("Public identifier should be cleared", "", doctypeToken.publicIdentifier.toString());
        assertEquals("System identifier should be cleared", "", doctypeToken.systemIdentifier.toString());
        assertFalse("Force quirks flag should be reset to false", doctypeToken.forceQuirks);
    }
}