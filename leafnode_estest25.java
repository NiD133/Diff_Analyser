package org.jsoup.nodes;

import org.junit.Test;

/**
 * Test suite for the LeafNode abstract class.
 */
public class LeafNodeTest {

    /**
     * Verifies that attribute manipulation methods fail with a ClassCastException
     * when the internal 'value' field of a LeafNode is in an inconsistent or corrupted state.
     * <p>
     * This is a white-box test that manually sets the 'value' to an invalid type (i.e., not a String
     * or an Attributes map) to ensure the code fails predictably rather than silently producing
     * incorrect behavior.
     */
    @Test(expected = ClassCastException.class)
    public void removeAttrThrowsClassCastExceptionWhenCoreValueIsCorrupted() {
        // Arrange: Create a LeafNode instance (using a concrete subclass).
        CDataNode cdataNode = new CDataNode("Initial content");

        // Arrange: Manually corrupt the internal state by assigning an invalid object type to the 'value' field.
        // The LeafNode's internal logic expects 'value' to be a String.
        cdataNode.value = cdataNode; // Set value to the node itself, which is not a String.

        // Act & Assert: Attempting to remove an attribute should now fail by throwing a
        // ClassCastException when it tries to access the 'value' as a String.
        // The @Test(expected) annotation handles the assertion.
        cdataNode.removeAttr("anyKey");
    }
}