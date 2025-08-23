package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    /**
     * Verifies that modifying the 'pubSysKey' of a DocumentType does not change its fundamental node name.
     * The node name for a DocumentType should always be "#doctype", as it represents the node's type.
     */
    @Test
    public void setPubSysKeyDoesNotAlterNodeName() {
        // Arrange: Create a DocumentType instance.
        // The specific initial values are not critical for this test.
        DocumentType docType = new DocumentType("name", "publicId", "systemId");

        // Act: Call the method under test to change an attribute.
        docType.setPubSysKey("some-new-key");

        // Assert: The node name should remain unchanged, confirming it's an invariant property.
        assertEquals("#doctype", docType.nodeName());
    }
}