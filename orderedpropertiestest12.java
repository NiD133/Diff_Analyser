package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for the remove() method in {@link OrderedProperties}.
 */
@DisplayName("OrderedProperties.remove()")
class OrderedPropertiesRemoveTest {

    private OrderedProperties properties;

    @BeforeEach
    void setUp() {
        // Arrange: Create a pre-populated OrderedProperties instance for each test.
        // This setup is clear, self-contained, and does not rely on external files.
        properties = new OrderedProperties();
        properties.put("key.first", "value1");
        properties.put("key.middle", "value2");
        properties.put("key.last", "value3");
    }

    @Nested
    @DisplayName("when key exists")
    class WhenKeyExists {

        @Test
        @DisplayName("should return the associated value")
        void shouldReturnValue() {
            // Act
            final Object removedValue = properties.remove("key.middle");

            // Assert
            assertEquals("value2", removedValue);
        }

        @Test
        @DisplayName("should remove the key-value pair")
        void shouldRemoveThePair() {
            // Act
            properties.remove("key.middle");

            // Assert
            assertEquals(2, properties.size(), "Size should be decremented");
            assertFalse(properties.containsKey("key.middle"), "Key should no longer be present");
        }

        @Test
        @DisplayName("should preserve the order of remaining elements")
        void shouldPreserveOrder() {
            // Act
            properties.remove("key.middle");

            // Assert
            final List<Object> expectedKeys = Arrays.asList("key.first", "key.last");

            // Verify order in the modern keySet view
            final List<Object> actualKeys = new ArrayList<>(properties.keySet());
            assertEquals(expectedKeys, actualKeys, "Order in keySet() should be maintained");

            // Verify order in the legacy propertyNames() view for backward compatibility
            final List<Object> actualPropertyNames = Collections.list(properties.propertyNames());
            assertEquals(expectedKeys, actualPropertyNames, "Order in propertyNames() should be maintained");
        }
    }

    @Nested
    @DisplayName("when key does not exist")
    class WhenKeyDoesNotExist {

        @Test
        @DisplayName("should return null")
        void shouldReturnNull() {
            // Act
            final Object removedValue = properties.remove("non.existent.key");

            // Assert
            assertNull(removedValue);
        }

        @Test
        @DisplayName("should not change the properties")
        void shouldNotChangeProperties() {
            // Act
            properties.remove("non.existent.key");

            // Assert
            final List<Object> expectedKeys = Arrays.asList("key.first", "key.middle", "key.last");
            assertEquals(3, properties.size(), "Size should remain unchanged");
            assertEquals(expectedKeys, new ArrayList<>(properties.keySet()), "Key set should remain unchanged");
        }
    }
}