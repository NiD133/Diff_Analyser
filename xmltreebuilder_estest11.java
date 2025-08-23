package org.jsoup.parser;

import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link XmlTreeBuilder}.
 * This class focuses on specific behaviors of the XML tree builder.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that the default namespace of the XmlTreeBuilder is set to the base URI
     * provided when parsing an XML fragment.
     */
    @Test
    public void defaultNamespaceIsSetFromBaseUriDuringFragmentParse() {
        // Arrange
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlBuilder);
        String xmlFragment = "<item>Data</item>";
        Element context = new Element("root");
        String expectedNamespace = "http://www.w3.org/2000/svg";

        // Act
        // The parseFragment method initializes the tree builder for the fragment parse,
        // which should set the base URI.
        parser.parseFragment(xmlFragment, context, expectedNamespace);

        // Assert
        // The defaultNamespace() method is expected to return the base URI that was set.
        assertEquals(expectedNamespace, xmlBuilder.defaultNamespace());
    }
}