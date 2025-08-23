package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the DocumentType node.
 * Note: The original test class name 'DocumentType_ESTestTest1' was preserved,
 * but a more conventional name would be 'DocumentTypeTest'.
 */
public class DocumentType_ESTestTest1 extends DocumentType_ESTest_scaffolding {

    /**
     * Verifies that the outer HTML is correctly generated for a DocumentType
     * that includes both a PUBLIC and a SYSTEM identifier.
     */
    @Test
    public void outerHtmlHeadRendersDoctypeWithPublicAndSystemIds() {
        // Arrange: Create a DocumentType using a standard, recognizable doctype
        // for clarity (HTML 4.01 Transitional).
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemId = "http://www.w3.org/TR/html4/loose.dtd";
        DocumentType docType = new DocumentType(name, publicId, systemId);

        StringWriter writer = new StringWriter();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String expectedHtml = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">";

        // Act: Render the doctype's outer HTML to the writer.
        // The QuietAppendable is an internal detail that wraps the writer.
        docType.outerHtmlHead(QuietAppendable.wrap(writer), outputSettings);
        String actualHtml = writer.toString();

        // Assert: Check if the rendered HTML matches the expected format.
        assertEquals(expectedHtml, actualHtml);
    }
}