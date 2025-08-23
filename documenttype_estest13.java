package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    /**
     * Verifies that the node name for a DocumentType is always "#doctype",
     * as this is a fixed property of the node type.
     */
    @Test
    public void nodeNameIsAlwaysHashDoctype() {
        // Arrange: Create a DocumentType instance.
        // The specific name, publicId, and systemId should not affect the nodeName.
        DocumentType docType = new DocumentType("html", "", "");

        // Act: Get the node name from the instance.
        String nodeName = docType.nodeName();

        // Assert: Confirm the node name is the expected constant value.
        assertEquals("#doctype", nodeName);
    }
}