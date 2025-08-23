package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the insertion order of keys in {@link OrderedProperties}.
 * Verifies that methods like {@code put} and {@code putAll} maintain the order of elements as they are added.
 */
public class OrderedPropertiesInsertionOrderTest {

    private static final int PROPERTY_COUNT = 11;

    /**
     * Asserts that the properties are in the expected order and have the correct keys and values.
     * It iterates through the entry set, which is sufficient to verify the ordering behavior.
     *
     * @param properties The OrderedProperties instance to check.
     * @param expectedKeys The array of keys in the expected order.
     */
    private void assertKeyAndValueOrder(final OrderedProperties properties, final String[] expectedKeys) {
        final Iterator<Map.Entry<Object, Object>> entryIterator = properties.entrySet().iterator();

        for (final String expectedKey : expectedKeys) {
            assertTrue(entryIterator.hasNext(), "Properties has fewer entries than expected.");
            final Map.Entry<Object, Object> entry = entryIterator.next();

            assertEquals(expectedKey, entry.getKey(), "Key mismatch in entry set.");
            // Assumes values are in the format "value" + number, derived from the key "key" + number.
            final String expectedValue = "value" + expectedKey.substring(3);
            assertEquals(expectedValue, entry.getValue(), "Value mismatch in entry set.");
        }

        assertFalse(entryIterator.hasNext(), "Properties has more entries than expected.");
    }

    @Test
    @DisplayName("putAll(Map) should preserve the iteration order of the source Map")
    void putAllShouldPreserveTheOrderOfSourceProperties() {
        // Arrange: Create a source properties object with keys in ascending order.
        final OrderedProperties sourceProperties = new OrderedProperties();
        final String[] expectedKeys = new String[PROPERTY_COUNT];

        for (int i = 1; i <= PROPERTY_COUNT; i++) {
            final String key = "key" + i;
            sourceProperties.put(key, "value" + i);
            expectedKeys[i - 1] = key;
        }

        final OrderedProperties targetProperties = new OrderedProperties();

        // Act: Use putAll to copy the properties.
        targetProperties.putAll(sourceProperties);

        // Assert: The order in the target should match the source.
        assertKeyAndValueOrder(targetProperties, expectedKeys);
    }

    @Test
    @DisplayName("put(key, value) should add keys in their insertion order")
    void putShouldPreserveInsertionOrder() {
        // Arrange
        final OrderedProperties properties = new OrderedProperties();

        // Act: Insert properties with keys in descending order.
        final String[] expectedKeysInInsertionOrder = IntStream.rangeClosed(1, PROPERTY_COUNT)
            .mapToObj(i -> "key" + (PROPERTY_COUNT - i + 1)) // Generates key11, key10, ..., key1
            .peek(key -> properties.put(key, "value" + key.substring(3)))
            .toArray(String[]::new);

        // Assert: The properties should be stored in the order they were inserted (descending),
        // not in natural key order (ascending).
        assertKeyAndValueOrder(properties, expectedKeysInInsertionOrder);
    }
}