package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.TextUtil;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for the {@link XmlTreeBuilder}, focusing on its ability to parse from various sources.
 */
public class XmlTreeBuilderTest {

    @Test
    @DisplayName("should correctly parse an XML document from an InputStream")
    void xmlParserCanParseFromInputStream() throws IOException {
        // Arrange
        String resourcePath = "/htmltests/xml-test.xml";
        String baseUrl = "http://foo.com";
        String expectedXml = "<doc><val>One<val>Two</val>Three</val></doc>";

        try (InputStream xmlInputStream = XmlTreeBuilder.class.getResourceAsStream(resourcePath)) {
            assertNotNull(xmlInputStream, "Test resource file not found: " + resourcePath);

            // Act
            Document doc = Jsoup.parse(xmlInputStream, null, baseUrl, Parser.xmlParser());

            // Assert
            assertEquals(expectedXml, TextUtil.stripNewlines(doc.html()));
        }
    }
}