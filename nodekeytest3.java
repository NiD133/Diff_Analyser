package org.jfree.data.flow;

import org.jfree.chart.TestUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;

/**
 * Tests for the {@link NodeKey} class, focusing on serialization.
 */
@DisplayName("NodeKey Serialization")
class NodeKeyTest {

    @Test
    @DisplayName("A NodeKey instance should remain equal after a serialization-deserialization round trip")
    void serializationRoundTripShouldPreserveEquality() {
        // Arrange: Create the original NodeKey instance.
        NodeKey<String> originalKey = new NodeKey<>(1, "S1");

        // Act: Serialize the original key and then deserialize it back into a new object.
        NodeKey<String> deserializedKey = TestUtils.serialised(originalKey);

        // Assert: The deserialized key should be a different instance but equal in value.
        assertNotSame(originalKey, deserializedKey, "Serialization should create a new object instance.");
        assertEquals(originalKey, deserializedKey, "The state of the object should be preserved after serialization.");
    }
}