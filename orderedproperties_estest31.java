package org.apache.commons.collections4.properties;

import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link OrderedProperties#computeIfAbsent(Object, java.util.function.Function)} method.
 */
public class OrderedPropertiesTest {

    /**
     * Tests that computeIfAbsent returns null and does not add an entry
     * when the key is absent and the mapping function computes a null value.
     *
     * According to the Map#computeIfAbsent Javadoc, if the mapping function
     * returns null, no mapping is recorded.
     */
    @Test
    public void computeIfAbsent_whenKeyIsAbsentAndMappingFunctionReturnsNull_shouldReturnNullAndNotAddEntry() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        final String key = "non.existent.key";

        // Act: Call computeIfAbsent with a function that always returns null.
        final Object result = properties.computeIfAbsent(key, k -> null);

        // Assert
        // 1. The method should return the computed value, which is null.
        assertNull("The result of computeIfAbsent should be null", result);

        // 2. The properties map should not be modified.
        assertTrue("The properties map should remain empty", properties.isEmpty());
        assertFalse("The key should not have been added to the properties", properties.containsKey(key));
    }
}