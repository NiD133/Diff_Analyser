package org.jsoup.select;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;

import static org.junit.Assert.assertSame;

/**
 * Test suite for the {@link Elements} class.
 * This improved test focuses on the behavior of the first() method.
 */
public class ElementsTest { // Renamed from the generated Elements_ESTestTest96

    /**
     * Verifies that Elements.first() returns the Document object itself
     * when the Document is the first item in the collection.
     */
    @Test
    public void first_whenDocumentIsFirstElementInList_returnsTheDocument() {
        // ARRANGE
        // Create a document and then an Elements collection that includes the document
        // itself as the first member. `getElementsContainingText("")` on an empty document
        // is a simple way to achieve this, as the document's own text ("") contains
        // the empty search string.
        Document document = Parser.parseBodyFragment("", "");
        Elements elementsContainingDocument = document.getElementsContainingText("");

        // ACT
        Element firstElement = elementsContainingDocument.first();

        // ASSERT
        // Verify that the returned element is the exact same instance as the original document.
        assertSame("The first element should be the document instance itself.", document, firstElement);
    }
}