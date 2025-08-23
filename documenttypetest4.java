package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the DocumentType class.
 */
public class DocumentTypeTest {

    /**
     * Verifies that the nodeName() for a DocumentType is always "#doctype".
     *
     * The node name for a DocumentType is a fixed value and should not be
     * influenced by the name, publicId, or systemId attributes provided
     * during its construction.
     */
    @Test
    public void nodeNameShouldAlwaysReturnHashDoctype() {
        // Arrange: Create a DocumentType instance. The specific constructor arguments
        // are arbitrary for this test, as they are not expected to affect the nodeName.
        DocumentType docType = new DocumentType("html", "", "");

        // Act: Retrieve the node name from the DocumentType object.
        String actualNodeName = docType.nodeName();

        // Assert: Confirm that the retrieved node name is the constant "#doctype".
        assertEquals("#doctype", actualNodeName);
    }
}