package org.apache.commons.collections4.properties;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link OrderedProperties}.
 * This class focuses on specific behaviors of the OrderedProperties methods.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that putIfAbsent() returns the existing value and does not
     * replace it when the key is already present in the properties.
     */
    @Test
    public void putIfAbsent_shouldReturnExistingValue_whenKeyIsPresent() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        final String key = "existing.key";
        final String initialValue = "initial.value";
        final String newValue = "new.value";

        // Pre-populate the properties with an initial key-value pair.
        properties.setProperty(key, initialValue);

        // Act
        // Attempt to put a new value for the same key.
        final Object returnedValue = properties.putIfAbsent(key, newValue);

        // Assert
        // 1. The method should return the value that was already associated with the key.
        assertEquals("putIfAbsent should return the existing value.", initialValue, returnedValue);

        // 2. The original value for the key should remain unchanged.
        assertEquals("The value for the key should not have been updated.", initialValue, properties.getProperty(key));
    }
}