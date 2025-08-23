package org.jfree.data.flow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Unit tests for the {@link NodeKey} class.
 */
public class NodeKeyTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the
     * 'node' argument is null, as null nodes are not permitted.
     */
    @Test
    public void constructor_withNullNode_throwsIllegalArgumentException() {
        // Arrange: Define the input arguments for the constructor call.
        int stage = 1;
        Integer nullNode = null;

        // Act & Assert: Call the constructor and verify that the expected exception is thrown.
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> new NodeKey<>(stage, nullNode)
        );

        // Assert on the exception message to ensure the correct validation failed.
        assertEquals("Null 'node' argument.", exception.getMessage());
    }
}