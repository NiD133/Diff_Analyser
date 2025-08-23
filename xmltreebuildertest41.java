package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test suite for the XmlTreeBuilder, focusing on pretty-printing behavior.
 */
public class XmlTreeBuilderTest {

    @Test
    @DisplayName("XML pretty-printing of text-only elements should default to inline but can be customized to block format")
    void xmlPrettyPrintingForTextOnlyElementsCanBeCustomized() {
        // This test case addresses the behavior reported in: https://github.com/jhy/jsoup/issues/2141
        String inputXml = """
            <package>
                <metadata xmlns:dc="http://purl.org/dc/elements/1.1/">
                    <dc:identifier id="pub-id">id</dc:identifier>
                    <dc:title>title</dc:title>
                    <dc:language>ja</dc:language>
                    <dc:description>desc</dc:description>
                </metadata>
            </package>""";

        // Arrange: Parse the XML and enable pretty-printing.
        Document doc = Jsoup.parse(inputXml, Parser.xmlParser());
        doc.outputSettings().prettyPrint(true);

        // --- Part 1: Verify the default pretty-print behavior ---
        // By default, elements containing only text are formatted inline to save space.
        String expectedInlineOutput = """
            <package>
             <metadata xmlns:dc="http://purl.org/dc/elements/1.1/">
              <dc:identifier id="pub-id">id</dc:identifier> <dc:title>title</dc:title> <dc:language>ja</dc:language> <dc:description>desc</dc:description>
             </metadata>
            </package>""";

        // Act & Assert: Check the initial output.
        assertEquals(expectedInlineOutput, doc.html());

        // --- Part 2: Customize tag definitions to force block formatting ---
        // By changing the tags to be block-level, we force each element onto a new, indented line.
        Element metadataElement = doc.expectFirst("metadata");
        metadataElement.tag().set(Tag.Block); // Treat <metadata> as a block element
        for (Element child : metadataElement.children()) {
            child.tag().set(Tag.Block); // Treat each child element as a block element
        }

        String expectedBlockOutput = """
            <package>
             <metadata xmlns:dc="http://purl.org/dc/elements/1.1/">
              <dc:identifier id="pub-id">id</dc:identifier>
              <dc:title>title</dc:title>
              <dc:language>ja</dc:language>
              <dc:description>desc</dc:description>
             </metadata>
            </package>""";

        // Act & Assert: Check the output after customization.
        assertEquals(expectedBlockOutput, doc.html());
    }
}