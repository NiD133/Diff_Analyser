package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.w3c.dom.DOMException;

import static org.junit.Assert.assertThrows;

/**
 * Test suite for {@link W3CDom}.
 * This class contains an improved version of a test case that was likely auto-generated,
 * focusing on clarity and modern testing practices.
 */
public class W3CDomTest {

    /**
     * Verifies that the {@link W3CDom#convert(org.jsoup.nodes.Element, org.w3c.dom.Document)} method
     * throws a DOMException when attempting to add a root element to a W3C document
     * that already contains one.
     */
    @Test(timeout = 4000)
    public void convertThrowsExceptionWhenAddingToDocumentThatAlreadyHasRootElement() {
        // Arrange
        W3CDom w3cDom = new W3CDom();
        // Create a basic Jsoup document, which includes <html> and <body> tags.
        Document jsoupDoc = Parser.parseBodyFragment("", "");

        // Convert the Jsoup document to a W3C document.
        // The resulting w3cDoc now has a root element (<html>).
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Act & Assert
        // Attempting to convert the same Jsoup document again into the already-populated
        // W3C document should fail, as a document cannot have more than one root element.
        assertThrows(
            "Should throw DOMException when adding a second root element",
            DOMException.class,
            () -> w3cDom.convert(jsoupDoc, w3cDoc)
        );
    }
}