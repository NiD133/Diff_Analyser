package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * This test class contains tests for the W3CDom helper class.
 * This test case is a refactoring of an auto-generated test.
 */
public class W3CDomTest {

    /**
     * Verifies that the internal W3CBuilder's tail method, used during document conversion,
     * does not have unintended side-effects on the original jsoup Document.
     *
     * This test is a refactoring of an auto-generated test that called the `tail` method
     * but had an unrelated assertion. This version clarifies the most likely intent:
     * ensuring the conversion process is non-destructive to the source document.
     */
    @Test(timeout = 4000)
    public void w3cBuilderTailDoesNotModifySourceDocument() {
        // Arrange
        // 1. Create a jsoup document by parsing an empty string. This results in a
        //    document in "quirks mode" because it lacks a DOCTYPE.
        Document jsoupDoc = Parser.parse("", "https://example.com");
        assertEquals("Pre-condition: Document should be in quirks mode",
            Document.QuirksMode.quirks, jsoupDoc.quirksMode());

        // 2. Create the W3C document and the builder. The W3CBuilder is an internal
        //    component used by W3CDom to walk the jsoup node tree during conversion.
        org.w3c.dom.Document w3cDoc = W3CDom.convert(jsoupDoc);
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        int arbitraryDepth = 1; // The depth parameter is not relevant to this test's logic.

        // Act
        // Call the `tail` method directly. In a real conversion, this would be
        // invoked by a NodeTraversor after visiting a node and all of its children.
        builder.tail(jsoupDoc, arbitraryDepth);

        // Assert
        // The main point of the test: confirm that the operation on the builder
        // did not alter the state of the original jsoup document.
        assertEquals("Post-condition: Document should remain in quirks mode",
            Document.QuirksMode.quirks, jsoupDoc.quirksMode());
    }
}