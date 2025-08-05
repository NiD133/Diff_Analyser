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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
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

    /** A predicate that always returns true. */
    protected static final Predicate<Object> ACCEPT_ALL_PREDICATE = TruePredicate.<Object>truePredicate();

    /** A predicate that only accepts String instances. */
    protected static final Predicate<Object> STRING_INSTANCE_PREDICATE = String.class::isInstance;

    /**
     * Factory method to create a new predicated map.
     *
     * @param map the map to decorate
     * @param keyPredicate the predicate to validate keys
     * @param valuePredicate the predicate to validate values
     * @return a new predicated map
     */
    protected IterableMap<K, V> decorateMap(final Map<K, V> map, final Predicate<? super K> keyPredicate,
        final Predicate<? super V> valuePredicate) {
        return PredicatedMap.predicatedMap(map, keyPredicate, valuePredicate);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * {@inheritDoc}
     * @return a map that accepts any key or value.
     */
    @Override
    public IterableMap<K, V> makeObject() {
        return decorateMap(new HashMap<>(), ACCEPT_ALL_PREDICATE, ACCEPT_ALL_PREDICATE);
    }

    /**
     * Creates a map that only accepts String keys and String values.
     *
     * @return a new predicated map with string-only predicates.
     */
    public IterableMap<K, V> makeTestMap() {
        return decorateMap(new HashMap<>(), STRING_INSTANCE_PREDICATE, STRING_INSTANCE_PREDICATE);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void put_shouldSucceed_whenKeyAndValueAreValid() {
        // Arrange
        final Map<K, V> predicatedMap = makeTestMap();
        final K validKey = (K) "validKey";
        final V validValue = (V) "validValue";

        // Act
        predicatedMap.put(validKey, validValue);

        // Assert
        assertEquals(1, predicatedMap.size());
        assertEquals(validValue, predicatedMap.get(validKey));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void put_shouldThrowException_whenKeyIsInvalid() {
        // Arrange
        final Map<K, V> predicatedMap = makeTestMap();
        final K invalidKey = (K) new Object(); // Invalid because it's not a String
        final V validValue = (V) "validValue";

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> predicatedMap.put(invalidKey, validValue),
            "put() should throw IllegalArgumentException for an invalid key.");

        assertTrue(predicatedMap.isEmpty(), "Map should be empty after a failed put operation.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void put_shouldThrowException_whenValueIsInvalid() {
        // Arrange
        final Map<K, V> predicatedMap = makeTestMap();
        final K validKey = (K) "validKey";
        final V invalidValue = (V) new Object(); // Invalid because it's not a String

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> predicatedMap.put(validKey, invalidValue),
            "put() should throw IllegalArgumentException for an invalid value.");

        assertTrue(predicatedMap.isEmpty(), "Map should be empty after a failed put operation.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void putAll_shouldThrowException_whenMapContainsInvalidKey() {
        // Arrange
        final Map<K, V> predicatedMap = makeTestMap();
        final Map<K, V> mapWithInvalidKey = new HashMap<>();
        mapWithInvalidKey.put((K) "validKey1", (V) "validValue1");
        mapWithInvalidKey.put((K) new Object(), (V) "validValue2"); // Invalid key

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> predicatedMap.putAll(mapWithInvalidKey),
            "putAll() should throw IllegalArgumentException if the map to copy contains an invalid key.");

        assertTrue(predicatedMap.isEmpty(), "Map should be empty after a failed putAll operation.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void putAll_shouldThrowException_whenMapContainsInvalidValue() {
        // Arrange
        final Map<K, V> predicatedMap = makeTestMap();
        final Map<K, V> mapWithInvalidValue = new HashMap<>();
        mapWithInvalidValue.put((K) "validKey1", (V) "validValue1");
        mapWithInvalidValue.put((K) "validKey2", (V) new Object()); // Invalid value

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> predicatedMap.putAll(mapWithInvalidValue),
            "putAll() should throw IllegalArgumentException if the map to copy contains an invalid value.");

        assertTrue(predicatedMap.isEmpty(), "Map should be empty after a failed putAll operation.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void entrySetSetValue_shouldThrowException_forInvalidValue() {
        // Arrange
        final Map<K, V> predicatedMap = makeTestMap();
        final K key = (K) "key";
        final V initialValue = (V) "initialValue";
        predicatedMap.put(key, initialValue);

        final Map.Entry<K, V> entry = predicatedMap.entrySet().iterator().next();
        final V invalidValue = (V) new Object(); // Invalid because it's not a String

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> entry.setValue(invalidValue),
            "Entry.setValue() should throw IllegalArgumentException for an invalid value.");

        assertEquals(initialValue, predicatedMap.get(key), "The value should not have been changed.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void entrySetSetValue_shouldUpdateValue_forValidValue() {
        // Arrange
        final Map<K, V> predicatedMap = makeTestMap();
        final K key = (K) "key";
        predicatedMap.put(key, (V) "initialValue");

        final Map.Entry<K, V> entry = predicatedMap.entrySet().iterator().next();
        final V validNewValue = (V) "newValue";

        // Act
        entry.setValue(validNewValue);

        // Assert
        assertEquals(validNewValue, predicatedMap.get(key), "The value should have been updated in the map.");
    }

    @Test
    public void entrySet_shouldNotBeNull_forNewMap() {
        // Arrange
        final Map<K, V> predicatedMap = makeTestMap();

        // Act & Assert
        assertNotNull(predicatedMap.entrySet(), "The entry set of a new map should not be null.");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void predicatedMap_shouldAllowAnyType_whenPredicatesAreNull() {
        // Arrange
        final Map<K, V> mapWithNoPredicates = decorateMap(new HashMap<>(), null, null);
        final K key = (K) new Object();
        final V value = (V) new Object();

        // Act
        mapWithNoPredicates.put(key, value);

        // Assert
        assertEquals(1, mapWithNoPredicates.size());
        assertEquals(value, mapWithNoPredicates.get(key));
    }
}