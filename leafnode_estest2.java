package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the LeafNode abstract class.
 * As LeafNode is abstract, we use a concrete subclass like DocumentType for instantiation.
 */
public class LeafNodeTest {

    @Test
    public void shouldSetAndRetrieveBaseUri() {
        // Arrange: Create an instance of a LeafNode using its concrete subclass, DocumentType.
        DocumentType docType = new DocumentType("html", "", "");
        String expectedBaseUri = "https://jsoup.org/";

        // Act: Set the base URI. The doSetBaseUri method is the underlying implementation
        // for setting the URI on a node.
        docType.doSetBaseUri(expectedBaseUri);

        // Assert: Verify that the base URI was set correctly by retrieving it.
        String actualBaseUri = docType.baseUri();
        assertEquals(expectedBaseUri, actualBaseUri);
    }
}