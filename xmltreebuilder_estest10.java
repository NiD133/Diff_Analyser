package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link XmlTreeBuilder} state after parsing operations.
 */
public class XmlTreeBuilderTest {

    @Test
    public void builderRetainsDefaultNamespaceAfterParsing() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String textInput = "Some text";
        String baseUri = "http://example.com/";
        String expectedDefaultNamespace = "http://www.w3.org/XML/1998/namespace";

        // Act
        // Parse a simple string, which will be treated as a text node.
        Document document = builder.parse(textInput, baseUri);

        // This call is unusual after a full parse, but we are testing that it
        // does not corrupt the builder's internal state.
        builder.completeParseFragment();

        // Assert
        // First, a sanity check that the document was created with the correct base URI.
        assertEquals("Document location should match base URI", baseUri, document.location());

        // The main assertion: The builder's default namespace should remain unchanged
        // after the parsing operations.
        assertEquals("Builder's default namespace should be constant", expectedDefaultNamespace, builder.defaultNamespace());
    }
}