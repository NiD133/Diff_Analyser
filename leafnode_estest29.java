package org.jsoup.nodes;

import org.junit.Test;
import static org.junit.Assert.fail;
import static org.junit.Assert.assertTrue;

/**
 * Test suite for the abstract LeafNode class, using CDataNode as a concrete implementation.
 */
public class LeafNodeTest {

    /**
     * Tests that calling attr() throws a ClassCastException if the node's internal
     * 'value' field has been corrupted to hold a non-String type.
     */
    @Test
    public void attrThrowsClassCastExceptionWhenInternalValueIsInvalid() {
        // Arrange: Use CDataNode as a concrete instance of the abstract LeafNode.
        CDataNode node = new CDataNode("some text");

        // This is a white-box test. We intentionally corrupt the internal state
        // of the LeafNode by directly setting its 'value' field to a plain Object.
        // The 'value' field is expected to be either a String or an Attributes map.
        Object invalidValue = new Object();
        node.value = invalidValue;

        // Act & Assert
        try {
            // Attempting to set an attribute should trigger an internal cast of the
            // 'value' field to a String, which will fail.
            node.attr("key", "value");
            fail("Expected a ClassCastException to be thrown due to the invalid internal state.");
        } catch (ClassCastException e) {
            // Verify that the exception is the one we expect.
            // This confirms that the internal type safety is the cause of the failure.
            String expectedMessage = "java.lang.Object cannot be cast to java.lang.String";
            assertTrue(
                "The exception message should indicate a failed cast to String.",
                e.getMessage().contains(expectedMessage)
            );
        }
    }
}