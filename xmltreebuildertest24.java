package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests edge cases for the {@link XmlTreeBuilder}, focusing on how it handles
 * special character sequences within script tags.
 */
public class XmlTreeBuilderTest {

    @Test
    @DisplayName("XML parser should correctly handle '<?' and '?>' sequences within a script tag")
    void xmlParserConvertsProcessingInstructionLikeTokensInScriptToComments() {
        // This test addresses an issue where processing instruction-like tokens ('<?' and '?>')
        // inside a <script> tag were not handled correctly by the XML parser.
        // See: https://github.com/jhy/jsoup/issues/1139

        // Arrange: Define the input XML and the expected output.
        String inputXml = "<script> var a=\"<?\"; var b=\"?>\"; </script>";

        // The XML parser identifies '<?' and '?>' as a processing instruction.
        // When serialized, Jsoup represents this as an XML comment.
        String expectedHtmlOutput = "<script> var a=\"<!--?\"; var b=\"?-->\"; </script>";

        // Act: Parse the input string using the XML parser.
        Document document = Jsoup.parse(inputXml, "", Parser.xmlParser());

        // Assert: Verify that the output HTML matches the expected result.
        assertEquals(expectedHtmlOutput, document.html(),
            "Processing instruction-like tokens within a script should be converted to comments.");
    }
}