package org.jsoup.nodes;

import org.junit.Test;

/**
 * Test suite for {@link NodeIterator}.
 * This class contains an improved version of a test case for edge-case scenarios.
 */
public class NodeIteratorTest {

    /**
     * Verifies that the NodeIterator constructor throws a ClassCastException when it
     * encounters an invalidly structured node tree during its initial traversal.
     * <p>
     * The invalid structure is a node having a non-Element node (like a CDataNode)
     * as its parent. The iterator's traversal logic is expected to assume that any
     * parent node is an Element to navigate the tree, leading to a cast failure.
     * </p>
     */
    @Test(expected = ClassCastException.class)
    public void constructorThrowsClassCastExceptionOnTreeWithNonElementParent() {
        // Arrange: Create an invalid tree structure.
        // A valid DOM tree would only have an Element or Document as a parent.
        // Here, we manually set a CDataNode as a parent to trigger the edge case.
        Element startNode = new Element("div");
        Node nonElementParent = new CDataNode("This CDataNode is an invalid parent");

        // The NodeIterator's internal logic is not designed to handle a non-Element parent.
        // We use direct field access to set up this invalid state because regular API
        // methods would prevent it.
        startNode.parentNode = nonElementParent;

        // Act: Instantiate the iterator.
        // The constructor immediately starts traversing to find the first matching node.
        // When it moves up from 'startNode' to its 'nonElementParent', the traversal
        // logic is expected to cast the parent to an Element, which will fail and
        // throw the expected ClassCastException.
        new NodeIterator<>(startNode, Element.class);
    }
}