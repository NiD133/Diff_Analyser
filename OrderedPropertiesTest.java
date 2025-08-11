/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * Tests {@link OrderedProperties} to verify that keys maintain their insertion order.
 */
class OrderedPropertiesTest {

    // Test data constants
    private static final int NUMERIC_KEY_START = 1;
    private static final int NUMERIC_KEY_END = 11;
    private static final char CHAR_KEY_START = 'Z';
    private static final char CHAR_KEY_END = 'A';
    
    // Test file paths
    private static final String ASCENDING_PROPERTIES_FILE = 
        "src/test/resources/org/apache/commons/collections4/properties/test.properties";
    private static final String DESCENDING_PROPERTIES_FILE = 
        "src/test/resources/org/apache/commons/collections4/properties/test-reverse.properties";

    /**
     * Verifies that properties maintain ascending order (key1, key2, ..., key11)
     * across all iteration methods: keys(), keySet(), entrySet(), and propertyNames().
     */
    private void verifyAscendingKeyOrder(final OrderedProperties properties) {
        verifyKeysEnumerationOrder(properties, NUMERIC_KEY_START, NUMERIC_KEY_END, 1);
        verifyKeySetIteratorOrder(properties, NUMERIC_KEY_START, NUMERIC_KEY_END, 1);
        verifyEntrySetIteratorOrder(properties, NUMERIC_KEY_START, NUMERIC_KEY_END, 1);
        verifyPropertyNamesOrder(properties, NUMERIC_KEY_START, NUMERIC_KEY_END, 1);
    }

    /**
     * Verifies that properties maintain descending order (key11, key10, ..., key1)
     * across all iteration methods: keys(), keySet(), entrySet(), and propertyNames().
     */
    private void verifyDescendingKeyOrder(final OrderedProperties properties) {
        verifyKeysEnumerationOrder(properties, NUMERIC_KEY_END, NUMERIC_KEY_START, -1);
        verifyKeySetIteratorOrder(properties, NUMERIC_KEY_END, NUMERIC_KEY_START, -1);
        verifyEntrySetIteratorOrder(properties, NUMERIC_KEY_END, NUMERIC_KEY_START, -1);
        verifyPropertyNamesOrder(properties, NUMERIC_KEY_END, NUMERIC_KEY_START, -1);
    }

    /**
     * Verifies the order of keys using the keys() enumeration method.
     */
    private void verifyKeysEnumerationOrder(final OrderedProperties properties, 
                                          final int start, final int end, final int step) {
        final Enumeration<Object> keysEnum = properties.keys();
        for (int i = start; isWithinRange(i, start, end, step); i += step) {
            assertEquals("key" + i, keysEnum.nextElement(), 
                "Keys enumeration should maintain insertion order");
        }
    }

    /**
     * Verifies the order of keys using the keySet() iterator.
     */
    private void verifyKeySetIteratorOrder(final OrderedProperties properties, 
                                         final int start, final int end, final int step) {
        final Iterator<Object> keyIterator = properties.keySet().iterator();
        for (int i = start; isWithinRange(i, start, end, step); i += step) {
            assertEquals("key" + i, keyIterator.next(), 
                "KeySet iterator should maintain insertion order");
        }
    }

    /**
     * Verifies the order of entries using the entrySet() iterator.
     */
    private void verifyEntrySetIteratorOrder(final OrderedProperties properties, 
                                           final int start, final int end, final int step) {
        final Iterator<Entry<Object, Object>> entryIterator = properties.entrySet().iterator();
        for (int i = start; isWithinRange(i, start, end, step); i += step) {
            final Entry<Object, Object> entry = entryIterator.next();
            assertEquals("key" + i, entry.getKey(), 
                "Entry key should maintain insertion order");
            assertEquals("value" + i, entry.getValue(), 
                "Entry value should correspond to key");
        }
    }

    /**
     * Verifies the order of property names using the propertyNames() enumeration.
     */
    private void verifyPropertyNamesOrder(final OrderedProperties properties, 
                                        final int start, final int end, final int step) {
        final Enumeration<?> propertyNames = properties.propertyNames();
        for (int i = start; isWithinRange(i, start, end, step); i += step) {
            assertEquals("key" + i, propertyNames.nextElement(), 
                "Property names should maintain insertion order");
        }
    }

    /**
     * Helper method to determine if iteration should continue based on step direction.
     */
    private boolean isWithinRange(final int current, final int start, final int end, final int step) {
        return step > 0 ? current <= end : current >= end;
    }

    /**
     * Loads properties from the descending test file and verifies the order.
     */
    private OrderedProperties loadPropertiesInDescendingOrder() throws FileNotFoundException, IOException {
        final OrderedProperties properties = new OrderedProperties();
        try (FileReader reader = new FileReader(DESCENDING_PROPERTIES_FILE)) {
            properties.load(reader);
        }
        verifyDescendingKeyOrder(properties);
        return properties;
    }

    /**
     * Creates properties with numeric keys in ascending order (1 to 11).
     */
    private OrderedProperties createAscendingNumericProperties() {
        final OrderedProperties properties = new OrderedProperties();
        for (int i = NUMERIC_KEY_START; i <= NUMERIC_KEY_END; i++) {
            properties.put("key" + i, "value" + i);
        }
        return properties;
    }

    /**
     * Creates properties with numeric keys in descending order (11 to 1).
     */
    private OrderedProperties createDescendingNumericProperties() {
        final OrderedProperties properties = new OrderedProperties();
        for (int i = NUMERIC_KEY_END; i >= NUMERIC_KEY_START; i--) {
            properties.put("key" + i, "value" + i);
        }
        return properties;
    }

    /**
     * Creates properties with character keys in descending order (Z to A).
     */
    private OrderedProperties createDescendingCharProperties() {
        final OrderedProperties properties = new OrderedProperties();
        for (char ch = CHAR_KEY_START; ch >= CHAR_KEY_END; ch--) {
            properties.put(String.valueOf(ch), "Value" + ch);
        }
        return properties;
    }

    @Test
    void testCompute_MaintainsInsertionOrder() {
        // Test ascending order insertion
        OrderedProperties properties = new OrderedProperties();
        for (int i = NUMERIC_KEY_START; i <= NUMERIC_KEY_END; i++) {
            final int keyNumber = i;
            properties.compute("key" + i, (key, value) -> "value" + keyNumber);
        }
        verifyAscendingKeyOrder(properties);

        // Test descending order insertion
        properties.clear();
        for (int i = NUMERIC_KEY_END; i >= NUMERIC_KEY_START; i--) {
            final int keyNumber = i;
            properties.compute("key" + i, (key, value) -> "value" + keyNumber);
        }
        verifyDescendingKeyOrder(properties);
    }

    @Test
    void testComputeIfAbsent_MaintainsInsertionOrder() {
        // Test ascending order insertion
        OrderedProperties properties = new OrderedProperties();
        for (int i = NUMERIC_KEY_START; i <= NUMERIC_KEY_END; i++) {
            final int keyNumber = i;
            properties.computeIfAbsent("key" + i, key -> "value" + keyNumber);
        }
        verifyAscendingKeyOrder(properties);

        // Test descending order insertion
        properties.clear();
        for (int i = NUMERIC_KEY_END; i >= NUMERIC_KEY_START; i--) {
            final int keyNumber = i;
            properties.computeIfAbsent("key" + i, key -> "value" + keyNumber);
        }
        verifyDescendingKeyOrder(properties);
    }

    @Test
    void testEntrySet_MaintainsInsertionOrder() {
        final OrderedProperties properties = createDescendingCharProperties();
        
        // Verify that entrySet iterator returns entries in insertion order (Z to A)
        final Iterator<Map.Entry<Object, Object>> entryIterator = properties.entrySet().iterator();
        for (char expectedChar = CHAR_KEY_START; expectedChar >= CHAR_KEY_END; expectedChar--) {
            final Map.Entry<Object, Object> entry = entryIterator.next();
            assertEquals(String.valueOf(expectedChar), entry.getKey(), 
                "Entry key should match expected character in insertion order");
            assertEquals("Value" + expectedChar, entry.getValue(), 
                "Entry value should correspond to the key");
        }
    }

    @Test
    void testForEach_MaintainsInsertionOrder() {
        final OrderedProperties properties = createDescendingCharProperties();
        
        // Use AtomicInteger to track expected character in forEach callback
        final AtomicInteger expectedCharCode = new AtomicInteger(CHAR_KEY_START);
        properties.forEach((key, value) -> {
            final char expectedChar = (char) expectedCharCode.getAndDecrement();
            assertEquals(String.valueOf(expectedChar), key, 
                "ForEach key should match expected character in insertion order");
            assertEquals("Value" + expectedChar, value, 
                "ForEach value should correspond to the key");
        });
    }

    @Test
    void testKeys_MaintainsInsertionOrder() {
        final OrderedProperties properties = createDescendingCharProperties();
        
        // Verify that keys() enumeration returns keys in insertion order (Z to A)
        final Enumeration<Object> keysEnum = properties.keys();
        for (char expectedChar = CHAR_KEY_START; expectedChar >= CHAR_KEY_END; expectedChar--) {
            assertEquals(String.valueOf(expectedChar), keysEnum.nextElement(), 
                "Keys enumeration should return keys in insertion order");
        }
    }

    @Test
    void testLoadFromFile_AscendingOrder() throws IOException {
        final OrderedProperties properties = new OrderedProperties();
        try (FileReader reader = new FileReader(ASCENDING_PROPERTIES_FILE)) {
            properties.load(reader);
        }
        verifyAscendingKeyOrder(properties);
    }

    @Test
    void testLoadFromFile_DescendingOrder() throws IOException {
        loadPropertiesInDescendingOrder();
    }

    @Test
    void testMerge_MaintainsInsertionOrder() {
        // Test ascending order insertion
        OrderedProperties properties = new OrderedProperties();
        for (int i = NUMERIC_KEY_START; i <= NUMERIC_KEY_END; i++) {
            properties.merge("key" + i, "value" + i, (oldValue, newValue) -> newValue);
        }
        verifyAscendingKeyOrder(properties);

        // Test descending order insertion
        properties.clear();
        for (int i = NUMERIC_KEY_END; i >= NUMERIC_KEY_START; i--) {
            properties.merge("key" + i, "value" + i, (oldValue, newValue) -> newValue);
        }
        verifyDescendingKeyOrder(properties);
    }

    @Test
    void testPut_MaintainsInsertionOrder() {
        // Test ascending order insertion
        final OrderedProperties ascendingProperties = createAscendingNumericProperties();
        verifyAscendingKeyOrder(ascendingProperties);

        // Test descending order insertion
        final OrderedProperties descendingProperties = createDescendingNumericProperties();
        verifyDescendingKeyOrder(descendingProperties);
    }

    @Test
    void testPutAll_MaintainsInsertionOrder() {
        // Create source properties in ascending order
        final OrderedProperties sourceProperties = createAscendingNumericProperties();
        
        // Test putAll maintains order from source
        final OrderedProperties targetProperties = new OrderedProperties();
        targetProperties.putAll(sourceProperties);
        verifyAscendingKeyOrder(targetProperties);

        // Test that subsequent puts maintain their own insertion order
        targetProperties.clear();
        final OrderedProperties descendingProperties = createDescendingNumericProperties();
        verifyDescendingKeyOrder(descendingProperties);
    }

    @Test
    void testPutIfAbsent_MaintainsInsertionOrder() {
        // Test ascending order insertion
        OrderedProperties properties = new OrderedProperties();
        for (int i = NUMERIC_KEY_START; i <= NUMERIC_KEY_END; i++) {
            properties.putIfAbsent("key" + i, "value" + i);
        }
        verifyAscendingKeyOrder(properties);

        // Test descending order insertion
        properties.clear();
        for (int i = NUMERIC_KEY_END; i >= NUMERIC_KEY_START; i--) {
            properties.putIfAbsent("key" + i, "value" + i);
        }
        verifyDescendingKeyOrder(properties);
    }

    @Test
    void testRemoveByKey_RemovesFromAllCollections() throws FileNotFoundException, IOException {
        final OrderedProperties properties = loadPropertiesInDescendingOrder();
        final String keyToRemove = "key1";
        
        // Remove the key
        properties.remove(keyToRemove);
        
        // Verify key is removed from all collection views
        assertFalse(properties.contains(keyToRemove), 
            "Key should not be found in contains() after removal");
        assertFalse(properties.containsKey(keyToRemove), 
            "Key should not be found in containsKey() after removal");
        assertFalse(Collections.list(properties.keys()).contains(keyToRemove), 
            "Key should not be found in keys() enumeration after removal");
        assertFalse(Collections.list(properties.propertyNames()).contains(keyToRemove), 
            "Key should not be found in propertyNames() after removal");
    }

    @Test
    void testRemoveByKeyValue_RemovesFromAllCollections() throws FileNotFoundException, IOException {
        final OrderedProperties properties = loadPropertiesInDescendingOrder();
        final String keyToRemove = "key1";
        final String valueToMatch = "value1";
        
        // Remove the key-value pair
        properties.remove(keyToRemove, valueToMatch);
        
        // Verify key is removed from all collection views
        assertFalse(properties.contains(keyToRemove), 
            "Key should not be found in contains() after removal");
        assertFalse(properties.containsKey(keyToRemove), 
            "Key should not be found in containsKey() after removal");
        assertFalse(Collections.list(properties.keys()).contains(keyToRemove), 
            "Key should not be found in keys() enumeration after removal");
        assertFalse(Collections.list(properties.propertyNames()).contains(keyToRemove), 
            "Key should not be found in propertyNames() after removal");
    }

    @Test
    void testToString_ReflectsInsertionOrder() {
        final OrderedProperties properties = createDescendingCharProperties();
        
        // Verify toString() reflects the insertion order
        final String expectedString = 
            "{Z=ValueZ, Y=ValueY, X=ValueX, W=ValueW, V=ValueV, U=ValueU, T=ValueT, S=ValueS, " +
            "R=ValueR, Q=ValueQ, P=ValueP, O=ValueO, N=ValueN, M=ValueM, L=ValueL, K=ValueK, " +
            "J=ValueJ, I=ValueI, H=ValueH, G=ValueG, F=ValueF, E=ValueE, D=ValueD, C=ValueC, " +
            "B=ValueB, A=ValueA}";
        
        assertEquals(expectedString, properties.toString(), 
            "toString() should reflect insertion order of keys");
    }
}