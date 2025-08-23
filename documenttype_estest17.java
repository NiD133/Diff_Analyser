package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    /**
     * Verifies that the outer HTML of a DocumentType with empty name, publicId, and systemId
     * is rendered as a minimal, standard doctype tag.
     */
    @Test
    public void outerHtmlForEmptyDoctypeIsMinimal() {
        // Arrange: Create a DocumentType with empty attributes.
        DocumentType docType = new DocumentType("", "", "");

        // Act: Generate the outer HTML for the doctype.
        String actualHtml = docType.outerHtml();

        // Assert: The output should be the simplest possible doctype declaration.
        assertEquals("<!doctype>", actualHtml);
        
        // Also assert that the node name is correct, as a basic sanity check.
        assertEquals("#doctype", docType.nodeName());
    }
}