package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.junit.jupiter.api.Test;

import static org.jsoup.nodes.Document.OutputSettings.Syntax;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the settings and behavior of the {@link XmlTreeBuilder} when used via {@link Parser#xmlParser()}.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that when a document is parsed using the XML parser, its output settings
     * are correctly configured for XML. Specifically, the syntax should be XML
     * and the entity escape mode should be XHTML.
     */
    @Test
    public void whenParsingWithXmlParser_shouldDefaultToXmlOutputSettings() {
        // Arrange
        String xmlInput = "<root/>";
        Parser xmlParser = Parser.xmlParser();

        // Act
        Document doc = Jsoup.parse(xmlInput, "", xmlParser);
        Document.OutputSettings outputSettings = doc.outputSettings();

        // Assert
        assertEquals(Syntax.xml, outputSettings.syntax(), "Output syntax should be configured to XML");
        assertEquals(Entities.EscapeMode.xhtml, outputSettings.escapeMode(), "Escape mode should be configured to XHTML");
    }
}