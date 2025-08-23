package org.jsoup.nodes;

import org.jsoup.parser.Parser;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

/**
 * This test class contains tests for the NodeIterator.
 * This particular test was refactored for clarity from an auto-generated test.
 */
public class NodeIteratorTest {

    /**
     * Verifies that calling next() on an iterator with no matching node types
     * throws a NoSuchElementException.
     */
    @Test
    public void nextThrowsNoSuchElementExceptionWhenNoMatchingNodesExist() {
        // Arrange: Create a document that does not contain the target node type (FormElement).
        String html = "<div><p>This is a sample document without any forms.</p></div>";
        Document doc = Parser.parse(html);
        Node root = doc.body(); // The node to start iteration from.

        // Act: Create an iterator that searches for a node type not present in the document.
        NodeIterator<FormElement> iterator = new NodeIterator<>(root, FormElement.class);

        // Assert: The iterator should correctly report that it has no elements.
        assertFalse("The iterator should be empty as no FormElements exist.", iterator.hasNext());

        // Assert: Calling next() on an exhausted iterator should throw the expected exception.
        try {
            iterator.next();
            fail("A NoSuchElementException should have been thrown because the iterator is exhausted.");
        } catch (NoSuchElementException expected) {
            // This is the expected behavior. The test passes.
        }
    }
}