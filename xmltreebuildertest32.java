package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.jsoup.parser.Parser.NamespaceXml;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the configuration and settings of the {@link XmlTreeBuilder}.
 */
class XmlTreeBuilderTest {

    @Test
    @DisplayName("Parser for an XML document should have XML-specific settings")
    void parserForXmlDocumentShouldHaveXmlSpecificSettings() {
        // Arrange: Define a simple XML string to be parsed.
        String xml = "<foo>";

        // Act: Parse the string using the XML parser. The resulting document's
        // parser instance should be configured with XML-specific settings.
        Document doc = Jsoup.parse(xml, Parser.xmlParser());
        Parser parser = doc.parser();
        ParseSettings settings = parser.settings();

        // Assert: Verify that the parser is configured correctly for XML.
        assertAll("XML Parser Settings",
            () -> assertTrue(settings.preserveTagCase(),
                "Tag case should be preserved in XML mode"),
            () -> assertTrue(settings.preserveAttributeCase(),
                "Attribute case should be preserved in XML mode"),
            () -> assertEquals(NamespaceXml, parser.defaultNamespace(),
                "Default namespace should be the XML namespace")
        );
    }
}