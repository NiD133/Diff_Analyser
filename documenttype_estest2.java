package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    @Test
    public void constructorWithEmptyStringsSetsPropertiesCorrectly() {
        // Arrange: Define empty strings for the doctype's name, publicId, and systemId.
        String name = "";
        String publicId = "";
        String systemId = "";

        // Act: Create a new DocumentType instance.
        DocumentType docType = new DocumentType(name, publicId, systemId);

        // Assert: Verify that the properties of the newly created object are set as expected.
        assertEquals("The systemId should be the empty string that was provided.", "", docType.systemId());
        assertEquals("The nodeName for a DocumentType should always be '#doctype'.", "#doctype", docType.nodeName());
    }
}