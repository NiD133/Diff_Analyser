package org.apache.commons.collections4.properties;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

/**
 * Tests for the {@link OrderedProperties} class.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that computeIfAbsent adds a new key-value pair and returns the new value
     * when the key is not already present in the properties.
     */
    @Test
    public void computeIfAbsentShouldAddValueWhenKeyIsMissing() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        final Map<String, String> key = new HashMap<>(); // A complex object to use as a key
        final Integer valueToCompute = 123;

        assertTrue("Precondition: The properties map should be empty.", properties.isEmpty());

        // Act
        final Object result = properties.computeIfAbsent(key, k -> valueToCompute);

        // Assert
        // 1. Verify the returned value
        assertEquals("The method should return the newly computed value.", valueToCompute, result);

        // 2. Verify the state of the properties map
        assertEquals("The map size should be 1 after adding the new entry.", 1, properties.size());
        assertEquals("The map should contain the new value for the given key.", valueToCompute, properties.get(key));
    }
}