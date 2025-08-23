package org.jsoup.nodes;

import org.junit.Test;

/**
 * Tests for the {@link NodeIterator} class.
 */
public class NodeIteratorTest {

    /**
     * Verifies that the NodeIterator fails when it encounters a malformed node tree.
     * The iterator's traversal logic relies on a valid parent-child relationship, and a
     * ClassCastException is expected if this assumption is violated.
     */
    @Test(expected = ClassCastException.class)
    public void iteratorFailsFastOnMalformedNodeTree() {
        // Arrange: Create a structurally inconsistent node tree.
        // We use the protected `doClone()` method to create a Document node whose parent
        // is a CDataNode. This is an invalid state because a CDataNode is a leaf node
        // and cannot have children. This simulates a corrupted DOM structure.
        Document shellDocument = Document.createShell("");
        CDataNode invalidParent = new CDataNode("This node cannot have children");
        Node documentWithInvalidParent = shellDocument.doClone(invalidParent);

        // Act: Attempt to create an iterator starting from the malformed node.
        // The internal traversal logic is expected to fail when it encounters the
        // invalid parent type, leading to a ClassCastException.
        NodeIterator.from(documentWithInvalidParent);

        // Assert: The @Test(expected) annotation handles the exception assertion.
    }
}