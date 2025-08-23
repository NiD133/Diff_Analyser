package org.jsoup.nodes;

import org.junit.Test;

/**
 * Tests for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    /**
     * Verifies that calling outerHtmlHead with a null Appendable throws a NullPointerException.
     * This is expected behavior, as the method requires a valid destination to write to.
     */
    @Test(expected = NullPointerException.class)
    public void outerHtmlHeadThrowsExceptionForNullAppendable() {
        // Arrange: Create a standard DocumentType and OutputSettings.
        // The specific values for the doctype don't matter for this test.
        DocumentType docType = new DocumentType("html", "", "");
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // Act: Call the method with a null appendable, which should trigger the exception.
        docType.outerHtmlHead(null, outputSettings);

        // Assert: The test passes if a NullPointerException is thrown, as declared by the
        // @Test(expected=...) annotation.
    }
}