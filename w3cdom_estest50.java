package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;

/**
 * This test class focuses on an edge case for the W3CDom#convert method.
 * Note: The original class name 'W3CDom_ESTestTest50' is kept to match the file,
 * but a more descriptive name like 'W3CDomConvertEdgeCasesTest' would be preferable.
 */
public class W3CDom_ESTestTest50 extends W3CDom_ESTest_scaffolding {

    /**
     * Verifies that converting a cloned Jsoup document into a W3C document
     * that was previously populated from the *original* Jsoup document fails with an
     * AssertionError.
     * <p>
     * This test case reveals that the {@code convert} method likely has an internal
     * state check. It fails when the target W3C document is detected to be associated
     * with a different source Jsoup document instance than the one currently being converted.
     * </p>
     */
    @Test(timeout = 4000, expected = AssertionError.class)
    public void convertingClonedJsoupDocIntoExistingW3cDocShouldFail() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        Document originalJsoupDoc = Parser.parse("<html><body>jsoup</body></html>", "http://example.com/");

        // 1. Create a W3C document from the original Jsoup document.
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(originalJsoupDoc);

        // 2. Create a shallow clone of the original document. A shallow clone is a new
        // object and does not share the same identity as the original.
        Document clonedJsoupDoc = originalJsoupDoc.shallowClone();

        // Act: Attempt to convert the cloned document into the already-populated W3C document.
        // This action is expected to trigger an internal assertion failure.
        w3cDom.convert(clonedJsoupDoc, w3cDoc);

        // Assert: The test expects an AssertionError, as specified by the @Test(expected=...) annotation.
    }
}