package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    /**
     * Verifies that the name() method correctly returns the name
     * provided in the constructor, even when it's an empty string.
     */
    @Test
    public void nameShouldReturnNameProvidedInConstructor() {
        // Arrange: Create a DocumentType with an empty name.
        // The publicId and systemId are also empty for this test case.
        DocumentType docType = new DocumentType("", "", "");

        // Act: Retrieve the name from the DocumentType instance.
        String actualName = docType.name();

        // Assert: Check that the retrieved name matches the one used during construction.
        assertEquals("The name should be an empty string as provided in the constructor.", "", actualName);
        
        // Also, verify a fundamental property: the nodeName for a DocumentType should always be "#doctype".
        assertEquals("The nodeName of a DocumentType must always be '#doctype'.", "#doctype", docType.nodeName());
    }
}