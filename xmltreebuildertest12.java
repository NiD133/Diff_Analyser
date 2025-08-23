package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

/**
 * Tests for the {@link XmlTreeBuilder}.
 * This test focuses on parsing XML declarations.
 */
class XmlTreeBuilderTest {

    @Test
    @DisplayName("Parsing an XML declaration should correctly extract its attributes")
    void parsesXmlDeclarationAttributesCorrectly() {
        // Arrange: An XML string with a declaration containing multiple attributes.
        String xmlWithDeclaration = "<?xml version='1' encoding='UTF-8' something='else'?><val>One</val>";

        // Act: Parse the XML string using the XML parser.
        Document doc = Jsoup.parse(xmlWithDeclaration, "", Parser.xmlParser());
        Node firstChild = doc.childNode(0);

        // Assert: The first node should be an XmlDeclaration with the correct attributes.
        assertInstanceOf(XmlDeclaration.class, firstChild, "The first node should be an XmlDeclaration.");
        XmlDeclaration declaration = (XmlDeclaration) firstChild;

        // Verify individual attributes are parsed correctly
        assertEquals("1", declaration.attr("version"), "The 'version' attribute should be '1'.");
        assertEquals("UTF-8", declaration.attr("encoding"), "The 'encoding' attribute should be 'UTF-8'.");
        assertEquals("else", declaration.attr("something"), "The 'something' attribute should be 'else'.");

        // Verify the serialized output of the declaration
        assertEquals("version=\"1\" encoding=\"UTF-8\" something=\"else\"", declaration.getWholeDeclaration(),
            "The content of the declaration should be correctly serialized.");
        assertEquals("<?xml version=\"1\" encoding=\"UTF-8\" something=\"else\"?>", declaration.outerHtml(),
            "The outer HTML of the declaration should be correct.");
    }
}