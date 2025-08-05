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
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractSortedMapTest} for exercising the {@link TransformedSortedMap}
 * implementation.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 */
public class TransformedSortedMapTest<K, V> extends AbstractSortedMapTest<K, V> {

    private static final String[] TEST_STRINGS = {"1", "3", "5", "7", "2", "4", "6"};
    private static final Transformer<String, Integer> STRING_TO_INTEGER = 
        TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

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
        return TransformedSortedMap.transformingSortedMap(
            new TreeMap<>(),
            (Transformer<? super K, ? extends K>) TransformerUtils.nopTransformer(),
            (Transformer<? super V, ? extends V>) TransformerUtils.nopTransformer()
        );
    }

    /**
     * Tests decorating a map with a value transformer that transforms new entries.
     * Existing entries should remain unchanged until modified.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testDecorateWithValueTransformer() {
        final SortedMap<K, V> base = new TreeMap<>();
        base.put((K) "A", (V) "1");
        base.put((K) "B", (V) "2");
        base.put((K) "C", (V) "3");

        final SortedMap<K, V> transformedMap = TransformedSortedMap.transformingSortedMap(
            base,
            null,
            (Transformer<? super V, ? extends V>) STRING_TO_INTEGER
        );
        
        assertEquals(3, transformedMap.size());
        assertEquals("1", transformedMap.get("A"));
        assertEquals("2", transformedMap.get("B"));
        assertEquals("3", transformedMap.get("C"));
        
        // Adding a new entry should transform the value
        transformedMap.put((K) "D", (V) "4");
        assertEquals(Integer.valueOf(4), transformedMap.get("D"));
    }

    /**
     * Tests decorating a map and transforming existing entries immediately.
     * New entries should also be transformed.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testDecorateAndTransformExistingValues() {
        final SortedMap<K, V> base = new TreeMap<>();
        base.put((K) "A", (V) "1");
        base.put((K) "B", (V) "2");
        base.put((K) "C", (V) "3");

        final SortedMap<K, V> transformedMap = TransformedSortedMap.transformedSortedMap(
            base,
            null,
            (Transformer<? super V, ? extends V>) STRING_TO_INTEGER
        );
        
        assertEquals(3, transformedMap.size());
        assertEquals(Integer.valueOf(1), transformedMap.get("A"));
        assertEquals(Integer.valueOf(2), transformedMap.get("B"));
        assertEquals(Integer.valueOf(3), transformedMap.get("C"));
        
        // New entry should be transformed
        transformedMap.put((K) "D", (V) "4");
        assertEquals(Integer.valueOf(4), transformedMap.get("D"));
    }

    /**
     * Tests that keys are transformed correctly when using a key transformer.
     * Verifies that map operations require the transformed key type.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testKeyTransformation() {
        final SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(
            new TreeMap<>(),
            (Transformer<? super K, ? extends K>) STRING_TO_INTEGER,
            null
        );
        
        // Verify initial state
        assertEquals(0, map.size());
        
        // Add entries with string keys (should be transformed to integers)
        for (int i = 0; i < TEST_STRINGS.length; i++) {
            final String element = TEST_STRINGS[i];
            map.put((K) element, (V) element);
            
            // Verify size after each insertion
            assertEquals(i + 1, map.size());
            
            // Verify transformed key exists
            final Integer key = Integer.valueOf(element);
            assertTrue(map.containsKey(key), "Map should contain transformed key: " + key);
            
            // Verify original string key cannot be used (wrong type)
            assertThrows(ClassCastException.class, () -> map.containsKey(element),
                "Should throw ClassCastException when using non-transformed key");
            
            // Verify value remains unchanged
            assertTrue(map.containsValue(element));
            assertEquals(element, map.get(key));
        }

        // Test removal with transformed key
        final Integer firstKey = Integer.valueOf(TEST_STRINGS[0]);
        assertEquals(TEST_STRINGS[0], map.remove(firstKey));
        
        // Test removal with non-transformed key should fail
        assertThrows(ClassCastException.class, () -> map.remove(TEST_STRINGS[1]));
    }

    /**
     * Tests that values are transformed correctly when using a value transformer.
     * Verifies that map operations require the original key type and transformed value type.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testValueTransformation() {
        final SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(
            new TreeMap<>(),
            null,
            (Transformer<? super V, ? extends V>) STRING_TO_INTEGER
        );
        
        // Verify initial state
        assertEquals(0, map.size());
        
        // Add entries with string values (should be transformed to integers)
        for (int i = 0; i < TEST_STRINGS.length; i++) {
            final String element = TEST_STRINGS[i];
            map.put((K) element, (V) element);
            
            // Verify size after each insertion
            assertEquals(i + 1, map.size());
            
            // Verify transformed value exists
            final Integer transformedValue = Integer.valueOf(element);
            assertTrue(map.containsValue(transformedValue),
                "Map should contain transformed value: " + transformedValue);
            
            // Verify original string value is not present
            assertFalse(map.containsValue(element),
                "Map should not contain non-transformed value: " + element);
            
            // Verify key remains unchanged
            assertTrue(map.containsKey(element));
            assertEquals(transformedValue, map.get(element));
        }

        // Test removal returns transformed value
        assertEquals(Integer.valueOf(TEST_STRINGS[0]), map.remove(TEST_STRINGS[0]));
    }

    /**
     * Tests that value updates through the entry set are transformed correctly.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testEntrySetValueTransformation() {
        // Create map with value transformer
        final SortedMap<K, V> map = TransformedSortedMap.transformingSortedMap(
            new TreeMap<>(),
            null,
            (Transformer<? super V, ? extends V>) STRING_TO_INTEGER
        );
        
        // Populate the map
        for (String element : TEST_STRINGS) {
            map.put((K) element, (V) element);
        }
        
        final Set<Map.Entry<K, V>> entrySet = map.entrySet();
        final Map.Entry<K, V> firstEntry = entrySet.iterator().next();
        
        // Update value through entry.setValue()
        firstEntry.setValue((V) "88");
        assertEquals(Integer.valueOf(88), firstEntry.getValue());
        assertEquals(Integer.valueOf(88), map.get(firstEntry.getKey()));
    }

    // Uncomment for serialization testing
    /*
    void testCreate() throws Exception {
        resetEmpty();
        writeExternalFormToDisk(
            (java.io.Serializable) map,
            "src/test/resources/data/test/TransformedSortedMap.emptyCollection.version4.obj");
        resetFull();
        writeExternalFormToDisk(
            (java.io.Serializable) map,
            "src/test/resources/data/test/TransformedSortedMap.fullCollection.version4.obj");
    }
    */
}