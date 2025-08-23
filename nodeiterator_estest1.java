package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link NodeIterator}.
 */
public class NodeIteratorTest {

    /**
     * Verifies that nodes returned by the iterator maintain their correct parent link
     * within the document tree.
     */
    @Test
    public void nextReturnsNodeWithCorrectParent() {
        // Arrange: Create a basic document. The DOM structure will be:
        // Document -> <html> -> <head> + <body>
        Document document = Document.createShell(""); // Creates a standard empty document
        NodeIterator<Node> iterator = NodeIterator.from(document);

        // Act:
        // The first node from the iterator should be the root document itself.
        Node rootNode = iterator.next();
        // The second node should be the <html> element, the first child of the document.
        Node htmlElement = iterator.next();

        // Assert:
        // 1. Verify the root node is the document and has no parent.
        assertSame("The first node should be the document itself", document, rootNode);
        assertNull("The document root should not have a parent", rootNode.parent());

        // 2. Verify the second node is the <html> element and its parent is the document.
        assertTrue("The <html> element should have a parent", htmlElement.hasParent());
        assertSame("The <html> element's parent should be the document", document, htmlElement.parent());
        assertEquals("html", htmlElement.nodeName());
    }
}