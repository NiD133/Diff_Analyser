package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    /**
     * Verifies that the name() method returns the name that was provided
     * in the constructor.
     */
    @Test
    public void nameReturnsNameProvidedInConstructor() {
        // Arrange: Create a DocumentType with a known, simple name.
        // The publicId and systemId are not relevant to this test, so they are empty.
        String expectedName = "html";
        DocumentType docType = new DocumentType(expectedName, "", "");

        // Act: Retrieve the name using the name() method.
        String actualName = docType.name();

        // Assert: Check that the retrieved name matches the one set in the constructor.
        assertEquals(expectedName, actualName);

        // Also, assert that the node name is correctly set to the constant for doctypes.
        assertEquals("#doctype", docType.nodeName());
    }
}