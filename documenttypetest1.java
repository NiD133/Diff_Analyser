package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

/**
 * Test suite for the DocumentType class.
 */
public class DocumentTypeTest {

    /**
     * Verifies that the outerHtmlHead method correctly generates the DOCTYPE string
     * for a document with both a PUBLIC and a SYSTEM identifier.
     */
    @Test
    public void outerHtmlHeadRendersPublicDoctypeCorrectly() {
        // Arrange: Set up a standard, recognizable DOCTYPE for clarity.
        String name = "html";
        String publicId = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemId = "http://www.w3.org/TR/html4/loose.dtd";
        DocumentType docType = new DocumentType(name, publicId, systemId);

        StringWriter writer = new StringWriter();
        QuietAppendable appendable = QuietAppendable.wrap(writer);
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // Construct the expected output string from the inputs to avoid "magic strings".
        String expectedHtml = "<!DOCTYPE " + name + " PUBLIC \"" + publicId + "\" \"" + systemId + "\">";

        // Act: Call the method under test to generate the DOCTYPE's outer HTML.
        docType.outerHtmlHead(appendable, outputSettings);
        String actualHtml = writer.toString();

        // Assert: Verify that the generated HTML matches the expected format.
        assertEquals(expectedHtml, actualHtml);
    }
}