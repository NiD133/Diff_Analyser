package org.apache.commons.collections4.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link OrderedProperties}.
 */
class OrderedPropertiesTest {

    /**
     * Asserts that the keys and values in the given OrderedProperties are in ascending order.
     */
    private void assertKeysInAscendingOrder(final OrderedProperties orderedProperties) {
        final int start = 1;
        final int end = 11;

        // Check keys using Enumeration
        final Enumeration<Object> keysEnum = orderedProperties.keys();
        for (int i = start; i <= end; i++) {
            assertEquals("key" + i, keysEnum.nextElement());
        }

        // Check keys using keySet iterator
        final Iterator<Object> keySetIterator = orderedProperties.keySet().iterator();
        for (int i = start; i <= end; i++) {
            assertEquals("key" + i, keySetIterator.next());
        }

        // Check entries using entrySet iterator
        final Iterator<Entry<Object, Object>> entrySetIterator = orderedProperties.entrySet().iterator();
        for (int i = start; i <= end; i++) {
            final Entry<Object, Object> entry = entrySetIterator.next();
            assertEquals("key" + i, entry.getKey());
            assertEquals("value" + i, entry.getValue());
        }

        // Check property names
        final Enumeration<?> propertyNames = orderedProperties.propertyNames();
        for (int i = start; i <= end; i++) {
            assertEquals("key" + i, propertyNames.nextElement());
        }
    }

    /**
     * Asserts that the keys and values in the given OrderedProperties are in descending order.
     */
    private OrderedProperties assertKeysInDescendingOrder(final OrderedProperties orderedProperties) {
        final int start = 11;
        final int end = 1;

        // Check keys using Enumeration
        final Enumeration<Object> keysEnum = orderedProperties.keys();
        for (int i = start; i >= end; i--) {
            assertEquals("key" + i, keysEnum.nextElement());
        }

        // Check keys using keySet iterator
        final Iterator<Object> keySetIterator = orderedProperties.keySet().iterator();
        for (int i = start; i >= end; i--) {
            assertEquals("key" + i, keySetIterator.next());
        }

        // Check entries using entrySet iterator
        final Iterator<Entry<Object, Object>> entrySetIterator = orderedProperties.entrySet().iterator();
        for (int i = start; i >= end; i--) {
            final Entry<Object, Object> entry = entrySetIterator.next();
            assertEquals("key" + i, entry.getKey());
            assertEquals("value" + i, entry.getValue());
        }

        // Check property names
        final Enumeration<?> propertyNames = orderedProperties.propertyNames();
        for (int i = start; i >= end; i--) {
            assertEquals("key" + i, propertyNames.nextElement());
        }

        return orderedProperties;
    }

    /**
     * Loads properties from a file and asserts they are in descending order.
     */
    private OrderedProperties loadPropertiesInReverseOrder() throws FileNotFoundException, IOException {
        final OrderedProperties orderedProperties = new OrderedProperties();
        try (FileReader reader = new FileReader("src/test/resources/org/apache/commons/collections4/properties/test-reverse.properties")) {
            orderedProperties.load(reader);
        }
        return assertKeysInDescendingOrder(orderedProperties);
    }

    @Test
    void testCompute() {
        final OrderedProperties orderedProperties = new OrderedProperties();

        // Populate properties in ascending order
        for (int i = 1; i <= 11; i++) {
            orderedProperties.compute("key" + i, (k, v) -> "value" + i);
        }
        assertKeysInAscendingOrder(orderedProperties);

        // Clear and populate properties in descending order
        orderedProperties.clear();
        for (int i = 11; i >= 1; i--) {
            orderedProperties.compute("key" + i, (k, v) -> "value" + i);
        }
        assertKeysInDescendingOrder(orderedProperties);
    }

    @Test
    void testComputeIfAbsent() {
        final OrderedProperties orderedProperties = new OrderedProperties();

        // Populate properties in ascending order
        for (int i = 1; i <= 11; i++) {
            orderedProperties.computeIfAbsent("key" + i, k -> "value" + i);
        }
        assertKeysInAscendingOrder(orderedProperties);

        // Clear and populate properties in descending order
        orderedProperties.clear();
        for (int i = 11; i >= 1; i--) {
            orderedProperties.computeIfAbsent("key" + i, k -> "value" + i);
        }
        assertKeysInDescendingOrder(orderedProperties);
    }

    @Test
    void testEntrySet() {
        final OrderedProperties orderedProperties = new OrderedProperties();

        // Populate properties with characters in descending order
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            orderedProperties.put(String.valueOf(ch), "Value" + ch);
        }

        // Check entries using entrySet iterator
        final Iterator<Map.Entry<Object, Object>> entries = orderedProperties.entrySet().iterator();
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            final Map.Entry<Object, Object> entry = entries.next();
            assertEquals(String.valueOf(ch), entry.getKey());
            assertEquals("Value" + ch, entry.getValue());
        }
    }

    @Test
    void testForEach() {
        final OrderedProperties orderedProperties = new OrderedProperties();

        // Populate properties with characters in descending order
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            orderedProperties.put(String.valueOf(ch), "Value" + ch);
        }

        // Check entries using forEach
        final AtomicInteger currentChar = new AtomicInteger('Z');
        orderedProperties.forEach((k, v) -> {
            final char expectedChar = (char) currentChar.getAndDecrement();
            assertEquals(String.valueOf(expectedChar), k);
            assertEquals("Value" + expectedChar, v);
        });
    }

    @Test
    void testKeys() {
        final OrderedProperties orderedProperties = new OrderedProperties();

        // Populate properties with characters in descending order
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            orderedProperties.put(String.valueOf(ch), "Value" + ch);
        }

        // Check keys using Enumeration
        final Enumeration<Object> keys = orderedProperties.keys();
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            assertEquals(String.valueOf(ch), keys.nextElement());
        }
    }

    @Test
    void testLoadOrderedKeys() throws IOException {
        final OrderedProperties orderedProperties = new OrderedProperties();
        try (FileReader reader = new FileReader("src/test/resources/org/apache/commons/collections4/properties/test.properties")) {
            orderedProperties.load(reader);
        }
        assertKeysInAscendingOrder(orderedProperties);
    }

    @Test
    void testLoadOrderedKeysReverse() throws IOException {
        loadPropertiesInReverseOrder();
    }

    @Test
    void testMerge() {
        final OrderedProperties orderedProperties = new OrderedProperties();

        // Populate properties in ascending order
        for (int i = 1; i <= 11; i++) {
            orderedProperties.merge("key" + i, "value" + i, (k, v) -> v);
        }
        assertKeysInAscendingOrder(orderedProperties);

        // Clear and populate properties in descending order
        orderedProperties.clear();
        for (int i = 11; i >= 1; i--) {
            orderedProperties.merge("key" + i, "value" + i, (k, v) -> v);
        }
        assertKeysInDescendingOrder(orderedProperties);
    }

    @Test
    void testPut() {
        final OrderedProperties orderedProperties = new OrderedProperties();

        // Populate properties in ascending order
        for (int i = 1; i <= 11; i++) {
            orderedProperties.put("key" + i, "value" + i);
        }
        assertKeysInAscendingOrder(orderedProperties);

        // Clear and populate properties in descending order
        orderedProperties.clear();
        for (int i = 11; i >= 1; i--) {
            orderedProperties.put("key" + i, "value" + i);
        }
        assertKeysInDescendingOrder(orderedProperties);
    }

    @Test
    void testPutAll() {
        final OrderedProperties sourceProperties = new OrderedProperties();

        // Populate source properties in ascending order
        for (int i = 1; i <= 11; i++) {
            sourceProperties.put("key" + i, "value" + i);
        }

        final OrderedProperties orderedProperties = new OrderedProperties();
        orderedProperties.putAll(sourceProperties);
        assertKeysInAscendingOrder(orderedProperties);

        // Clear and populate properties in descending order
        orderedProperties.clear();
        for (int i = 11; i >= 1; i--) {
            orderedProperties.put("key" + i, "value" + i);
        }
        assertKeysInDescendingOrder(orderedProperties);
    }

    @Test
    void testPutIfAbsent() {
        final OrderedProperties orderedProperties = new OrderedProperties();

        // Populate properties in ascending order
        for (int i = 1; i <= 11; i++) {
            orderedProperties.putIfAbsent("key" + i, "value" + i);
        }
        assertKeysInAscendingOrder(orderedProperties);

        // Clear and populate properties in descending order
        orderedProperties.clear();
        for (int i = 11; i >= 1; i--) {
            orderedProperties.putIfAbsent("key" + i, "value" + i);
        }
        assertKeysInDescendingOrder(orderedProperties);
    }

    @Test
    void testRemoveKey() throws FileNotFoundException, IOException {
        final OrderedProperties props = loadPropertiesInReverseOrder();
        final String keyToRemove = "key1";
        props.remove(keyToRemove);
        assertFalse(props.contains(keyToRemove));
        assertFalse(props.containsKey(keyToRemove));
        assertFalse(Collections.list(props.keys()).contains(keyToRemove));
        assertFalse(Collections.list(props.propertyNames()).contains(keyToRemove));
    }

    @Test
    void testRemoveKeyValue() throws FileNotFoundException, IOException {
        final OrderedProperties props = loadPropertiesInReverseOrder();
        final String keyToRemove = "key1";
        props.remove(keyToRemove, "value1");
        assertFalse(props.contains(keyToRemove));
        assertFalse(props.containsKey(keyToRemove));
        assertFalse(Collections.list(props.keys()).contains(keyToRemove));
        assertFalse(Collections.list(props.propertyNames()).contains(keyToRemove));
    }

    @Test
    void testToString() {
        final OrderedProperties orderedProperties = new OrderedProperties();

        // Populate properties with characters in descending order
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            orderedProperties.put(String.valueOf(ch), "Value" + ch);
        }

        // Check string representation
        assertEquals(
                "{Z=ValueZ, Y=ValueY, X=ValueX, W=ValueW, V=ValueV, U=ValueU, T=ValueT, S=ValueS, R=ValueR, Q=ValueQ, P=ValueP, O=ValueO, N=ValueN, M=ValueM, L=ValueL, K=ValueK, J=ValueJ, I=ValueI, H=ValueH, G=ValueG, F=ValueF, E=ValueE, D=ValueD, C=ValueC, B=ValueB, A=ValueA}",
                orderedProperties.toString());
    }
}