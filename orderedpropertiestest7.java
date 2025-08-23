package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OrderedProperties} focusing on loading from a file
 * and preserving key order.
 */
public class OrderedPropertiesTest {

    private static final String REVERSE_ORDER_PROPERTIES =
        "src/test/resources/org/apache/commons/collections4/properties/test-reverse.properties";

    @Test
    void load_shouldPreserveKeyOrderWhenLoadingFromFile() throws IOException {
        // Arrange
        // The test properties file contains keys in reverse numerical order (key11, key10, ..., key1).
        // We expect OrderedProperties to maintain this insertion order after loading.
        final int keyCount = 11;
        final List<Object> expectedKeys = new ArrayList<>();
        final List<Map.Entry<Object, Object>> expectedEntries = new ArrayList<>();
        for (int i = keyCount; i >= 1; i--) {
            final String key = "key" + i;
            final String value = "value" + i;
            expectedKeys.add(key);
            expectedEntries.add(new AbstractMap.SimpleEntry<>(key, value));
        }

        final OrderedProperties orderedProperties = new OrderedProperties();

        // Act
        try (FileReader reader = new FileReader(REVERSE_ORDER_PROPERTIES)) {
            orderedProperties.load(reader);
        }

        // Assert
        // Verify that all views of the properties reflect the insertion order from the file.
        assertIterableEquals(expectedKeys, orderedProperties.keySet(),
            "keySet() should preserve insertion order.");
        assertIterableEquals(expectedEntries, orderedProperties.entrySet(),
            "entrySet() should preserve insertion order of keys and values.");
        assertIterableEquals(expectedKeys, Collections.list(orderedProperties.keys()),
            "keys() enumeration should preserve insertion order.");
        assertIterableEquals(expectedKeys, Collections.list(orderedProperties.propertyNames()),
            "propertyNames() enumeration should preserve insertion order.");
    }
}