package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the parsing of XML declarations by the XmlTreeBuilder.
 * This includes the XML prolog, DOCTYPE, and other markup declarations.
 */
@DisplayName("XmlParser Declaration Parsing")
class XmlParserDeclarationsTest {

    @Test
    @DisplayName("should correctly parse an XML declaration prolog")
    void parsesXmlDeclaration() {
        // Arrange
        String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
        
        // Act
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        XmlDeclaration xmlDecl = (XmlDeclaration) doc.childNode(0);

        // Assert
        assertAll("XML Declaration properties",
            () -> assertEquals("xml", xmlDecl.name()),
            () -> assertEquals("1.0", xmlDecl.attr("version")),
            () -> assertEquals("utf-8", xmlDecl.attr("encoding")),
            () -> assertEquals("version=\"1.0\" encoding=\"utf-8\"", xmlDecl.getWholeDeclaration()),
            () -> assertEquals(xml, xmlDecl.outerHtml())
        );
        assertEquals(xml, doc.outerHtml());
    }

    @Test
    @DisplayName("should correctly parse a DOCTYPE declaration")
    void parsesDoctypeDeclaration() {
        // Arrange
        String xml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";

        // Act
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        DocumentType doctype = (DocumentType) doc.childNode(0);

        // Assert
        assertAll("DOCTYPE properties",
            () -> assertEquals("html", doctype.name()),
            () -> assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.attr("publicId")),
            () -> assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.attr("systemId")),
            () -> assertEquals(xml, doctype.outerHtml())
        );
        assertEquals(xml, doc.outerHtml());
    }

    @Test
    @DisplayName("should correctly parse a general markup declaration (e.g., <!ELEMENT>)")
    void parsesMarkupDeclaration() {
        // Arrange
        String xml = "<!ELEMENT footnote (#PCDATA|a)*>";

        // Act
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        Node node = doc.childNode(0);

        // Assert
        // Jsoup uses the XmlDeclaration node to represent general DTD markup declarations like <!ELEMENT>, <!ATTLIST>, etc.
        assertInstanceOf(XmlDeclaration.class, node, "General markup declarations should be parsed as XmlDeclaration nodes");
        
        XmlDeclaration markupDecl = (XmlDeclaration) node;
        assertAll("Markup Declaration properties",
            () -> assertEquals("ELEMENT", markupDecl.name()),
            () -> assertEquals("footnote (#PCDATA|a)*", markupDecl.getWholeDeclaration()),
            () -> assertEquals(xml, markupDecl.outerHtml())
        );
        assertEquals(xml, doc.outerHtml());
    }
}