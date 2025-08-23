package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.DOMException;

import static org.junit.Assert.assertThrows;

/**
 * Test suite for {@link W3CDom}.
 * Note: The original test class name "W3CDom_ESTestTest4" and its dependency on a scaffolding class
 * are artifacts of a test generation tool. For better maintainability, tests are typically
 * consolidated into a single, hand-written test class like this one.
 */
public class W3CDomTest {

    /**
     * Verifies that attempting to add a root element to a W3C document that already
     * contains one will result in a DOMException.
     */
    @Test
    public void headShouldThrowExceptionWhenAddingRootToDocumentThatAlreadyHasOne() {
        // Arrange
        // 1. Create a standard Jsoup document.
        Document jsoupDoc = Document.createShell("");

        // 2. Convert it to a W3C Document. This W3C doc now has a root element (<html>).
        W3CDom w3cDomConverter = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDomConverter.fromJsoup(jsoupDoc);

        // 3. Instantiate a W3CBuilder, which is the internal class used for the conversion.
        //    Its destination is the W3C document itself.
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        final int rootNodeDepth = 0;

        // Act & Assert
        // Calling head() on the builder with the Jsoup document attempts to add another
        // root element to the W3C document. This is not allowed by the W3C DOM
        // specification (HIERARCHY_REQUEST_ERR) and must throw a DOMException.
        assertThrows(DOMException.class, () -> {
            w3cBuilder.head(jsoupDoc, rootNodeDepth);
        });
    }
}