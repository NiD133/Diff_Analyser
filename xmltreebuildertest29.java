package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.jsoup.nodes.Document.OutputSettings.Syntax;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link XmlTreeBuilder}, focusing on how it handles and serializes
 * malformed XML, particularly concerning invalid attribute names.
 */
public class XmlTreeBuilderTest {

    @Test
    void xmlParserSanitizesInvalidAttributeNamesOnOutput() {
        // Arrange: An XML string with several malformed attributes to test the parser's leniency.
        // Malformations include:
        // 1. In <body>: An attribute name that is just a quote (`"`), followed by a standalone attribute (`name`).
        // 2. In <div>: An attribute with an empty name (`=""`).
        String malformedXml = "<body style=\"color: red\" \" name\"><div =\"\"></div></body>";

        // The expected output shows how the parser sanitizes these invalid names upon serialization.
        // - The invalid attribute name `"` is replaced with `_`.
        // - The standalone attribute `name` is serialized as `name_=""`.
        // - The attribute with an empty name is also replaced with `_`.
        String expectedSanitizedXml = "<body style=\"color: red\" _=\"\" name_=\"\"><div _=\"\"></div></body>";

        // Act
        Document doc = Jsoup.parse(malformedXml, Parser.xmlParser());
        String actualXml = doc.html();

        // Assert
        // First, confirm the document is correctly configured for XML output.
        assertEquals(Syntax.xml, doc.outputSettings().syntax(), "Document should have XML syntax settings.");

        // Second, verify that the serialized output matches the expected sanitized string.
        assertEquals(expectedSanitizedXml, actualXml, "Invalid attribute names should be sanitized on output.");
    }
}