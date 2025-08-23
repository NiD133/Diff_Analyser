package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    @Test
    public void publicIdReturnsTheCorrectValue() {
        // Arrange: Create a DocumentType with distinct, realistic values.
        String expectedName = "html";
        String expectedPublicId = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String expectedSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        DocumentType docType = new DocumentType(expectedName, expectedPublicId, expectedSystemId);

        // Act: Retrieve the public ID from the created object.
        String actualPublicId = docType.publicId();

        // Assert: Verify that the returned public ID matches the one provided at construction.
        assertEquals(expectedPublicId, actualPublicId);
        
        // A secondary assertion to confirm the object's fundamental type is correct.
        assertEquals("#doctype", docType.nodeName());
    }
}