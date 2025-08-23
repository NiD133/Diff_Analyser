package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link DocumentType} class.
 */
public class DocumentTypeTest {

    @Test
    public void nodeNameShouldAlwaysReturnHashDoctype() {
        // Arrange: Create a DocumentType instance. The constructor arguments (name, publicId, systemId)
        // are irrelevant for this test, as the nodeName is a fixed value for this type.
        DocumentType docType = new DocumentType("html", "", "");

        // Act: Retrieve the node name.
        String nodeName = docType.nodeName();

        // Assert: The node name for a DocumentType must always be "#doctype".
        assertEquals("#doctype", nodeName);
    }
}