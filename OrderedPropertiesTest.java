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
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests {@link OrderedProperties}.
 */
class OrderedPropertiesTest {

    /**
     * Asserts that all views of the properties object (keySet, keys, entrySet, etc.)
     * iterate in the specified order of keys.
     *
     * @param properties The OrderedProperties instance to check.
     * @param expectedKeys The expected order of keys.
     */
    private void assertPropertyOrder(final OrderedProperties properties, final List<String> expectedKeys) {
        // 1. Check keySet()
        assertIterableEquals(expectedKeys, properties.keySet());

        // 2. Check keys() enumeration
        assertIterableEquals(expectedKeys, Collections.list(properties.keys()));

        // 3. Check propertyNames() enumeration
        assertIterableEquals(expectedKeys, Collections.list(properties.propertyNames()));

        // 4. Check entrySet() for both keys and values
        final List<String> actualKeysFromEntrySet = new ArrayList<>();
        final List<String> actualValuesFromEntrySet = new ArrayList<>();
        for (final Map.Entry<Object, Object> entry : properties.entrySet()) {
            actualKeysFromEntrySet.add((String) entry.getKey());
            actualValuesFromEntrySet.add((String) entry.getValue());
        }
        assertIterableEquals(expectedKeys, actualKeysFromEntrySet, "Entry set keys should be in order.");

        final List<String> expectedValues = expectedKeys.stream().map(key -> "value" + key.substring(3)).collect(Collectors.toList());
        assertIterableEquals(expectedValues, actualValuesFromEntrySet, "Entry set values should be in order.");
    }

    /**
     * Provides a stream of different methods that can be used to add entries to an OrderedProperties instance.
     * Each method is wrapped in a BiConsumer for consistent testing.
     *
     * @return A stream of Arguments, each containing a display name and a BiConsumer for a modification method.
     */
    static Stream<Arguments> modificationMethodProvider() {
        return Stream.of(
            Arguments.of(
                "put(k, v)",
                (BiConsumer<OrderedProperties, Entry<Object, Object>>) (p, e) -> p.put(e.getKey(), e.getValue())
            ),
            Arguments.of(
                "putIfAbsent(k, v)",
                (BiConsumer<OrderedProperties, Entry<Object, Object>>) (p, e) -> p.putIfAbsent(e.getKey(), e.getValue())
            ),
            Arguments.of(
                "compute(k, f)",
                (BiConsumer<OrderedProperties, Entry<Object, Object>>) (p, e) -> p.compute(e.getKey(), (k, v) -> e.getValue())
            ),
            Arguments.of(
                "computeIfAbsent(k, f)",
                (BiConsumer<OrderedProperties, Entry<Object, Object>>) (p, e) -> p.computeIfAbsent(e.getKey(), k -> e.getValue())
            ),
            Arguments.of(
                "merge(k, v, f)",
                (BiConsumer<OrderedProperties, Entry<Object, Object>>) (p, e) -> p.merge(e.getKey(), e.getValue(), (oldV, newV) -> newV)
            )
        );
    }

    @DisplayName("Modification methods should preserve insertion order")
    @ParameterizedTest(name = "{index}: using {0}")
    @MethodSource("modificationMethodProvider")
    void testModificationMethodsPreserveInsertionOrder(final String methodName, final BiConsumer<OrderedProperties, Entry<Object, Object>> modifier) {
        final OrderedProperties properties = new OrderedProperties();

        // Test ascending order insertion
        final List<String> ascendingKeys = IntStream.rangeClosed(1, 11)
            .mapToObj(i -> "key" + i)
            .collect(Collectors.toList());

        for (final String key : ascendingKeys) {
            final String value = "value" + key.substring(3);
            modifier.accept(properties, Map.entry(key, value));
        }
        assertPropertyOrder(properties, ascendingKeys);

        // Test descending order insertion
        properties.clear();
        final List<String> descendingKeys = IntStream.rangeClosed(1, 11)
            .map(i -> 12 - i)
            .mapToObj(i -> "key" + i)
            .collect(Collectors.toList());

        for (final String key : descendingKeys) {
            final String value = "value" + key.substring(3);
            modifier.accept(properties, Map.entry(key, value));
        }
        assertPropertyOrder(properties, descendingKeys);
    }

    @Test
    @DisplayName("putAll() should preserve the source map's iteration order")
    void testPutAllPreservesOrder() {
        // Arrange: Create a source map with a defined order
        final Map<String, String> sourceMap = new LinkedHashMap<>();
        final List<String> expectedKeys = new ArrayList<>();
        for (int i = 11; i >= 1; i--) {
            final String key = "key" + i;
            expectedKeys.add(key);
            sourceMap.put(key, "value" + i);
        }

        // Act
        final OrderedProperties properties = new OrderedProperties();
        properties.putAll(sourceMap);

        // Assert
        assertPropertyOrder(properties, expectedKeys);
    }

    @Test
    @DisplayName("load() should add keys in the order they appear in the properties file")
    void testLoadPreservesFileOrder() throws IOException {
        // Arrange
        final OrderedProperties ascendingProperties = new OrderedProperties();
        final List<String> ascendingKeys = IntStream.rangeClosed(1, 11).mapToObj(i -> "key" + i).collect(Collectors.toList());

        final OrderedProperties descendingProperties = new OrderedProperties();
        final List<String> descendingKeys = IntStream.rangeClosed(1, 11).map(i -> 12 - i).mapToObj(i -> "key" + i).collect(Collectors.toList());

        // Act
        try (FileReader reader = new FileReader("src/test/resources/org/apache/commons/collections4/properties/test.properties")) {
            ascendingProperties.load(reader);
        }
        try (FileReader reader = new FileReader("src/test/resources/org/apache/commons/collections4/properties/test-reverse.properties")) {
            descendingProperties.load(reader);
        }

        // Assert
        assertPropertyOrder(ascendingProperties, ascendingKeys);
        assertPropertyOrder(descendingProperties, descendingKeys);
    }

    @Test
    @DisplayName("remove(key) should remove the key-value pair")
    void testRemoveKey() {
        // Arrange
        final OrderedProperties props = new OrderedProperties();
        props.put("key1", "value1");
        props.put("key2", "value2");
        props.put("key3", "value3");
        final String keyToRemove = "key2";

        // Act
        props.remove(keyToRemove);

        // Assert
        assertFalse(props.containsKey(keyToRemove), "containsKey should be false after removal");
        assertFalse(props.containsValue("value2"), "containsValue should be false after removal");
        assertFalse(Collections.list(props.keys()).contains(keyToRemove), "keys() enumeration should not contain the key");
        assertFalse(Collections.list(props.propertyNames()).contains(keyToRemove), "propertyNames() enumeration should not contain the key");
        assertIterableEquals(List.of("key1", "key3"), props.keySet(), "Key set order should be maintained after removal");
    }

    @Test
    @DisplayName("remove(key, value) should remove the entry only if value matches")
    void testRemoveKeyValue() {
        // Arrange
        final OrderedProperties props = new OrderedProperties();
        props.put("key1", "value1");
        props.put("key2", "value2");
        final String keyToRemove = "key2";

        // Act: remove with correct value
        props.remove(keyToRemove, "value2");
        // Act: attempt to remove with incorrect value
        props.remove("key1", "wrong-value");


        // Assert
        assertFalse(props.containsKey(keyToRemove), "Key should be removed when value matches");
        assertIterableEquals(List.of("key1"), props.keySet(), "Key should not be removed when value does not match");
    }

    @Test
    @DisplayName("Iteration via forEach() should be in insertion order")
    void testForEach() {
        // Arrange
        final OrderedProperties orderedProperties = new OrderedProperties();
        final List<String> expectedKeys = new ArrayList<>();
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            final String key = String.valueOf(ch);
            expectedKeys.add(key);
            orderedProperties.put(key, "Value" + ch);
        }

        // Act
        final List<Object> actualKeys = new ArrayList<>();
        orderedProperties.forEach((k, v) -> actualKeys.add(k));

        // Assert
        assertIterableEquals(expectedKeys, actualKeys);
    }

    @Test
    @DisplayName("Iteration via entrySet() should be in insertion order")
    void testEntrySet() {
        // Arrange
        final OrderedProperties orderedProperties = new OrderedProperties();
        final List<String> expectedKeys = new ArrayList<>();
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            final String key = String.valueOf(ch);
            expectedKeys.add(key);
            orderedProperties.put(key, "Value" + ch);
        }

        // Act
        final List<Object> actualKeys = new ArrayList<>();
        final Iterator<Map.Entry<Object, Object>> entries = orderedProperties.entrySet().iterator();
        while (entries.hasNext()) {
            actualKeys.add(entries.next().getKey());
        }

        // Assert
        assertIterableEquals(expectedKeys, actualKeys);
    }

    @Test
    @DisplayName("Iteration via keys() enumeration should be in insertion order")
    void testKeys() {
        // Arrange
        final OrderedProperties orderedProperties = new OrderedProperties();
        final List<String> expectedKeys = new ArrayList<>();
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            final String key = String.valueOf(ch);
            expectedKeys.add(key);
            orderedProperties.put(key, "Value" + ch);
        }

        // Act
        final Enumeration<Object> keys = orderedProperties.keys();
        final List<Object> actualKeys = Collections.list(keys);

        // Assert
        assertIterableEquals(expectedKeys, actualKeys);
    }

    @Test
    @DisplayName("toString() should format properties in insertion order")
    void testToString() {
        // Arrange
        final OrderedProperties orderedProperties = new OrderedProperties();
        for (char ch = 'Z'; ch >= 'A'; ch--) {
            orderedProperties.put(String.valueOf(ch), "Value" + ch);
        }

        // Assert
        final String expected = "{Z=ValueZ, Y=ValueY, X=ValueX, W=ValueW, V=ValueV, U=ValueU, T=ValueT, S=ValueS, R=ValueR, Q=ValueQ, P=ValueP, O=ValueO, N=ValueN, M=ValueM, L=ValueL, K=ValueK, J=ValueJ, I=ValueI, H=ValueH, G=ValueG, F=ValueF, E=ValueE, D=ValueD, C=ValueC, B=ValueB, A=ValueA}";
        assertEquals(expected, orderedProperties.toString());
    }
}