package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OrderedProperties} to ensure methods that add entries
 * preserve the insertion order. This test focuses on the {@code computeIfAbsent} method.
 */
class OrderedPropertiesTest {

    private static final int START_INDEX = 1;
    private static final int END_INDEX = 11;

    private OrderedProperties properties;

    @BeforeEach
    void setUp() {
        properties = new OrderedProperties();
    }

    @Test
    @DisplayName("computeIfAbsent should preserve ascending insertion order")
    void computeIfAbsent_shouldPreserveAscendingInsertionOrder() {
        // Arrange: Prepare a list of keys to be inserted in ascending order.
        final List<String> expectedKeys = new ArrayList<>();

        // Act: Insert properties in ascending order using computeIfAbsent.
        for (int i = START_INDEX; i <= END_INDEX; i++) {
            final String key = "key" + i;
            // The loop variable 'i' is effectively final and can be used in the lambda.
            properties.computeIfAbsent(key, k -> "value" + i);
            expectedKeys.add(key);
        }

        // Assert: Verify that all views of the properties reflect the insertion order.
        assertPropertyOrder(properties, expectedKeys);
    }

    @Test
    @DisplayName("computeIfAbsent should preserve descending insertion order")
    void computeIfAbsent_shouldPreserveDescendingInsertionOrder() {
        // Arrange: Prepare a list of keys to be inserted in descending order.
        final List<String> expectedKeys = new ArrayList<>();

        // Act: Insert properties in descending order using computeIfAbsent.
        for (int i = END_INDEX; i >= START_INDEX; i--) {
            final String key = "key" + i;
            properties.computeIfAbsent(key, k -> "value" + i);
            expectedKeys.add(key);
        }

        // Assert: Verify that all views of the properties reflect the insertion order.
        assertPropertyOrder(properties, expectedKeys);
    }

    /**
     * Asserts that the keys and values in the OrderedProperties instance match the
     * expected order across all relevant views (keys, keySet, entrySet, propertyNames).
     *
     * @param props        The OrderedProperties instance to check.
     * @param expectedKeys The list of keys in the expected order.
     */
    private void assertPropertyOrder(final OrderedProperties props, final List<String> expectedKeys) {
        // Get iterators/enumerations for all relevant views.
        final Enumeration<Object> keysEnum = props.keys();
        final Iterator<Object> keySetIter = props.keySet().iterator();
        final Iterator<Map.Entry<Object, Object>> entrySetIter = props.entrySet().iterator();
        final Enumeration<?> propertyNamesEnum = props.propertyNames();

        // Iterate through the expected keys and verify each view simultaneously.
        for (final String expectedKey : expectedKeys) {
            final String expectedValue = "value" + expectedKey.substring(3); // e.g., "key1" -> "value1"

            // 1. Test keys()
            assertEquals(expectedKey, keysEnum.nextElement(), "keys() enumeration has wrong element or order");

            // 2. Test keySet()
            assertEquals(expectedKey, keySetIter.next(), "keySet() iterator has wrong element or order");

            // 3. Test entrySet()
            final Map.Entry<Object, Object> entry = entrySetIter.next();
            assertEquals(expectedKey, entry.getKey(), "entrySet() has wrong key");
            assertEquals(expectedValue, entry.getValue(), "entrySet() has wrong value");

            // 4. Test propertyNames()
            assertEquals(expectedKey, propertyNamesEnum.nextElement(), "propertyNames() enumeration has wrong element or order");
        }

        // Verify that none of the views have extra elements.
        assertFalse(keysEnum.hasMoreElements(), "keys() enumeration has extra elements");
        assertFalse(keySetIter.hasNext(), "keySet() iterator has extra elements");
        assertFalse(entrySetIter.hasNext(), "entrySet() iterator has extra elements");
        assertFalse(propertyNamesEnum.hasMoreElements(), "propertyNames() enumeration has extra elements");
    }
}