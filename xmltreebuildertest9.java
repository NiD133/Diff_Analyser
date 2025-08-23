package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.jsoup.nodes.Document.OutputSettings.Syntax;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that when a document is parsed using the XML parser, its default
     * output syntax is correctly set to {@code Syntax.xml}.
     */
    @Test
    void xmlParserSetsOutputSyntaxToXml() {
        // Arrange
        String simpleXml = "<data/>";
        Parser xmlParser = Parser.xmlParser();

        // Act
        Document doc = Jsoup.parse(simpleXml, "", xmlParser);

        // Assert
        assertEquals(Syntax.xml, doc.outputSettings().syntax());
    }
}