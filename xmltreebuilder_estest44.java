package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;
import java.io.StringReader;
import static org.junit.Assert.assertEquals;

public class XmlTreeBuilder_ESTestTest44 extends XmlTreeBuilder_ESTest_scaffolding {

    /**
     * Tests that the XML parser correctly handles a simple string without any tags.
     * The string "xmlns" is notable because it's a keyword in XML for namespace declarations,
     * but here it should be treated as simple text content.
     */
    @Test
    public void parseShouldTreatSimpleStringAsTextContent() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputText = "xmlns";
        StringReader reader = new StringReader(inputText);
        String baseUri = "http://example.com";

        // Act
        Document doc = xmlTreeBuilder.parse(reader, baseUri);

        // Assert
        // The parser should produce a document whose text content is exactly the input string.
        assertEquals(inputText, doc.text());
    }
}