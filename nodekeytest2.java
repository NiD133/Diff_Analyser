package org.jfree.data.flow;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests for the {@link NodeKey} class, focusing on its cloning behavior.
 */
@DisplayName("A NodeKey")
class NodeKeyTest {

    @Test
    @DisplayName("should produce an equal but distinct instance when cloned")
    void cloneCreatesEqualButNotSameInstance() throws CloneNotSupportedException {
        // Arrange: Create an original NodeKey instance.
        var originalKey = new NodeKey<>(2, "A");

        // Act: Create a clone of the original key.
        var clonedKey = (NodeKey<?>) originalKey.clone();

        // Assert: Verify the properties of a correct clone.
        // 1. The clone must be a different object in memory.
        assertNotSame(originalKey, clonedKey,
                "A clone must be a different object instance from the original.");

        // 2. The clone must be equal to the original based on its state.
        assertEquals(originalKey, clonedKey,
                "A clone must be equal to the original.");

        // 3. The clone must be of the exact same class as the original.
        assertSame(originalKey.getClass(), clonedKey.getClass(),
                "A clone's class must be identical to the original's.");
    }
}