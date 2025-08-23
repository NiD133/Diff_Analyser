package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertThrows;

/**
 * Test suite for {@link W3CDom}.
 * This focuses on edge cases and error handling during the conversion process.
 */
public class W3CDomTest {

    /**
     * Verifies that converting a Jsoup document with a circular reference
     * (e.g., a document that is a child of itself) results in a StackOverflowError.
     * This is expected behavior, as traversing the node tree would lead to infinite recursion.
     */
    @Test(timeout = 4000)
    public void fromJsoupWithCircularReferenceThrowsStackOverflowError() {
        // Arrange: Create a Jsoup document and introduce a circular reference
        // by making the document its own child.
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Parser.parse("<html><head></head><body></body></html>", "");
        jsoupDoc.prependChild(jsoupDoc); // The document is now its own child

        // Act & Assert: The conversion attempt should lead to infinite recursion,
        // causing a StackOverflowError. We use assertThrows to verify this explicitly.
        assertThrows(StackOverflowError.class, () -> {
            w3cDom.fromJsoup(jsoupDoc);
        });
    }
}