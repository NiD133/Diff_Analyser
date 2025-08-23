package org.jsoup.parser;

import org.junit.Test;
import static org.junit.Assert.*;

import org.jsoup.parser.Token.XmlDecl;

/**
 * This test class contains an improved version of a test for the Tokeniser.
 * The original class name `Tokeniser_ESTestTest31` and its scaffolding are omitted
 * for clarity, as they are artifacts of a test generation tool.
 */
public class TokeniserTest {

    /**
     * Verifies that createXmlDeclPending() returns a non-null, correctly initialized
     * XML declaration token.
     */
    @Test
    public void createXmlDeclPendingShouldReturnInitializedToken() {
        // Arrange: Create a Tokeniser configured for XML parsing.
        // An XmlTreeBuilder is required to instantiate a Tokeniser. It must be
        // initialized by parsing some content, though the content itself is not
        // relevant to this specific test.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<root/>", "http://example.com/");
        Tokeniser tokeniser = new Tokeniser(xmlTreeBuilder);

        // Act: Create a pending XML declaration token, setting its 'isDeclaration' flag to false.
        XmlDecl xmlDeclToken = tokeniser.createXmlDeclPending(false);

        // Assert: The method should return a valid token instance with the specified state.
        assertNotNull("The created XML declaration token should not be null.", xmlDeclToken);
        assertFalse("The 'isDeclaration' flag should be correctly set to false.", xmlDeclToken.isDeclaration);
    }
}