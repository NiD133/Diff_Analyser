package org.apache.commons.collections4.properties;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link OrderedProperties} class.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that calling put() on an existing key returns the previous value
     * associated with that key, as per the Map interface contract.
     */
    @Test
    public void put_whenKeyExists_shouldReturnOldValue() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        final String key = "user.language";
        final String oldValue = "en";
        final String newValue = "fr";

        // Set an initial value for the key.
        properties.put(key, oldValue);

        // Act
        // Update the value for the existing key and capture the returned value.
        final Object returnedValue = properties.put(key, newValue);

        // Assert
        // The put method should return the value that was previously associated with the key.
        assertEquals("The old value should be returned when a key is updated.", oldValue, returnedValue);

        // Also, verify that the new value has been correctly stored.
        assertEquals("The new value should be stored in the properties.", newValue, properties.get(key));
    }
}