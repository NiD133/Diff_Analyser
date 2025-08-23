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
 * Tests the insertion-order-preserving behavior of the {@link OrderedProperties#put(Object, Object)} method.
 */
class OrderedPropertiesTest {

    private OrderedProperties orderedProperties;

    @BeforeEach
    void setUp() {
        orderedProperties = new OrderedProperties();
    }

    @Test
    @DisplayName("put() should maintain insertion order when adding keys sequentially")
    void putMaintainsAscendingInsertionOrder() {
        // Arrange
        final int numberOfProperties = 11;
        final List<String> expectedKeys = IntStream.rangeClosed(1, numberOfProperties)
            .mapToObj(i -> "key" + i)
            .collect(Collectors.toList());

        // Act
        expectedKeys.forEach(key -> orderedProperties.put(key, key.replace("key", "value")));

        // Assert
        assertPropertyOrder(orderedProperties, expectedKeys);
    }

    @Test
    @DisplayName("put() should maintain insertion order when adding keys in reverse")
    void putMaintainsDescendingInsertionOrder() {
        // Arrange
        final int numberOfProperties = 11;
        final List<String> expectedKeys = IntStream.rangeClosed(1, numberOfProperties)
            .map(i -> numberOfProperties - i + 1) // Generates 11, 10, ..., 1
            .mapToObj(i -> "key" + i)
            .collect(Collectors.toList());

        // Act
        expectedKeys.forEach(key -> orderedProperties.put(key, key.replace("key", "value")));

        // Assert
        assertPropertyOrder(orderedProperties, expectedKeys);
    }

    /**
     * Asserts that the various key and entry views of the OrderedProperties
     * match the expected insertion order.
     *
     * @param properties The OrderedProperties instance to check.
     * @param expectedKeys The list of keys in the expected order.
     */
    private void assertPropertyOrder(final OrderedProperties properties, final List<String> expectedKeys) {
        final List<String> expectedValues = expectedKeys.stream()
            .map(k -> k.replace("key", "value"))
            .collect(Collectors.toList());

        // 1. Verify keySet() order
        assertEquals(expectedKeys, new ArrayList<>(properties.keySet()),
            "keySet() should maintain insertion order.");

        // 2. Verify entrySet() order for both keys and values
        final List<String> actualKeysFromEntries = new ArrayList<>();
        final List<Object> actualValuesFromEntries = new ArrayList<>();
        for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
            actualKeysFromEntries.add((String) entry.getKey());
            actualValuesFromEntries.add(entry.getValue());
        }
        assertEquals(expectedKeys, actualKeysFromEntries, "Keys from entrySet() should maintain insertion order.");
        assertEquals(expectedValues, actualValuesFromEntries, "Values from entrySet() should correspond to key order.");

        // 3. Verify Enumerations from keys() and propertyNames()
        assertEquals(expectedKeys, Collections.list(properties.keys()),
            "keys() enumeration should maintain insertion order.");
        assertEquals(expectedKeys, Collections.list(properties.propertyNames()),
            "propertyNames() enumeration should maintain insertion order.");
    }
}