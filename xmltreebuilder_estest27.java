package org.jsoup.parser;

import org.jsoup.nodes.CDataNode;
import org.junit.Test;

/**
 * Test suite for {@link XmlTreeBuilder}, focusing on its internal state management.
 */
public class XmlTreeBuilderTest {

    /**
     * Verifies that attempting to insert a node into an XmlTreeBuilder
     * before it has been initialized (e.g., by a call to parse())
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void insertLeafNodeOnUninitializedBuilderThrowsException() {
        // Arrange: Create a builder but do not initialize it.
        // This means its internal document, which nodes are appended to, is null.
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        CDataNode cdataNode = new CDataNode("some cdata");

        // Act: Attempt to insert a leaf node. This should fail because there is no
        // root document to append the node to.
        xmlTreeBuilder.insertLeafNode(cdataNode);

        // Assert: The @Test(expected) annotation handles the exception assertion.
    }
}