package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for {@link XmlTreeBuilder}.
 * This refactored test clarifies the original, auto-generated test's intent.
 */
public class XmlTreeBuilderTest {

    /**
     * Tests that the default namespace of the XmlTreeBuilder is updated from the base URI
     * when a new fragment parsing operation is initialized.
     */
    @Test
    public void defaultNamespaceIsSetFromBaseUriWhenParsingFragment() {
        // Arrange
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Parser parser = new Parser(treeBuilder);
        
        // The base URI provided to parseFragment is expected to set the builder's default namespace state.
        String expectedNamespaceUri = "http://www.w3.org/XML/1998/namespace";
        
        // A simple context element and fragment are sufficient for this test.
        Element context = new Document(""); 
        String fragment = "<data>test</data>";

        // Act
        // Initializing a fragment parse, which should update the internal state of the tree builder.
        parser.parseFragment(fragment, context, expectedNamespaceUri);

        // Assert
        // Verify that the builder's default namespace was set to the base URI of the parse.
        assertEquals(expectedNamespaceUri, treeBuilder.defaultNamespace());
    }
}