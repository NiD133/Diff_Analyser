package org.jsoup.nodes;

import org.junit.Test;

/**
 * Test suite for the LeafNode abstract class, focusing on edge cases and invalid states.
 */
public class LeafNodeTest {

    /**
     * Verifies that calling attr() on a node with a circular parent reference
     * (i.e., a node that is its own parent) results in a StackOverflowError.
     * <p>
     * This is an invalid state for a node tree. This test ensures that operations that may
     * traverse the parent chain fail predictably with a StackOverflowError due to
     * infinite recursion, rather than causing an infinite loop.
     */
    @Test(timeout = 4000, expected = StackOverflowError.class)
    public void attrOnNodeWithCircularParentReferenceShouldThrowStackOverflowError() {
        // Arrange: Create a CDataNode and set its parent to itself to form a cycle.
        // CDataNode is used here as a concrete implementation of the abstract LeafNode.
        CDataNode cdataNode = new CDataNode("some data");
        cdataNode.parentNode = cdataNode; // Create the invalid circular reference.

        // Act: Attempt to set an attribute on the node. This action is expected
        // to trigger a recursive loop when traversing the parent hierarchy,
        // leading to the expected StackOverflowError.
        cdataNode.attr("key", "value");

        // Assert: The test passes if a StackOverflowError is thrown, as declared
        // by the @Test(expected=...) annotation. No further assertion code is needed.
    }
}