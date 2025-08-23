package org.apache.commons.collections4.properties;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Tests for the {@link OrderedProperties} class.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that the toString() method correctly handles a self-referential map
     * to prevent infinite recursion, as per the Map.toString() contract.
     */
    @Test
    public void toStringShouldHandleSelfReferenceWithoutInfiniteRecursion() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();

        // Act: Add the map to itself as both a key and a value.
        final Object previousValue = properties.put(properties, properties);

        // Assert: The first 'put' for a new key should return null.
        assertNull("Putting a new key-value pair should return null", previousValue);

        // Act: Get the string representation of the self-referential map.
        final String toStringResult = properties.toString();

        // Assert: The output should use "(this Map)" to denote the self-reference,
        // preventing an infinite loop and a StackOverflowError.
        assertEquals("The toString() output for a self-referential map is incorrect",
                     "{(this Map)=(this Map)}", toStringResult);
    }
}