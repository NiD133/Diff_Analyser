package org.jsoup.nodes;

import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * Test suite for NodeIterator, focusing on traversal edge cases.
 */
public class NodeIteratorTest {

    /**
     * Verifies that the NodeIterator fails with a StackOverflowError when traversing a cyclic
     * node structure (i.e., a node that is an ancestor of itself), which prevents an infinite loop.
     */
    @Test(expected = StackOverflowError.class, timeout = 4000)
    public void nextThrowsStackOverflowErrorOnCyclicNodeStructure() {
        // Arrange: Create a document and then make it a child of itself to form a cycle.
        // This simulates a malformed DOM structure that could cause infinite loops.
        Document document = Parser.parseBodyFragment("", "");
        document.prependChild(document); // The document node is now its own child.

        // Create an iterator starting from the root of this cyclic structure.
        // The specific node type to filter for (FormElement) is not important; the test
        // focuses on the traversal logic itself.
        NodeIterator<FormElement> iterator = new NodeIterator<>(document, FormElement.class);

        // Act: Attempting to find the next node in a cyclic graph should lead to
        // unbounded recursion, which is expected to result in a StackOverflowError.
        iterator.next();
    }
}