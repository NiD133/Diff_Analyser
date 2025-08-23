package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite for the XML Tree Builder's handling of CDATA sections.
 */
public class XmlTreeBuilderTest {

    @Test
    @DisplayName("A CDATA block should be parsed and serialized correctly, preserving its content.")
    public void cdataBlockIsCorrectlyParsedAndSerialized() {
        // Arrange
        final String cdataContent = "\n<html>\n <foo><&amp;";
        final String xmlInput = "<div id=1><![CDATA[" + cdataContent + "]]></div>";
        final String expectedHtmlOutput = "<div id=\"1\"><![CDATA[" + cdataContent + "]]></div>";

        // Act
        Document doc = Jsoup.parse(xmlInput, "", Parser.xmlParser());
        Element div = doc.getElementById("1");

        assertNotNull(div, "The 'div' element should be found in the parsed document.");
        Node cdataNode = div.childNode(0);

        // Assert
        // 1. Verify the structure of the parsed element
        assertEquals(1, div.childNodeSize(), "The div element should contain exactly one child node.");
        assertEquals(0, div.children().size(), "The CDATA content should not be parsed into child elements.");
        assertTrue(cdataNode instanceof CDataNode, "The child node should be an instance of CDataNode.");

        // 2. Verify the content of the CDATA node
        assertEquals(cdataContent, ((CDataNode) cdataNode).text(), "The text content of the CDataNode should be preserved exactly.");
        assertEquals(cdataContent, div.text(), "The text of the parent element should match the CDATA content.");

        // 3. Verify the round-trip serialization
        assertEquals(expectedHtmlOutput, div.outerHtml(), "The serialized output should correctly represent the original CDATA block.");
    }
}