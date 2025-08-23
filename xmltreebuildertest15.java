package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.jsoup.nodes.Document.OutputSettings.Syntax;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for XML output generation from a Document.
 *
 * Note: This test was originally named XmlTreeBuilderTestTest15 but has been renamed.
 * It tests Document output settings rather than the XmlTreeBuilder parser.
 */
class DocumentXmlOutputTest {

    @Test
    @DisplayName("Document.outerHtml() should generate a valid XML prolog when syntax is set to XML")
    void shouldIncludeXmlPrologWhenSyntaxIsXml() {
        // Arrange
        Document document = Document.createShell(""); // Creates a doc with <html><head><body>
        document.outputSettings().syntax(Syntax.xml);
        document.charset(StandardCharsets.UTF_8);

        // The expected output string with an XML prolog.
        // Jsoup's pretty-printer produces this specific format for an empty shell document.
        String expectedXmlOutput = """
            <?xml version="1.0" encoding="UTF-8"?>
            <html>
             <head></head>
             <body></body>
            </html>""";

        // Act
        String actualXmlOutput = document.outerHtml();

        // Assert
        assertEquals(expectedXmlOutput, actualXmlOutput);
    }
}