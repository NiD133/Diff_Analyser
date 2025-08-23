package org.jsoup.helper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for {@link W3CDom}.
 * This test focuses on the conversion of Jsoup's DocumentType node.
 */
public class W3CDomTest {

    /**
     * Verifies that a Jsoup Document containing a DocumentType node is correctly
     * converted to a W3C Document, preserving the doctype's attributes.
     */
    @Test
    public void convertsJsoupDocumentTypeToW3cDocumentType() {
        // Arrange: Create a Jsoup document with a specific DocumentType.
        Document jsoupDoc = Document.createShell("");
        DocumentType jsoupDocType = new DocumentType(
            "html",
            "-//W3C//DTD XHTML 1.0 Transitional//EN",
            "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"
        );
        jsoupDoc.prependChild(jsoupDocType);

        W3CDom w3cDomConverter = new W3CDom();

        // Act: Convert the Jsoup document to a W3C DOM document.
        org.w3c.dom.Document w3cDoc = w3cDomConverter.fromJsoup(jsoupDoc);

        // Assert: Verify that the DocumentType was converted and its properties are correct.
        assertNotNull("The resulting W3C document should not be null.", w3cDoc);
        
        org.w3c.dom.DocumentType w3cDocType = w3cDoc.getDoctype();
        assertNotNull("The W3C document should have a doctype.", w3cDocType);

        assertEquals("html", w3cDocType.getName());
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", w3cDocType.getPublicId());
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", w3cDocType.getSystemId());
    }
}