package org.apache.commons.collections4.properties;

import org.junit.Test;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@code computeIfAbsent} method in {@link OrderedProperties}.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that computeIfAbsent correctly adds a new key-value pair and returns
     * the new value when the key is not already present in the properties.
     */
    @Test
    public void computeIfAbsentShouldAddAndReturnValueWhenKeyIsMissing() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        
        // For this test, we use the properties' own (initially empty) entry set as a key.
        // This is an unusual but valid object to use as a key.
        final Set<Map.Entry<Object, Object>> missingKey = properties.entrySet();
        assertTrue("Precondition: The key (the entry set) should be empty", missingKey.isEmpty());
        
        // The mapping function will simply return its input (an identity function).
        // This means the value associated with the key will be the key itself.
        final Function<Object, Object> identityFunction = key -> key;

        // Act
        // Since the key is not present, the mapping function is called.
        // The result of the function is then put into the map and returned.
        final Object computedValue = properties.computeIfAbsent(missingKey, identityFunction);

        // Assert
        // 1. Verify the returned value is correct.
        // The NOPTransformer in the original test is an identity function, so the returned
        // value should be the same instance as the key.
        assertSame("The returned value should be the same instance as the key", missingKey, computedValue);

        // 2. Verify the state of the properties map has been updated correctly.
        assertEquals("Properties should now contain one entry", 1, properties.size());
        assertTrue("The key should now be present in the properties", properties.containsKey(missingKey));
        assertSame("The value for the new key should be the key itself", missingKey, properties.get(missingKey));
    }
}