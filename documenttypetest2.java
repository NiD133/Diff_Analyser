package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the DocumentType class.
 */
public class DocumentTypeTest {

    /**
     * Tests that the systemId() getter correctly returns the value
     * provided in the constructor.
     */
    @Test
    public void systemIdShouldReturnIdSetInConstructor() {
        // Arrange: Create a DocumentType with empty strings for name and public ID,
        // and a specific value for the system ID.
        DocumentType docType = new DocumentType("", "", "systemId_value");

        // Act: Retrieve the system ID.
        String systemId = docType.systemId();

        // Assert: Verify that the retrieved system ID matches the one set in the constructor.
        assertEquals("systemId_value", systemId);
    }

    /**
     * Tests that the nodeName() method consistently returns the predefined
     * string "#doctype" for any DocumentType instance.
     */
    @Test
    public void nodeNameShouldAlwaysReturnHashDoctype() {
        // Arrange: Create a DocumentType with arbitrary values.
        // The constructor parameters do not affect the outcome of this test.
        DocumentType docType = new DocumentType("name", "publicId", "systemId");

        // Act: Retrieve the node name.
        String nodeName = docType.nodeName();

        // Assert: Verify that the node name is the constant string "#doctype".
        assertEquals("#doctype", nodeName);
    }
}