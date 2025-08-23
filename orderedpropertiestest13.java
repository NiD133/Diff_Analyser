package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the remove(key, value) method in {@link OrderedProperties}.
 */
@DisplayName("OrderedProperties#remove(key, value)")
class OrderedPropertiesTest {

    private OrderedProperties properties;

    /**
     * Creates a new OrderedProperties instance with a known, predictable
     * set of key-value pairs before each test. This ensures tests are isolated
     * and start from a consistent state.
     */
    @BeforeEach
    void setUp() {
        properties = new OrderedProperties();
        properties.put("key3", "value3");
        properties.put("key2", "value2");
        properties.put("key1", "value1");
    }

    @Test
    @DisplayName("Should remove entry and preserve order when key and value match")
    void remove_whenKeyAndValueMatch_shouldRemoveEntryAndPreserveOrder() {
        // Arrange
        final String keyToRemove = "key2";
        final String valueToRemove = "value2";
        // Pre-condition checks to ensure the initial state is correct.
        assertEquals(3, properties.size());
        assertTrue(properties.containsKey(keyToRemove));

        // Act
        final boolean wasRemoved = properties.remove(keyToRemove, valueToRemove);

        // Assert
        assertTrue(wasRemoved, "The remove method should return true on successful removal.");
        assertEquals(2, properties.size(), "The size should be reduced by one.");
        assertFalse(properties.containsKey(keyToRemove), "The key should no longer exist in the properties.");

        // Verify that the order of the remaining keys is preserved.
        final List<Object> expectedKeys = List.of("key3", "key1");
        final List<Object> actualKeys = Collections.list(properties.keys());
        assertEquals(expectedKeys, actualKeys, "The order of the remaining keys should be preserved.");
    }

    @Test
    @DisplayName("Should not remove entry when the value does not match")
    void remove_whenValueDoesNotMatch_shouldNotRemoveEntry() {
        // Arrange
        final String keyToRemove = "key2";
        final String incorrectValue = "wrong-value";
        final List<Object> originalKeys = Collections.list(properties.keys());

        // Act
        final boolean wasRemoved = properties.remove(keyToRemove, incorrectValue);

        // Assert
        assertFalse(wasRemoved, "The remove method should return false when the value does not match.");
        assertEquals(3, properties.size(), "The size should remain unchanged.");
        assertTrue(properties.containsKey(keyToRemove), "The key should still exist in the properties.");

        // Verify that the key order is unchanged.
        final List<Object> actualKeys = Collections.list(properties.keys());
        assertEquals(originalKeys, actualKeys, "The key order should be unchanged after a failed removal.");
    }
}