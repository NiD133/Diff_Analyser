package org.jsoup.parser;

import org.jsoup.nodes.Attributes;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the XmlTreeBuilder class.
 * This class focuses on namespace handling during the tree construction process.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that when a new element is inserted via insertElementFor,
     * it correctly inherits the default namespace from its parent in the parsing context.
     */
    @Test
    public void insertElementForInheritsDefaultNamespaceFromParent() {
        // Arrange
        final String expectedNamespace = "http://www.w3.org/XML/1998/namespace";
        String xmlWithNamespace = "<doc xmlns='" + expectedNamespace + "'></doc>";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Parser parser = new Parser(builder);

        // Parse the initial XML to establish a context. After this, the <doc> element
        // is on the builder's stack, and its default namespace is active.
        parser.parse(xmlWithNamespace, "");

        // Create a token for a new element to be inserted into the <doc> context.
        Token.StartTag newElementToken = new Token.StartTag();
        newElementToken.nameAttr("newChild", new Attributes());

        // Act
        // Insert the new element. It should be placed within the current namespace scope.
        builder.insertElementFor(newElementToken);

        // Assert
        // The builder's current default namespace should be the one inherited from the <doc> parent.
        String currentDefaultNamespace = builder.defaultNamespace();
        assertEquals(expectedNamespace, currentDefaultNamespace);
    }
}