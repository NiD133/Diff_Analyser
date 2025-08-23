package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the merge() method in {@link OrderedProperties} to ensure it maintains insertion order.
 */
@DisplayName("OrderedProperties.merge()")
class OrderedPropertiesTest {

    private OrderedProperties orderedProperties;

    @BeforeEach
    void setUp() {
        orderedProperties = new OrderedProperties();
    }

    /**
     * A comprehensive assertion helper that verifies the order of keys and values
     * across all relevant iteration methods of OrderedProperties.
     *
     * @param properties The OrderedProperties instance to check.
     * @param expectedKeys The list of keys in their expected order.
     */
    private void assertPropertyOrder(final OrderedProperties properties, final List<String> expectedKeys) {
        // 1. Verify keySet() order
        final List<Object> actualKeysFromKeySet = new ArrayList<>(properties.keySet());
        assertEquals(expectedKeys, actualKeysFromKeySet, "keySet() should maintain insertion order");

        // 2. Verify keys() enumeration order
        final List<Object> actualKeysFromEnum = Collections.list(properties.keys());
        assertEquals(expectedKeys, actualKeysFromEnum, "keys() enumeration should maintain insertion order");

        // 3. Verify propertyNames() enumeration order
        final List<Object> actualKeysFromPropertyNames = Collections.list(properties.propertyNames());
        assertEquals(expectedKeys, actualKeysFromPropertyNames, "propertyNames() enumeration should maintain insertion order");

        // 4. Verify entrySet() order for both keys and values
        final List<String> expectedValues = expectedKeys.stream()
                .map(key -> key.replace("key", "value"))
                .collect(Collectors.toList());

        final List<Map.Entry<Object, Object>> actualEntries = new ArrayList<>(properties.entrySet());
        assertEquals(expectedKeys.size(), actualEntries.size(), "entrySet() should have the correct number of entries");

        for (int i = 0; i < expectedKeys.size(); i++) {
            final Map.Entry<Object, Object> actualEntry = actualEntries.get(i);
            assertEquals(expectedKeys.get(i), actualEntry.getKey(), "Entry key at index " + i + " should be correct");
            assertEquals(expectedValues.get(i), actualEntry.getValue(), "Entry value at index " + i + " should be correct");
        }
    }

    @Test
    @DisplayName("maintains insertion order when properties are added in ascending sequence")
    void testMergeMaintainsAscendingInsertionOrder() {
        // Arrange
        final int numProperties = 11;
        final List<String> expectedKeys = IntStream.rangeClosed(1, numProperties)
                .mapToObj(i -> "key" + i)
                .collect(Collectors.toList());

        // Act
        for (final String key : expectedKeys) {
            final String value = key.replace("key", "value");
            orderedProperties.merge(key, value, (oldValue, newValue) -> newValue);
        }

        // Assert
        assertPropertyOrder(orderedProperties, expectedKeys);
    }

    @Test
    @DisplayName("maintains insertion order when properties are added in descending sequence")
    void testMergeMaintainsDescendingInsertionOrder() {
        // Arrange
        final int numProperties = 11;
        final List<String> expectedKeys = IntStream.rangeClosed(1, numProperties)
                .map(i -> numProperties - i + 1) // Generates numbers from 11 down to 1
                .mapToObj(i -> "key" + i)
                .collect(Collectors.toList());

        // Act
        for (final String key : expectedKeys) {
            final String value = key.replace("key", "value");
            orderedProperties.merge(key, value, (oldValue, newValue) -> newValue);
        }

        // Assert
        assertPropertyOrder(orderedProperties, expectedKeys);
    }
}