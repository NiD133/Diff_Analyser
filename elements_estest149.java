package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.util.function.UnaryOperator;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the Elements class.
 * The original test was automatically generated and has been refactored for clarity.
 */
public class Elements_ESTestTest149 extends Elements_ESTest_scaffolding {

    /**
     * This test verifies that `Elements.replaceAll` correctly handles a complex scenario
     * where the list of elements contains duplicates and an element has been moved
     * within the DOM tree.
     *
     * The test creates an unusual DOM structure by making the root document a child of
     * the `<html>` element. It then ensures that `replaceAll`, even with a no-op
     * identity function, preserves this structure without errors.
     */
    @Test
    public void replaceAllShouldCorrectlyHandleListWithMovedAndDuplicateElements() {
        // ARRANGE: Create a document and manipulate its structure in a non-standard way.
        Document doc = Parser.parseBodyFragment("Some text", "http://example.com");

        // For a basic fragment, getAllElements() returns: [document, html, head, body].
        Elements elements = doc.getAllElements();

        // Intentionally create a convoluted state:
        // 1. Replace the 'body' element (at index 3) with the document element itself.
        // 2. This moves the document node to become a child of the <html> element.
        // 3. The 'elements' list now contains the document node twice: [doc, html, head, doc].
        final int bodyElementIndex = 3;
        elements.set(bodyElementIndex, doc);

        // Pre-condition check: After the 'set' operation, the document's sibling index
        // should be 1, as it is now the second child of <html> (after <head>).
        assertEquals("Pre-condition failed: Document's sibling index should be 1 after setup.",
            1, doc.siblingIndex());

        // ACT: Call replaceAll with an identity operator.
        // This operation replaces each element with itself. The purpose is to trigger the
        // internal logic of replaceAll on our unusual DOM state and ensure it behaves correctly.
        UnaryOperator<Element> identityOperator = UnaryOperator.identity();
        elements.replaceAll(identityOperator);

        // ASSERT: The document's position in the DOM should remain unchanged after the operation.
        assertEquals("Document's sibling index should not change after replaceAll.",
            1, doc.siblingIndex());
    }
}