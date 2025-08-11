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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections4.IterableMap;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.functors.TruePredicate;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractMapTest} for exercising the
 * {@link PredicatedMap} implementation.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 */
public class PredicatedMapTest<K, V> extends AbstractIterableMapTest<K, V> {

    protected static final Predicate<Object> truePredicate = TruePredicate.<Object>truePredicate();
    protected static final Predicate<Object> testPredicate = String.class::isInstance;

    // Test data constants
    private static final String VALID_KEY = "validKey";
    private static final String VALID_VALUE = "validValue";
    private static final Integer INVALID_KEY = 123;
    private static final Integer INVALID_VALUE = 456;

    protected IterableMap<K, V> decorateMap(final Map<K, V> map, final Predicate<? super K> keyPredicate,
        final Predicate<? super V> valuePredicate) {
        return PredicatedMap.predicatedMap(map, keyPredicate, valuePredicate);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public IterableMap<K, V> makeObject() {
        return decorateMap(new HashMap<>(), truePredicate, truePredicate);
    }

    public IterableMap<K, V> makeTestMap() {
        return decorateMap(new HashMap<>(), testPredicate, testPredicate);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testEntrySet_NonNull() {
        // Test that entrySet is not null for a new map
        Map<K, V> map = makeTestMap();
        assertNotNull(map.entrySet(), "EntrySet of new map should not be null");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testEntrySet_AcceptsEntriesWhenPredicatesAreNull() {
        // Test that a map with null predicates accepts entries
        Map<K, V> map = decorateMap(new HashMap<>(), null, null);
        map.put((K) "oneKey", (V) "oneValue");
        assertEquals(1, map.entrySet().size(), "EntrySet size should be 1 after adding an entry");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testPut_InvalidValue_ThrowsException() {
        // Test putting an invalid value (non-String) with valid key
        Map<K, V> map = makeTestMap();
        assertThrows(IllegalArgumentException.class, 
            () -> map.put((K) VALID_KEY, (V) INVALID_VALUE),
            "Put with invalid value should throw IllegalArgumentException");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testPut_InvalidKey_ThrowsException() {
        // Test putting an invalid key (non-String) with valid value
        Map<K, V> map = makeTestMap();
        assertThrows(IllegalArgumentException.class, 
            () -> map.put((K) INVALID_KEY, (V) VALID_VALUE),
            "Put with invalid key should throw IllegalArgumentException");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testPut_InvalidKeyOrValue_NotAddedToMap() {
        // Test that invalid keys/values are not added to the map
        Map<K, V> map = makeTestMap();
        
        // Attempt invalid operations (should throw and not modify map)
        assertThrows(IllegalArgumentException.class, () -> map.put((K) INVALID_KEY, (V) VALID_VALUE));
        assertThrows(IllegalArgumentException.class, () -> map.put((K) VALID_KEY, (V) INVALID_VALUE));
        
        // Verify map remains unchanged
        assertFalse(map.containsKey(INVALID_KEY), "Map should not contain invalid key");
        assertFalse(map.containsValue(INVALID_VALUE), "Map should not contain invalid value");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testPutAll_InvalidValue_ThrowsException() {
        // Test putAll with a map containing invalid value
        Map<K, V> map = makeTestMap();
        Map<K, V> invalidMap = new HashMap<>();
        invalidMap.put((K) "A", (V) "a");
        invalidMap.put((K) "B", (V) "b");
        invalidMap.put((K) "C", (V) INVALID_VALUE); // Invalid value
        
        assertThrows(IllegalArgumentException.class, 
            () -> map.putAll(invalidMap),
            "putAll with invalid value should throw IllegalArgumentException");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testEntrySetValue_InvalidValue_ThrowsException() {
        // Test setting invalid value via entry.setValue()
        Map<K, V> map = makeTestMap();
        map.put((K) "E", (V) "e");
        
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        Map.Entry<K, V> entry = iterator.next();
        
        assertThrows(IllegalArgumentException.class, 
            () -> entry.setValue((V) INVALID_VALUE),
            "entry.setValue() with invalid value should throw IllegalArgumentException");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testEntrySetValue_ValidValue_Succeeds() {
        // Test setting valid value via entry.setValue()
        Map<K, V> map = makeTestMap();
        map.put((K) "F", (V) "f");
        
        Iterator<Map.Entry<K, V>> iterator = map.entrySet().iterator();
        Map.Entry<K, V> entry = iterator.next();
        entry.setValue((V) "x"); // Should succeed
        
        // Verify the value was updated
        assertEquals("x", map.get("F"), "entry.setValue() should update the value");
    }

}