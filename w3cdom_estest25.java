package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Test;

/**
 * Test suite for the {@link W3CDom} class, focusing on exception handling.
 */
public class W3CDomTest {

    /**
     * Verifies that the convert method throws a NullPointerException when the
     * source jsoup Document is null.
     */
    @Test(expected = NullPointerException.class)
    public void convertWithNullJsoupDocumentThrowsNullPointerException() {
        // Arrange: Create a W3CDom instance and a non-null destination W3C document.
        W3CDom w3cDom = new W3CDom();
        
        // The convert method requires a destination document. We can create one from a dummy element.
        // The content of this document is irrelevant to this test.
        Element dummyJsoupElement = new Element("html");
        org.w3c.dom.Document destinationW3cDoc = w3cDom.fromJsoup(dummyJsoupElement);

        // Act: Call the method under test with a null jsoup document.
        // This call is expected to throw a NullPointerException.
        w3cDom.convert((Document) null, destinationW3cDoc);

        // Assert: The test will pass if a NullPointerException is thrown, as declared
        // by the @Test(expected = ...) annotation.
    }
}