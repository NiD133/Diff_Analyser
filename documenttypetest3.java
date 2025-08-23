package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the {@link DocumentType} class, focusing on data retrieval methods.
 */
public class DocumentTypeTest {

    /**
     * Verifies that the publicId() method correctly retrieves the public ID
     * that was set during the DocumentType object's construction.
     */
    @Test
    public void publicIdGetter_ShouldReturnCorrectValue_WhenSetInConstructor() {
        // Arrange: Define test data and create a DocumentType instance.
        String name = "html";
        String expectedPublicId = "SYSTEM";
        String systemId = "some-system-id"; // Required by constructor, but not under test.
        DocumentType docType = new DocumentType(name, expectedPublicId, systemId);

        // Act: Retrieve the public ID and node name from the object.
        String actualPublicId = docType.publicId();
        String actualNodeName = docType.nodeName();

        // Assert: Verify that the retrieved values are correct.
        assertEquals("The public ID should match the value provided in the constructor.", expectedPublicId, actualPublicId);
        assertEquals("The node name for a DocumentType should always be '#doctype'.", "#doctype", actualNodeName);
    }
}