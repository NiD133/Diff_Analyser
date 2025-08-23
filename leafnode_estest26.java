package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the LeafNode abstract class.
 */
public class LeafNodeTest {

    /**
     * Tests that calling attributes() on a LeafNode with a corrupted internal 'value'
     * field throws a ClassCastException. This is a white-box test verifying the
     * method's behavior under unexpected internal states.
     */
    @Test
    public void attributesThrowsClassCastExceptionWhenInternalValueIsInvalidType() {
        // Arrange: Create a CDataNode, which is a subclass of LeafNode.
        CDataNode node = new CDataNode("Initial content");

        // Act: Directly manipulate the internal 'value' field to simulate a corrupted state.
        // The 'value' field is expected to be a String or an Attributes object,
        // but here we set it to the node itself to create an invalid state.
        node.value = node;

        // Assert: Calling attributes() should result in a ClassCastException because
        // it cannot cast the invalid object type to a String.
        try {
            node.attributes();
            fail("A ClassCastException should have been thrown due to the invalid internal state.");
        } catch (ClassCastException e) {
            // The exception is expected. We verify the message to ensure it's the correct failure.
            String expectedMessage = "org.jsoup.nodes.CDataNode cannot be cast to java.lang.String";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}