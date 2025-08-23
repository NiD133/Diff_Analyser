package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OrderedProperties} focusing on order preservation
 * with the {@code putIfAbsent} method.
 */
@DisplayName("OrderedProperties.putIfAbsent")
class OrderedPropertiesTest {

    private static final int NUM_PROPERTIES = 11;

    /**
     * Verifies that the order of properties is maintained across different access methods.
     * It checks against keySet, entrySet, keys (Enumeration), and propertyNames (Enumeration).
     *
     * @param properties The OrderedProperties instance to check.
     * @param expectedKeys The list of keys in their expected insertion order.
     */
    private void assertPropertyOrderIsPreserved(final OrderedProperties properties, final List<String> expectedKeys) {
        // 1. Verify keySet() order
        final List<Object> actualKeys = new ArrayList<>(properties.keySet());
        assertEquals(expectedKeys, actualKeys, "keySet() should preserve insertion order.");

        // 2. Verify entrySet() order for both keys and values
        final List<Map.Entry<Object, Object>> actualEntries = new ArrayList<>(properties.entrySet());
        assertEquals(expectedKeys.size(), actualEntries.size(), "entrySet() size should match expected size.");
        for (int i = 0; i < expectedKeys.size(); i++) {
            final String expectedKey = expectedKeys.get(i);
            final String expectedValue = expectedKey.replace("key", "value");
            final Map.Entry<Object, Object> actualEntry = actualEntries.get(i);

            assertEquals(expectedKey, actualEntry.getKey(), "Entry key at index " + i + " should be correct.");
            assertEquals(expectedValue, actualEntry.getValue(), "Entry value at index " + i + " should be correct.");
        }

        // 3. Verify keys() Enumeration order
        final List<Object> keysFromEnumeration = Collections.list(properties.keys());
        assertEquals(expectedKeys, keysFromEnumeration, "keys() enumeration should preserve insertion order.");

        // 4. Verify propertyNames() Enumeration order
        final List<Object> propertyNamesFromEnumeration = Collections.list(properties.propertyNames());
        assertEquals(expectedKeys, propertyNamesFromEnumeration, "propertyNames() enumeration should preserve insertion order.");
    }

    @Test
    @DisplayName("should preserve insertion order when keys are added sequentially")
    void putIfAbsentShouldPreserveAscendingInsertionOrder() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        final List<String> expectedKeys = IntStream.rangeClosed(1, NUM_PROPERTIES)
            .mapToObj(i -> "key" + i)
            .collect(Collectors.toList());

        // Act
        expectedKeys.forEach(key -> {
            final String value = key.replace("key", "value");
            properties.putIfAbsent(key, value);
        });

        // Assert
        assertPropertyOrderIsPreserved(properties, expectedKeys);
    }

    @Test
    @DisplayName("should preserve insertion order when keys are added in reverse")
    void putIfAbsentShouldPreserveDescendingInsertionOrder() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        final List<String> expectedKeys = IntStream.rangeClosed(1, NUM_PROPERTIES)
            .map(i -> NUM_PROPERTIES - i + 1) // Generates 11, 10, ..., 1
            .mapToObj(i -> "key" + i)
            .collect(Collectors.toList());

        // Act
        expectedKeys.forEach(key -> {
            final String value = key.replace("key", "value");
            properties.putIfAbsent(key, value);
        });

        // Assert
        assertPropertyOrderIsPreserved(properties, expectedKeys);
    }

    @Test
    @DisplayName("should not add, update, or reorder an element if the key already exists")
    void putIfAbsentShouldNotAffectExistingKeys() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();
        properties.put("key2", "value2");
        properties.put("key1", "value1");

        final List<String> expectedKeys = List.of("key2", "key1");

        // Act
        final Object oldValue = properties.putIfAbsent("key1", "newValue1");

        // Assert
        assertEquals("value1", oldValue, "Should return the existing value.");
        assertEquals("value1", properties.get("key1"), "Value should not be updated.");
        assertPropertyOrderIsPreserved(properties, expectedKeys);
    }
}