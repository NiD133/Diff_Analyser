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
 * Test suite for {@link PredicatedMap} implementation.
 * 
 * PredicatedMap validates that keys and values match specified predicates
 * before allowing them to be added to the underlying map.
 *
 * @param <K> the key type.
 * @param <V> the value type.
 */
public class PredicatedMapTest<K, V> extends AbstractIterableMapTest<K, V> {

    /** Predicate that accepts all objects - used for unrestricted maps */
    protected static final Predicate<Object> ACCEPT_ALL_PREDICATE = TruePredicate.<Object>truePredicate();

    /** Predicate that only accepts String objects - used for testing validation */
    protected static final Predicate<Object> STRING_ONLY_PREDICATE = String.class::isInstance;

    /**
     * Creates a PredicatedMap with the specified predicates for keys and values.
     */
    protected IterableMap<K, V> createPredicatedMap(final Map<K, V> baseMap, 
                                                   final Predicate<? super K> keyPredicate,
                                                   final Predicate<? super V> valuePredicate) {
        return PredicatedMap.predicatedMap(baseMap, keyPredicate, valuePredicate);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public IterableMap<K, V> makeObject() {
        // Create an unrestricted map (accepts any keys and values)
        return createPredicatedMap(new HashMap<>(), ACCEPT_ALL_PREDICATE, ACCEPT_ALL_PREDICATE);
    }

    /**
     * Creates a test map that only accepts String keys and values.
     */
    public IterableMap<K, V> createStringOnlyMap() {
        return createPredicatedMap(new HashMap<>(), STRING_ONLY_PREDICATE, STRING_ONLY_PREDICATE);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testEntrySet_ReturnsValidEntrySet() {
        // Given: A string-only predicated map
        Map<K, V> stringOnlyMap = createStringOnlyMap();
        
        // When & Then: Entry set should not be null
        assertNotNull(stringOnlyMap.entrySet(), "Entry set should never be null");
        
        // Given: An unrestricted map with one entry
        Map<K, V> unrestrictedMap = createPredicatedMap(new HashMap<>(), null, null);
        unrestrictedMap.put((K) "testKey", (V) "testValue");
        
        // When & Then: Entry set should contain the added entry
        assertEquals(1, unrestrictedMap.entrySet().size(), 
                    "Entry set should contain exactly one entry after adding one key-value pair");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testPut_ValidatesKeysAndValues() {
        // Given: A map that only accepts String keys and values
        final Map<K, V> stringOnlyMap = createStringOnlyMap();
        
        // When & Then: Adding invalid value should throw exception
        assertThrows(IllegalArgumentException.class, 
                    () -> stringOnlyMap.put((K) "validKey", (V) Integer.valueOf(3)),
                    "Should reject non-String values");

        // When & Then: Adding invalid key should throw exception
        assertThrows(IllegalArgumentException.class, 
                    () -> stringOnlyMap.put((K) Integer.valueOf(3), (V) "validValue"),
                    "Should reject non-String keys");

        // When & Then: Map should not contain the rejected entries
        assertFalse(stringOnlyMap.containsKey(Integer.valueOf(3)), 
                   "Map should not contain rejected key");
        assertFalse(stringOnlyMap.containsValue(Integer.valueOf(3)), 
                   "Map should not contain rejected value");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testPutAll_ValidatesAllEntries() {
        // Given: A map that only accepts String keys and values
        final Map<K, V> stringOnlyMap = createStringOnlyMap();
        
        // Given: A source map with mixed valid and invalid entries
        final Map<K, V> sourceMap = new HashMap<>();
        sourceMap.put((K) "validKey1", (V) "validValue1");
        sourceMap.put((K) "validKey2", (V) "validValue2");
        sourceMap.put((K) "validKey3", (V) "validValue3");
        sourceMap.put((K) "invalidKey", (V) Integer.valueOf(3)); // Invalid value

        // When & Then: putAll should reject the entire operation due to invalid entry
        assertThrows(IllegalArgumentException.class, 
                    () -> stringOnlyMap.putAll(sourceMap),
                    "Should reject putAll operation when any entry is invalid");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testEntrySetValue_ValidatesNewValues() {
        // Given: A map that only accepts String keys and values
        final Map<K, V> stringOnlyMap = createStringOnlyMap();
        stringOnlyMap.put((K) "testKey", (V) "originalValue");
        
        // Given: An entry from the map
        Iterator<Map.Entry<K, V>> iterator = stringOnlyMap.entrySet().iterator();
        Map.Entry<K, V> entry = iterator.next();

        // When & Then: Setting invalid value through entry should throw exception
        assertThrows(IllegalArgumentException.class, 
                    () -> entry.setValue((V) Integer.valueOf(3)),
                    "Should reject invalid values when set through Map.Entry.setValue()");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testEntrySetValue_AcceptsValidValues() {
        // Given: A map that only accepts String keys and values
        final Map<K, V> stringOnlyMap = createStringOnlyMap();
        stringOnlyMap.put((K) "testKey", (V) "originalValue");
        
        // Given: An entry from the map
        Iterator<Map.Entry<K, V>> iterator = stringOnlyMap.entrySet().iterator();
        Map.Entry<K, V> entry = iterator.next();

        // When: Setting valid value through entry
        entry.setValue((V) "newValidValue");
        
        // Then: Operation should succeed (no exception thrown)
        assertEquals("newValidValue", entry.getValue(), 
                    "Entry should contain the new valid value");
    }

//    void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "src/test/resources/data/test/PredicatedMap.emptyCollection.version4.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "src/test/resources/data/test/PredicatedMap.fullCollection.version4.obj");
//    }
}