package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for the compute() method in {@link OrderedProperties}.
 */
@DisplayName("OrderedProperties.compute()")
class OrderedPropertiesComputeTest {

    private static final String KEY_PREFIX = "key";
    private static final String VALUE_PREFIX = "value";
    private static final int START_INDEX = 1;
    private static final int END_INDEX = 11;

    private OrderedProperties orderedProperties;

    @BeforeEach
    void setUp() {
        orderedProperties = new OrderedProperties();
    }

    /**
     * Asserts that the properties are in the expected order by checking keys(),
     * keySet(), entrySet(), and propertyNames().
     *
     * @param properties The OrderedProperties instance to check.
     * @param expectedKeys The list of keys in their expected order.
     */
    private void assertPropertyOrder(final OrderedProperties properties, final List<String> expectedKeys) {
        final List<String> expectedValues = expectedKeys.stream()
                .map(key -> key.replace(KEY_PREFIX, VALUE_PREFIX))
                .collect(Collectors.toList());

        // 1. Verify order via keys()
        final Enumeration<Object> keysEnum = properties.keys();
        for (final String expectedKey : expectedKeys) {
            assertEquals(expectedKey, keysEnum.nextElement(), "keys() enumeration is out of order.");
        }
        assertFalse(keysEnum.hasMoreElements(), "keys() has more elements than expected.");

        // 2. Verify order via keySet()
        final Iterator<Object> keySetIter = properties.keySet().iterator();
        for (final String expectedKey : expectedKeys) {
            assertEquals(expectedKey, keySetIter.next(), "keySet() iterator is out of order.");
        }
        assertFalse(keySetIter.hasNext(), "keySet() has more elements than expected.");

        // 3. Verify order and values via entrySet()
        final Iterator<Entry<Object, Object>> entrySetIter = properties.entrySet().iterator();
        for (int i = 0; i < expectedKeys.size(); i++) {
            final Entry<Object, Object> entry = entrySetIter.next();
            assertEquals(expectedKeys.get(i), entry.getKey(), "entrySet() key is out of order.");
            assertEquals(expectedValues.get(i), entry.getValue(), "entrySet() value is incorrect.");
        }
        assertFalse(entrySetIter.hasNext(), "entrySet() has more elements than expected.");

        // 4. Verify order via propertyNames()
        final Enumeration<?> propertyNamesEnum = properties.propertyNames();
        for (final String expectedKey : expectedKeys) {
            assertEquals(expectedKey, propertyNamesEnum.nextElement(), "propertyNames() enumeration is out of order.");
        }
        assertFalse(propertyNamesEnum.hasMoreElements(), "propertyNames() has more elements than expected.");
    }

    @Test
    @DisplayName("should maintain insertion order when adding new keys")
    void computeShouldMaintainAscendingInsertionOrder() {
        // Arrange
        final List<String> expectedKeys = new ArrayList<>();
        for (int i = START_INDEX; i <= END_INDEX; i++) {
            expectedKeys.add(KEY_PREFIX + i);
        }

        // Act
        for (int i = START_INDEX; i <= END_INDEX; i++) {
            final int index = i;
            orderedProperties.compute(KEY_PREFIX + i, (k, v) -> VALUE_PREFIX + index);
        }

        // Assert
        assertPropertyOrder(orderedProperties, expectedKeys);
    }

    @Test
    @DisplayName("should maintain new insertion order after being cleared and repopulated")
    void computeShouldMaintainNewOrderAfterClear() {
        // Arrange: Pre-populate and clear to ensure the internal state is reset.
        for (int i = START_INDEX; i <= END_INDEX; i++) {
            orderedProperties.put(KEY_PREFIX + i, VALUE_PREFIX + i);
        }
        orderedProperties.clear();

        final List<String> expectedKeysInDescendingOrder = new ArrayList<>();
        for (int i = END_INDEX; i >= START_INDEX; i--) {
            expectedKeysInDescendingOrder.add(KEY_PREFIX + i);
        }

        // Act: Repopulate in a different (descending) order.
        for (int i = END_INDEX; i >= START_INDEX; i--) {
            final int index = i;
            orderedProperties.compute(KEY_PREFIX + i, (k, v) -> VALUE_PREFIX + index);
        }

        // Assert
        assertPropertyOrder(orderedProperties, expectedKeysInDescendingOrder);
    }
}