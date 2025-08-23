package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    /**
     * Verifies that the systemId() method correctly returns the system ID
     * that was provided in the constructor.
     */
    @Test
    public void systemIdReturnsValueFromConstructor() {
        // Arrange: Create a DocumentType with distinct, readable values.
        String name = "html";
        String publicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String expectedSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        
        DocumentType docType = new DocumentType(name, publicId, expectedSystemId);

        // Act: Retrieve the system ID from the object.
        String actualSystemId = docType.systemId();

        // Assert: Check that the returned system ID matches the one provided.
        assertEquals(expectedSystemId, actualSystemId);
    }
}