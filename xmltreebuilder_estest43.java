package org.jsoup.parser;

import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.jsoup.parser.Parser.NamespaceXml;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link XmlTreeBuilder} class, focusing on its initialization and namespace handling.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that initializing a fragment parse with a null context correctly
     * sets up the default XML namespace.
     */
    @Test
    public void initialiseParseFragmentWithNullContextSetsDefaultXmlNamespace() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String expectedNamespace = NamespaceXml; // "http://www.w3.org/XML/1998/namespace"

        // Act
        // Initialize the builder to parse a fragment without a specific parent context.
        xmlTreeBuilder.initialiseParseFragment((Element) null);
        String actualNamespace = xmlTreeBuilder.defaultNamespace();

        // Assert
        // The builder should be configured with the standard XML namespace by default.
        assertEquals(expectedNamespace, actualNamespace);
    }
}