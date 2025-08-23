package org.jsoup.parser;

import org.jsoup.nodes.Element;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link XmlTreeBuilder}.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that the XmlTreeBuilder inherits the default namespace from the
     * context element when parsing an XML fragment. This is important for ensuring
     * that newly parsed nodes are placed in the correct namespace.
     */
    @Test
    public void defaultNamespaceIsInheritedFromContextElementWhenParsingFragment() {
        // Arrange
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(xmlTreeBuilder);

        // Define a context element that has a specific namespace.
        final String expectedNamespace = "http://www.example.com/namespace";
        Element contextElement = new Element("div", expectedNamespace);

        // Act
        // Initialize the tree builder by parsing a fragment with the context element.
        // The content of the fragment and base URI are not relevant to this test.
        parser.parseXmlFragment("<p>child</p>", "http://example.com/", contextElement);

        // The original test also inserted a doctype after initialization. We'll keep this
        // step to ensure the namespace state persists across subsequent operations.
        xmlTreeBuilder.insertDoctypeFor(new Token.Doctype());

        // Assert
        // The tree builder's default namespace should now be the one from the context element.
        String actualNamespace = xmlTreeBuilder.defaultNamespace();
        assertEquals(expectedNamespace, actualNamespace);
    }
}