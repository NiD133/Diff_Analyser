package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test suite for the {@link W3CDom} class, focusing on the conversion
 * from a jsoup Document to a W3C Document.
 */
public class W3CDomTest {

    /**
     * Verifies that a standard, newly-created jsoup Document is correctly converted
     * into a W3C Document, preserving the basic HTML structure (html, head, body).
     *
     * This test replaces a generated test that incorrectly expected an AssertionError.
     * The correct behavior is a successful conversion, which this test now validates.
     */
    @Test
    public void fromJsoupConvertsBasicDocumentToW3cDom() {
        // Arrange: Create a W3CDom converter and a standard jsoup Document.
        // A new jsoup Document is initialized with a default <html><head></head><body></body></html> structure.
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("http://example.com/");

        // Act: Convert the jsoup Document to a W3C Document.
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert: Verify that the conversion was successful and the basic structure is intact.
        assertNotNull("The resulting W3C document should not be null.", w3cDoc);

        // Check for the root <html> element.
        Element root = w3cDoc.getDocumentElement();
        assertNotNull("The W3C document should have a root element.", root);
        assertEquals("The root element's tag name should be 'html'.", "html", root.getTagName());

        // Check for the <head> element.
        NodeList headElements = root.getElementsByTagName("head");
        assertEquals("There should be exactly one <head> element.", 1, headElements.getLength());

        // Check for the <body> element.
        NodeList bodyElements = root.getElementsByTagName("body");
        assertEquals("There should be exactly one <body> element.", 1, bodyElements.getLength());
    }
}