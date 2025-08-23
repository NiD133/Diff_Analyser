package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class W3CDom_ESTestTest34 extends W3CDom_ESTest_scaffolding {

    /**
     * Tests that attempting to convert a jsoup Element into a W3C Document
     * that was created from a *different* jsoup Document throws an AssertionError.
     * This is an invalid use case, as the destination document's context does not
     * match the source element's document.
     */
    @Test(timeout = 4000)
    public void convertElementIntoMismatchedW3cDocumentShouldThrowAssertionError() {
        // Arrange: Create two different jsoup documents and convert one to a W3C document.
        W3CDom w3cDom = new W3CDom();

        // The jsoup document we intend to convert.
        Document sourceJsoupDoc = new Document("http://example.com/source");
        sourceJsoupDoc.body().append("<p>Source Content</p>");

        // A *different* jsoup document, used to create the destination W3C document.
        Document otherJsoupDoc = Parser.parse("<html><body>Other Content</body></html>", "http://example.com/other");
        
        // The destination W3C document, which is internally tied to otherJsoupDoc.
        org.w3c.dom.Document destinationW3cDoc = W3CDom.convert(otherJsoupDoc);

        // Act & Assert: Attempting to convert sourceJsoupDoc into the mismatched destinationW3cDoc
        // should fail with an AssertionError due to an internal contract violation.
        try {
            w3cDom.convert(sourceJsoupDoc, destinationW3cDoc);
            fail("Expected an AssertionError to be thrown due to mismatched document contexts.");
        } catch (AssertionError e) {
            // The internal assertion that fails is not expected to have a message.
            // This confirms we are catching the specific error we anticipate.
            assertNull("The AssertionError should not have a message.", e.getMessage());
        }
    }
}