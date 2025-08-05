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

import org.apache.commons.collections4.Transformer;
import org.junit.Before;
import org.junit.Test;

import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;
import java.util.TreeMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Unit tests for {@link TransformedSortedMap}.
 */
public class TransformedSortedMapTest {

    private SortedMap<String, Integer> originalMap;
    private final Transformer<String, String> keyTransformer = key -> "transformedKey_" + key;
    private final Transformer<Integer, Integer> valueTransformer = value -> value * 10;

    @Before
    public void setUp() {
        originalMap = new TreeMap<>();
        originalMap.put("one", 1);
        originalMap.put("two", 2);
        originalMap.put("three", 3);
    }

    // --- Test Factory Methods ---

    @Test
    public void transformingSortedMap_withNullMap_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            TransformedSortedMap.transformingSortedMap(null, keyTransformer, valueTransformer)
        );
    }

    @Test
    public void transformedSortedMap_withNullMap_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            TransformedSortedMap.transformedSortedMap(null, keyTransformer, valueTransformer)
        );
    }

    @Test
    public void transformingSortedMap_doesNotTransformExistingElements() {
        // The 'transforming' factory method decorates the map but does not change existing entries.
        SortedMap<String, Integer> transformedMap =
            TransformedSortedMap.transformingSortedMap(originalMap, keyTransformer, valueTransformer);

        assertEquals("Value for 'one' should be the original value", Integer.valueOf(1), transformedMap.get("one"));
    }

    @Test
    public void transformedSortedMap_transformsExistingElements() {
        // The 'transformed' factory method transforms all existing entries upon creation.
        SortedMap<String, Integer> transformedMap =
            TransformedSortedMap.transformedSortedMap(originalMap, null, valueTransformer);

        assertEquals("Value for 'one' should be transformed", Integer.valueOf(10), transformedMap.get("one"));
        assertEquals("Value for 'two' should be transformed", Integer.valueOf(20), transformedMap.get("two"));
        assertEquals("Value for 'three' should be transformed", Integer.valueOf(30), transformedMap.get("three"));
    }

    @Test
    public void transformedSortedMap_whenTransformerThrowsException_propagatesException() {
        Transformer<Object, Object> throwingTransformer = input -> {
            throw new RuntimeException("Transform failed");
        };
        
        // The factory method attempts to transform existing elements, which should fail.
        assertThrows(RuntimeException.class, () ->
            TransformedSortedMap.transformedSortedMap(originalMap, null, throwingTransformer)
        );
    }

    // --- Test SortedMap Interface Methods ---

    @Test
    public void firstKey_returnsFirstKeyFromUnderlyingMap() {
        SortedMap<String, Integer> transformedMap = TransformedSortedMap.transformingSortedMap(originalMap, null, null);
        assertEquals("one", transformedMap.firstKey());
    }

    @Test
    public void firstKey_onEmptyMap_throwsNoSuchElementException() {
        SortedMap<String, Integer> emptyMap = TransformedSortedMap.transformingSortedMap(new TreeMap<>(), null, null);
        assertThrows(NoSuchElementException.class, emptyMap::firstKey);
    }

    @Test
    public void lastKey_returnsLastKeyFromUnderlyingMap() {
        SortedMap<String, Integer> transformedMap = TransformedSortedMap.transformingSortedMap(originalMap, null, null);
        assertEquals("two", transformedMap.lastKey());
    }

    @Test
    public void lastKey_onEmptyMap_throwsNoSuchElementException() {
        SortedMap<String, Integer> emptyMap = TransformedSortedMap.transformingSortedMap(new TreeMap<>(), null, null);
        assertThrows(NoSuchElementException.class, emptyMap::lastKey);
    }

    @Test
    public void comparator_whenUsingNaturalOrdering_returnsNull() {
        SortedMap<String, Integer> transformedMap = TransformedSortedMap.transformingSortedMap(new TreeMap<>(), null, null);
        assertNull("Comparator should be null for natural ordering", transformedMap.comparator());
    }

    @Test
    public void comparator_whenUsingCustomComparator_returnsThatComparator() {
        Comparator<String> reverseComparator = Comparator.reverseOrder();
        SortedMap<String, Integer> mapWithComparator = new TreeMap<>(reverseComparator);
        SortedMap<String, Integer> transformedMap = TransformedSortedMap.transformingSortedMap(mapWithComparator, null, null);

        assertSame("Should return the original comparator", reverseComparator, transformedMap.comparator());
    }

    // --- Test Sub-Map View Methods ---

    @Test
    public void headMap_returnsTransformedView() {
        SortedMap<String, Integer> transformedMap = TransformedSortedMap.transformingSortedMap(originalMap, null, valueTransformer);
        SortedMap<String, Integer> headMap = transformedMap.headMap("three");

        // The view itself should be a TransformedSortedMap
        assertTrue(headMap instanceof TransformedSortedMap);
        assertEquals(2, headMap.size());
        assertTrue(headMap.containsKey("one"));
        assertTrue(headMap.containsKey("two"));

        // Putting a new value into the view should transform it
        headMap.put("a_new_key", 0);
        assertEquals(Integer.valueOf(0), headMap.get("a_new_key")); // 0 * 10 = 0
    }

    @Test
    public void tailMap_returnsTransformedView() {
        SortedMap<String, Integer> transformedMap = TransformedSortedMap.transformingSortedMap(originalMap, null, valueTransformer);
        SortedMap<String, Integer> tailMap = transformedMap.tailMap("three");

        assertTrue(tailMap instanceof TransformedSortedMap);
        assertEquals(2, tailMap.size());
        assertTrue(tailMap.containsKey("three"));
        assertTrue(tailMap.containsKey("two"));
    }



    @Test
    public void subMap_returnsTransformedView() {
        SortedMap<String, Integer> transformedMap = TransformedSortedMap.transformingSortedMap(originalMap, null, valueTransformer);
        SortedMap<String, Integer> subMap = transformedMap.subMap("one", "three");

        assertTrue(subMap instanceof TransformedSortedMap);
        assertEquals(1, subMap.size());
        assertTrue(subMap.containsKey("one"));
        assertFalse(subMap.containsKey("three")); // toKey is exclusive
    }

    @Test
    public void subMap_onEmptyMap_returnsEmptyMap() {
        SortedMap<String, Integer> emptyMap = TransformedSortedMap.transformingSortedMap(new TreeMap<>(), null, null);
        SortedMap<String, Integer> subMap = emptyMap.subMap("a", "z");
        assertTrue(subMap.isEmpty());
    }

    @Test
    public void subMap_whenKeyIsOutOfRange_throwsIllegalArgumentException() {
        // Create a sub-map from the original map
        SortedMap<String, Integer> originalSubMap = originalMap.subMap("one", "three");
        
        // Decorate the sub-map
        SortedMap<String, Integer> transformedSubMap = TransformedSortedMap.transformingSortedMap(originalSubMap, null, null);
        
        // Accessing a key outside the sub-map's range should fail
        assertThrows(IllegalArgumentException.class, () -> transformedSubMap.tailMap("z"));
    }

    // --- Test General Map Behavior ---

    @Test
    public void put_withKeyAndValueTransformers_transformsBoth() {
        SortedMap<String, Integer> transformedMap =
            TransformedSortedMap.transformingSortedMap(new TreeMap<>(), keyTransformer, valueTransformer);

        transformedMap.put("four", 4);

        // Check that the key and value were transformed before being put into the underlying map
        assertFalse(transformedMap.containsKey("four"));
        assertTrue(transformedMap.containsKey("transformedKey_four"));
        assertEquals(Integer.valueOf(40), transformedMap.get("transformedKey_four"));
    }

    @Test
    public void put_withNonComparableTransformedKey_throwsClassCastException() {
        // A transformer that returns a non-comparable object for a key
        Transformer<String, Object> nonComparableKeyTransformer = key -> new Object();
        
        SortedMap<String, Object> map = new TreeMap<>();
        SortedMap<String, Object> transformedMap =
            TransformedSortedMap.transformingSortedMap(map, nonComparableKeyTransformer, null);

        // TreeMap requires keys to be Comparable, so this should fail.
        try {
            transformedMap.put("anyKey", "anyValue");
            fail("Expected a ClassCastException because the transformed key is not Comparable.");
        } catch (ClassCastException e) {
            // Expected
        }
    }
}