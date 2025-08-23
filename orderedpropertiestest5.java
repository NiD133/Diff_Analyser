package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OrderedProperties} to ensure that the insertion order of properties is preserved.
 */
class OrderedPropertiesTest {

    /**
     * Tests that properties added programmatically are returned in their insertion order
     * across all relevant views (keySet, entrySet, keys, and propertyNames).
     */
    @Test
    void shouldPreserveInsertionOrderForProgrammaticallyAddedProperties() {
        // Arrange: Create properties and define the expected order.
        // We add keys in reverse alphabetical order ('Z' down to 'A').
        final OrderedProperties properties = new OrderedProperties();
        final List<String> expectedKeys = new ArrayList<>();
        final List<Entry<Object, Object>> expectedEntries = new ArrayList<>();

        for (char ch = 'Z'; ch >= 'A'; ch--) {
            final String key = String.valueOf(ch);
            final String value = "Value" + ch;
            properties.put(key, value);

            // Build expected lists in parallel to ensure order matches insertion.
            expectedKeys.add(key);
            expectedEntries.add(new SimpleEntry<>(key, value));
        }

        // Act & Assert: Verify that all views return elements in the insertion order.

        // 1. Test keySet()
        assertIterableEquals(expectedKeys, properties.keySet(),
            "keySet() should preserve insertion order.");

        // 2. Test entrySet()
        assertIterableEquals(expectedEntries, properties.entrySet(),
            "entrySet() should preserve insertion order.");

        // 3. Test keys() Enumeration
        final List<Object> actualKeysFromEnum = Collections.list(properties.keys());
        assertIterableEquals(expectedKeys, actualKeysFromEnum,
            "keys() enumeration should preserve insertion order.");

        // 4. Test propertyNames() Enumeration
        final List<Object> actualPropertyNames = Collections.list(properties.propertyNames());
        assertIterableEquals(expectedKeys, actualPropertyNames,
            "propertyNames() enumeration should preserve insertion order.");
    }

    /**
     * Tests that properties loaded from a file are maintained in the order they appear in the file.
     * This test relies on 'test-reverse.properties', which is expected to list keys
     * from "key11" down to "key1".
     */
    @Test
    void shouldPreserveOrderWhenLoadingFromFile() throws IOException {
        // Arrange: Define the expected order based on the 'test-reverse.properties' file content.
        // The file is assumed to contain keys in descending order (key11, key10, ..., key1).
        final List<String> expectedKeys = IntStream.rangeClosed(1, 11)
            .map(i -> 12 - i) // Generates 11, 10, ..., 1
            .mapToObj(i -> "key" + i)
            .collect(Collectors.toList());

        final List<Entry<Object, Object>> expectedEntries = IntStream.rangeClosed(1, 11)
            .map(i -> 12 - i) // Generates 11, 10, ..., 1
            .mapToObj(i -> new SimpleEntry<>("key" + i, "value" + i))
            .collect(Collectors.toList());

        final OrderedProperties properties = new OrderedProperties();
        final String propertiesFilePath = "src/test/resources/org/apache/commons/collections4/properties/test-reverse.properties";

        // Act: Load properties from the file.
        try (FileReader reader = new FileReader(propertiesFilePath)) {
            properties.load(reader);
        }

        // Assert: Verify that all views return elements in the file's order.
        assertEquals(11, properties.size());
        assertIterableEquals(expectedKeys, properties.keySet(), "keySet() should preserve order from file.");
        assertIterableEquals(expectedEntries, properties.entrySet(), "entrySet() should preserve order from file.");
        assertIterableEquals(expectedKeys, Collections.list(properties.keys()), "keys() should preserve order from file.");
        assertIterableEquals(expectedKeys, Collections.list(properties.propertyNames()), "propertyNames() should preserve order from file.");
    }
}