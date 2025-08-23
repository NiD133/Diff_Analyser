package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.jsoup.nodes.Document;

/**
 * This test was originally part of an auto-generated suite for NodeIterator,
 * but it actually verifies the behavior of the Document.wholeText() method.
 * It has been refactored for clarity.
 */
public class NodeIterator_ESTestTest13 { // Note: Class name kept for context.

    /**
     * Verifies that wholeText() returns an empty string for a document
     * that contains only element nodes and no text nodes.
     */
    @Test
    public void wholeTextShouldReturnEmptyStringForDocumentWithOnlyElements() {
        // Arrange: Create a document with a standard shell (html, head, body)
        // and add a child element. This structure contains no TextNodes.
        Document doc = Document.createShell("");
        doc.body().appendElement("div");

        // Act: Get the combined text of the entire document.
        String combinedText = doc.wholeText();

        // Assert: The result should be an empty string, as no text nodes exist.
        assertEquals("wholeText() should be empty for a document with no text nodes", "", combinedText);
    }
}