package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Elements_ESTestTest120 extends Elements_ESTest_scaffolding {

    /**
     * Tests an edge case where wrapping all elements in a document with malformed HTML
     * causes the Document node itself to be reparented, which is unexpected behavior.
     */
    @Test(timeout = 4000)
    public void wrapOnAllElementsWithMalformedHtmlAltersDocumentHierarchy() {
        // Arrange
        // A malformed HTML string used for both parsing the initial document and for the wrapper.
        // The Jsoup parser is lenient and will attempt to create a DOM from this string.
        String malformedHtml = "<m-2,eXTA)>:N5y7";
        Document document = Parser.parse(malformedHtml, malformedHtml);

        // Act
        // 1. Select all elements in the document. This includes the Document node itself,
        //    as well as the <html>, <head>, and <body> elements.
        Elements allElements = document.getAllElements();

        // 2. Wrap every single element with the same malformed HTML.
        //    This operation triggers an unusual side effect on the Document node.
        allElements.wrap(malformedHtml);

        // Assert
        // Verify the surprising side effect: the Document object itself has been reparented.
        // A Document node is expected to be the root of a DOM tree and should never have a parent.
        // This test captures a specific, non-obvious behavior of the wrap() method under these conditions.
        assertTrue("After wrapping, the document should unexpectedly have a parent.", document.hasParent());
        assertEquals("The document's sibling index should be 1.", 1, document.siblingIndex());
    }
}