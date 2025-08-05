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
package org.apache.commons.collections4.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSortedMapTest} for exercising the {@link TransformedSortedMap}
 * implementation.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 */
public class TransformedSortedMapTest<K, V> extends AbstractSortedMapTest<K, V> {

    private static final Transformer<String, Integer> STRING_TO_INTEGER =
        TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

    private SortedMap<K, V> baseMap;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() {
        baseMap = new TreeMap<>();
        baseMap.put((K) "A", (V) "1");
        baseMap.put((K) "B", (V) "2");
        baseMap.put((K) "C", (V) "3");
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public boolean isSubMapViewsSerializable() {
        // TreeMap sub map views have a bug in deserialization.
        return false;
    }

    @Override
    @SuppressWarnings("unchecked")
    public SortedMap<K, V> makeObject() {
        return TransformedSortedMap.transformingSortedMap(new TreeMap<>(),
            (Transformer<? super K, ? extends K>) TransformerUtils.nopTransformer(),
            (Transformer<? super V, ? extends V>) TransformerUtils.nopTransformer());
    }

    /**
     * Tests that {@code transformingSortedMap} decorates a map without transforming
     * its existing elements, but does transform new elements that are added.
     */
    @Test
    @DisplayName("transformingSortedMap() should only transform new values, not existing ones")
    @SuppressWarnings("unchecked")
    void testTransformingSortedMap_decoratesWithoutTransformingExistingValues() {
        // Act: Create a transforming map that decorates the base map
        final SortedMap<K, V> transformedMap = TransformedSortedMap
            .transformingSortedMap(baseMap, null, (Transformer<? super V, ? extends V>) STRING_TO_INTEGER);

        // Assert: Existing values remain untransformed
        assertEquals(3, transformedMap.size());
        assertEquals("1", transformedMap.get("A"));
        assertEquals("2", transformedMap.get("B"));
        assertEquals("3", transformedMap.get("C"));

        // Act: Add a new entry
        transformedMap.put((K) "D", (V) "4");

        // Assert: The new value is transformed
        assertEquals(Integer.valueOf(4), transformedMap.get("D"));
    }

    /**
     * Tests that {@code transformedSortedMap} decorates a map and transforms
     * all its elements (existing and new).
     */
    @Test
    @DisplayName("transformedSortedMap() should transform both existing and new values")
    @SuppressWarnings("unchecked")
    void testTransformedSortedMap_decoratesAndTransformsExistingValues() {
        // Act: Create a transformed map that decorates and transforms the base map
        final SortedMap<K, V> transformedMap = TransformedSortedMap
            .transformedSortedMap(baseMap, null, (Transformer<? super V, ? extends V>) STRING_TO_INTEGER);

        // Assert: Existing values are transformed
        assertEquals(3, transformedMap.size());
        assertEquals(Integer.valueOf(1), transformedMap.get("A"));
        assertEquals(Integer.valueOf(2), transformedMap.get("B"));
        assertEquals(Integer.valueOf(3), transformedMap.get("C"));

        // Act: Add a new entry
        transformedMap.put((K) "D", (V) "4");

        // Assert: The new value is also transformed
        assertEquals(Integer.valueOf(4), transformedMap.get("D"));
    }

    /**
     * Tests that when a key transformer is used, keys are transformed on insertion,
     * and queries must use the transformed key type.
     */
    @Test
    @DisplayName("With key transformer, queries must use the transformed key")
    @SuppressWarnings("unchecked")
    void testKeyTransformation() {
        // Arrange: Create a map that transforms String keys to Integers
        final SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(
            new TreeMap<>(),
            (Transformer<? super K, ? extends K>) STRING_TO_INTEGER,
            null);

        // Act & Assert: Put a value and verify it's stored with a transformed key
        map.put((K) "1", (V) "one");
        assertEquals(1, map.size());
        assertTrue(map.containsKey(1)); // Query with transformed key (Integer)
        assertEquals("one", map.get(1));
        assertTrue(map.containsValue("one"));

        // Assert that querying with the original key type fails, as the underlying
        // TreeMap cannot compare an Integer with a String.
        final String untransformedKey = "1";
        assertThrows(ClassCastException.class, () -> map.containsKey(untransformedKey));
        assertThrows(ClassCastException.class, () -> map.remove(untransformedKey));

        // Assert that removing with the transformed key works
        assertEquals("one", map.remove(1));
        assertTrue(map.isEmpty());
    }

    /**
     * Tests that when a value transformer is used, values are transformed on insertion
     * and when an entry's value is updated.
     */
    @Test
    @DisplayName("With value transformer, values are transformed on put and get")
    @SuppressWarnings("unchecked")
    void testValueTransformation() {
        // Arrange: Create a map that transforms String values to Integers
        final SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(
            new TreeMap<>(),
            null,
            (Transformer<? super V, ? extends V>) STRING_TO_INTEGER);

        // Act & Assert: Put a value and verify it's transformed
        map.put((K) "one", (V) "1");
        assertEquals(1, map.size());
        assertEquals(Integer.valueOf(1), map.get("one"));
        assertTrue(map.containsValue(1));
        assertFalse(map.containsValue("1"));

        // Assert: remove() returns the transformed value
        assertEquals(Integer.valueOf(1), map.remove("one"));
        assertTrue(map.isEmpty());
    }

    /**
     * Tests that the value transformer is applied when updating a value
     * via a Map.Entry from the entry set.
     */
    @Test
    @DisplayName("With value transformer, Entry.setValue() should also transform the value")
    @SuppressWarnings("unchecked")
    void testEntrySetSetValueTransformation() {
        // Arrange: Create a map with a value transformer and add an entry
        final SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(
            new TreeMap<>(),
            null,
            (Transformer<? super V, ? extends V>) STRING_TO_INTEGER);
        map.put((K) "key", (V) "1"); // Initial value is transformed to Integer(1)

        // Act: Get the entry and update its value with a String
        final Map.Entry<K, V> entry = map.entrySet().iterator().next();
        entry.setValue((V) "99");

        // Assert: The value is transformed to an Integer in both the entry and the map
        assertEquals(Integer.valueOf(99), entry.getValue());
        assertEquals(Integer.valueOf(99), map.get("key"));
    }

}