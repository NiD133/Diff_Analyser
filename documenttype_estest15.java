package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    /**
     * Verifies that the publicId() method correctly returns the public ID
     * that was provided during the object's construction.
     */
    @Test
    public void publicIdShouldReturnTheValueProvidedInConstructor() {
        // Arrange: Define test data and create the object under test.
        String name = "";
        String expectedPublicId = "";
        String systemId = "any-system-id"; // This value is not under test but required by the constructor.

        DocumentType documentType = new DocumentType(name, expectedPublicId, systemId);

        // Act: Call the method being tested.
        String actualPublicId = documentType.publicId();

        // Assert: Verify the results.
        assertEquals("The publicId should match the value from the constructor.",
                     expectedPublicId, actualPublicId);
        
        // Also, verify a core property of any DocumentType node.
        assertEquals("The nodeName for a DocumentType should always be '#doctype'.",
                     "#doctype", documentType.nodeName());
    }
}