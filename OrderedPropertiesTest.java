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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Test;

/**
 * Tests {@link OrderedProperties}.
 */
class OrderedPropertiesTest {

    private static final int NUMERIC_FIRST = 1;
    private static final int NUMERIC_LAST = 11;

    private void assertOrder(OrderedProperties properties, List<String> expectedKeys, List<String> expectedValues) {
        // Verify keys() enumeration
        Enumeration<Object> keysEnum = properties.keys();
        for (String key : expectedKeys) {
            assertEquals(key, keysEnum.nextElement());
        }
        assertFalse(keysEnum.hasMoreElements());

        // Verify keySet() iterator
        Iterator<Object> keySetIterator = properties.keySet().iterator();
        for (String key : expectedKeys) {
            assertEquals(key, keySetIterator.next());
        }
        assertFalse(keySetIterator.hasNext());

        // Verify entrySet() iterator
        Iterator<Entry<Object, Object>> entrySetIterator = properties.entrySet().iterator();
        for (int i = 0; i < expectedKeys.size(); i++) {
            Entry<Object, Object> entry = entrySetIterator.next();
            assertEquals(expectedKeys.get(i), entry.getKey());
            assertEquals(expectedValues.get(i), entry.getValue());
        }
        assertFalse(entrySetIterator.hasNext());

        // Verify propertyNames() enumeration
        Enumeration<?> propertyNamesEnum = properties.propertyNames();
        for (String key : expectedKeys) {
            assertEquals(key, propertyNamesEnum.nextElement());
        }
        assertFalse(propertyNamesEnum.hasMoreElements());
    }

    private List<String> generateNumericKeys(int start, int end, int step) {
        List<String> keys = new ArrayList<>();
        for (int i = start; step > 0 ? i <= end : i >= end; i += step) {
            keys.add("key" + i);
        }
        return keys;
    }

    private List<String> generateNumericValues(int start, int end, int step) {
        List<String> values = new ArrayList<>();
        for (int i = start; step > 0 ? i <= end : i >= end; i += step) {
            values.add("value" + i);
        }
        return values;
    }

    private List<String> generateCharacterKeysDescending() {
        List<String> keys = new ArrayList<>();
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            keys.add(String.valueOf(ch));
        }
        return keys;
    }

    private List<String> generateCharacterValuesDescending() {
        List<String> values = new ArrayList<>();
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            values.add("Value" + ch);
        }
        return values;
    }

    private void assertKeyAbsent(OrderedProperties props, String key) {
        assertFalse(props.contains(key));
        assertFalse(props.containsKey(key));
        assertFalse(Collections.list(props.keys()).contains(key));
        assertFalse(Collections.list(props.propertyNames()).contains(key));
    }

    private OrderedProperties loadPropertiesFromFile(String filename) throws IOException {
        OrderedProperties properties = new OrderedProperties();
        try (FileReader reader = new FileReader(filename)) {
            properties.load(reader);
        }
        return properties;
    }

    @Test
    void testCompute() {
        OrderedProperties props = new OrderedProperties();
        
        // Ascending insertion
        for (int i = NUMERIC_FIRST; i <= NUMERIC_LAST; i++) {
            props.compute("key" + i, (k, v) -> "value" + i);
        }
        assertOrder(props, 
            generateNumericKeys(NUMERIC_FIRST, NUMERIC_LAST, 1),
            generateNumericValues(NUMERIC_FIRST, NUMERIC_LAST, 1)
        );

        props.clear();
        
        // Descending insertion
        for (int i = NUMERIC_LAST; i >= NUMERIC_FIRST; i--) {
            props.compute("key" + i, (k, v) -> "value" + i);
        }
        assertOrder(props,
            generateNumericKeys(NUMERIC_LAST, NUMERIC_FIRST, -1),
            generateNumericValues(NUMERIC_LAST, NUMERIC_FIRST, -1)
        );
    }

    @Test
    void testComputeIfAbsent() {
        OrderedProperties props = new OrderedProperties();
        
        // Ascending insertion
        for (int i = NUMERIC_FIRST; i <= NUMERIC_LAST; i++) {
            props.computeIfAbsent("key" + i, k -> "value" + i);
        }
        assertOrder(props,
            generateNumericKeys(NUMERIC_FIRST, NUMERIC_LAST, 1),
            generateNumericValues(NUMERIC_FIRST, NUMERIC_LAST, 1)
        );

        props.clear();
        
        // Descending insertion
        for (int i = NUMERIC_LAST; i >= NUMERIC_FIRST; i--) {
            props.computeIfAbsent("key" + i, k -> "value" + i);
        }
        assertOrder(props,
            generateNumericKeys(NUMERIC_LAST, NUMERIC_FIRST, -1),
            generateNumericValues(NUMERIC_LAST, NUMERIC_FIRST, -1)
        );
    }

    @Test
    void testEntrySet() {
        OrderedProperties props = new OrderedProperties();
        List<String> expectedKeys = generateCharacterKeysDescending();
        List<String> expectedValues = generateCharacterValuesDescending();
        
        // Insert in descending order
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            props.put(String.valueOf(ch), "Value" + ch);
        }

        // Verify entrySet order
        Iterator<Entry<Object, Object>> entries = props.entrySet().iterator();
        for (int i = 0; i < expectedKeys.size(); i++) {
            Entry<Object, Object> entry = entries.next();
            assertEquals(expectedKeys.get(i), entry.getKey());
            assertEquals(expectedValues.get(i), entry.getValue());
        }
    }

    @Test
    void testForEach() {
        OrderedProperties props = new OrderedProperties();
        List<String> expectedKeys = generateCharacterKeysDescending();
        List<String> expectedValues = generateCharacterValuesDescending();
        
        // Insert in descending order
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            props.put(String.valueOf(ch), "Value" + ch);
        }

        // Verify forEach order
        AtomicInteger index = new AtomicInteger(0);
        props.forEach((k, v) -> {
            int i = index.getAndIncrement();
            assertEquals(expectedKeys.get(i), k);
            assertEquals(expectedValues.get(i), v);
        });
    }

    @Test
    void testKeys() {
        OrderedProperties props = new OrderedProperties();
        List<String> expectedKeys = generateCharacterKeysDescending();
        
        // Insert in descending order
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            props.put(String.valueOf(ch), "Value" + ch);
        }

        // Verify keys enumeration
        Enumeration<Object> keys = props.keys();
        for (String expectedKey : expectedKeys) {
            assertEquals(expectedKey, keys.nextElement());
        }
    }

    @Test
    void testLoadOrderedKeys() throws IOException {
        OrderedProperties props = loadPropertiesFromFile(
            "src/test/resources/org/apache/commons/collections4/properties/test.properties"
        );
        assertOrder(props,
            generateNumericKeys(NUMERIC_FIRST, NUMERIC_LAST, 1),
            generateNumericValues(NUMERIC_FIRST, NUMERIC_LAST, 1)
        );
    }

    @Test
    void testLoadOrderedKeysReverse() throws IOException {
        OrderedProperties props = loadPropertiesFromFile(
            "src/test/resources/org/apache/commons/collections4/properties/test-reverse.properties"
        );
        assertOrder(props,
            generateNumericKeys(NUMERIC_LAST, NUMERIC_FIRST, -1),
            generateNumericValues(NUMERIC_LAST, NUMERIC_FIRST, -1)
        );
    }

    @Test
    void testMerge() {
        OrderedProperties props = new OrderedProperties();
        
        // Ascending insertion
        for (int i = NUMERIC_FIRST; i <= NUMERIC_LAST; i++) {
            props.merge("key" + i, "value" + i, (k, v) -> v);
        }
        assertOrder(props,
            generateNumericKeys(NUMERIC_FIRST, NUMERIC_LAST, 1),
            generateNumericValues(NUMERIC_FIRST, NUMERIC_LAST, 1)
        );

        props.clear();
        
        // Descending insertion
        for (int i = NUMERIC_LAST; i >= NUMERIC_FIRST; i--) {
            props.merge("key" + i, "value" + i, (k, v) -> v);
        }
        assertOrder(props,
            generateNumericKeys(NUMERIC_LAST, NUMERIC_FIRST, -1),
            generateNumericValues(NUMERIC_LAST, NUMERIC_FIRST, -1)
        );
    }

    @Test
    void testPut() {
        OrderedProperties props = new OrderedProperties();
        
        // Ascending insertion
        for (int i = NUMERIC_FIRST; i <= NUMERIC_LAST; i++) {
            props.put("key" + i, "value" + i);
        }
        assertOrder(props,
            generateNumericKeys(NUMERIC_FIRST, NUMERIC_LAST, 1),
            generateNumericValues(NUMERIC_FIRST, NUMERIC_LAST, 1)
        );

        props.clear();
        
        // Descending insertion
        for (int i = NUMERIC_LAST; i >= NUMERIC_FIRST; i--) {
            props.put("key" + i, "value" + i);
        }
        assertOrder(props,
            generateNumericKeys(NUMERIC_LAST, NUMERIC_FIRST, -1),
            generateNumericValues(NUMERIC_LAST, NUMERIC_FIRST, -1)
        );
    }

    @Test
    void testPutAll() {
        // Create source properties in ascending order
        OrderedProperties source = new OrderedProperties();
        for (int i = NUMERIC_FIRST; i <= NUMERIC_LAST; i++) {
            source.put("key" + i, "value" + i);
        }

        // Verify putAll maintains order
        OrderedProperties target = new OrderedProperties();
        target.putAll(source);
        assertOrder(target,
            generateNumericKeys(NUMERIC_FIRST, NUMERIC_LAST, 1),
            generateNumericValues(NUMERIC_FIRST, NUMERIC_LAST, 1)
        );
    }

    @Test
    void testPutIfAbsent() {
        OrderedProperties props = new OrderedProperties();
        
        // Ascending insertion
        for (int i = NUMERIC_FIRST; i <= NUMERIC_LAST; i++) {
            props.putIfAbsent("key" + i, "value" + i);
        }
        assertOrder(props,
            generateNumericKeys(NUMERIC_FIRST, NUMERIC_LAST, 1),
            generateNumericValues(NUMERIC_FIRST, NUMERIC_LAST, 1)
        );

        props.clear();
        
        // Descending insertion
        for (int i = NUMERIC_LAST; i >= NUMERIC_FIRST; i--) {
            props.putIfAbsent("key" + i, "value" + i);
        }
        assertOrder(props,
            generateNumericKeys(NUMERIC_LAST, NUMERIC_FIRST, -1),
            generateNumericValues(NUMERIC_LAST, NUMERIC_FIRST, -1)
        );
    }

    @Test
    void testRemoveKey() throws IOException {
        OrderedProperties props = loadPropertiesFromFile(
            "src/test/resources/org/apache/commons/collections4/properties/test-reverse.properties"
        );
        String keyToRemove = "key1";
        props.remove(keyToRemove);
        assertKeyAbsent(props, keyToRemove);
    }

    @Test
    void testRemoveKeyValue() throws IOException {
        OrderedProperties props = loadPropertiesFromFile(
            "src/test/resources/org/apache/commons/collections4/properties/test-reverse.properties"
        );
        String keyToRemove = "key1";
        props.remove(keyToRemove, "value1");
        assertKeyAbsent(props, keyToRemove);
    }

    @Test
    void testToString() {
        OrderedProperties props = new OrderedProperties();
        StringBuilder expected = new StringBuilder("{");
        
        // Insert in descending order and build expected string
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            props.put(String.valueOf(ch), "Value" + ch);
            expected.append(ch).append("=Value").append(ch);
            if (ch > 'A') {
                expected.append(", ");
            }
        }
        expected.append("}");
        
        assertEquals(expected.toString(), props.toString());
    }
}