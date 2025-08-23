package org.jsoup.nodes;

import org.junit.Test;

/**
 * Tests for the {@link DocumentType} class, focusing on edge cases.
 */
public class DocumentTypeTest {

    /**
     * Tests that a StackOverflowError is thrown when an attribute-modifying method
     * is called on a node that has a circular parent reference (i.e., it is its own parent).
     * <p>
     * This scenario causes infinite recursion because modifying an attribute triggers a
     * notification that traverses up the parent tree to find the owner document.
     * When a node is its own parent, this traversal never terminates.
     */
    @Test(expected = StackOverflowError.class)
    public void setPubSysKeyOnNodeWithCircularParentReferenceThrowsStackOverflow() {
        // Arrange: Create a DocumentType node and set it as its own parent, creating a circular reference.
        DocumentType docTypeWithCircularRef = new DocumentType("html", "", "");
        docTypeWithCircularRef.setParentNode(docTypeWithCircularRef);

        // Act: Attempt to modify an attribute. This should trigger the infinite recursion.
        // The test will pass if a StackOverflowError is thrown.
        docTypeWithCircularRef.setPubSysKey("SYSTEM");
    }
}