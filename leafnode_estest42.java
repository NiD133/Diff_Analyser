package org.jsoup.nodes;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test suite for the LeafNode abstract class.
 */
class LeafNodeTest {

    @Test
    void hasAttrShouldThrowIllegalArgumentExceptionWhenKeyIsNull() {
        // Arrange: Create a concrete instance of a LeafNode.
        LeafNode node = new CDataNode("some data");

        // Act & Assert: Verify that calling hasAttr with a null key throws the expected exception.
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> node.hasAttr(null),
            "Expected hasAttr(null) to throw IllegalArgumentException, but it did not."
        );

        // Assert: Verify the exception message is correct.
        assertEquals("Object must not be null", exception.getMessage());
    }
}