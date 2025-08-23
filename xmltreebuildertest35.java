package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderTest {

    @Test
    @DisplayName("Should correctly parse an XML declaration with an encoded '>' in an attribute value")
    void parsesXmlDeclarationWithEncodedGreaterThanInAttribute() {
        // This is a regression test for https://github.com/jhy/jsoup/issues/1947.
        // It verifies that the parser correctly decodes an attribute value containing
        // an encoded greater-than sign (e.g., att2="&lt;val2>").

        // Arrange
        String xmlWithEncodedAttribute = "<x><?xmlDeclaration att1=\"value1\" att2=\"&lt;val2>\"?></x>";
        String expectedDecodedValue = "<val2>";

        // Act
        Document doc = Jsoup.parse(xmlWithEncodedAttribute, Parser.xmlParser());
        Node firstChild = doc.expectFirst("x").childNode(0);

        // Assert
        assertTrue(firstChild instanceof XmlDeclaration, "The first child node should be an XmlDeclaration.");
        XmlDeclaration decl = (XmlDeclaration) firstChild;

        assertEquals(expectedDecodedValue, decl.attr("att2"), "The encoded attribute value should be correctly decoded.");
        assertEquals("value1", decl.attr("att1"), "Other attributes should remain unaffected.");
    }
}