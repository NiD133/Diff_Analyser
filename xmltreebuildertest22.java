package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the XmlTreeBuilder.
 */
public class XmlTreeBuilderTest {

    @Test
    @DisplayName("Parsing a CDATA section should preserve all whitespace")
    public void parsingCdataShouldPreserveWhitespace() {
        // Arrange: Define an XML string with a CDATA section containing significant whitespace.
        String xml = "<script type=\"text/javascript\">//<![CDATA[\n\n  foo();\n//]]></script>";
        String expectedText = "//\n\n  foo();\n//";

        // Act: Parse the XML string using the XML parser.
        Document doc = Jsoup.parse(xml, "", Parser.xmlParser());
        Element scriptElement = doc.selectFirst("script");
        String actualText = scriptElement.text();

        // Assert: Verify that the parsed output and the extracted text content are correct.
        assertEquals(xml, doc.outerHtml(), "The parsed document's outer HTML should match the original input string.");
        assertEquals(expectedText, actualText, "The text content of the script element should exactly match the CDATA content.");
    }
}