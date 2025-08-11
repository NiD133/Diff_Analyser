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
package org.apache.commons.collections4.multimap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.apache.commons.collections4.collection.TransformedCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Tests {@link TransformedMultiValuedMap}.
 * 
 * This test class verifies that TransformedMultiValuedMap correctly applies transformations
 * to keys and values when they are added to the map. It tests both factory methods:
 * - transformingMap(): transforms only new entries
 * - transformedMap(): transforms both existing and new entries
 */
public class TransformedMultiValuedMapTest<K, V> extends AbstractMultiValuedMapTest<K, V> {

    // Test data constants for better readability
    private static final String KEY_A = "A";
    private static final String KEY_B = "B";
    private static final String KEY_C = "C";
    private static final String KEY_D = "D";
    
    private static final String VALUE_1 = "1";
    private static final String VALUE_2 = "2";
    private static final String VALUE_3 = "3";
    private static final String VALUE_4 = "4";
    
    private static final String[] TEST_ELEMENTS = {"1", "3", "5", "7", "2", "4", "6"};

    @Override
    protected int getIterationBehaviour() {
        return AbstractCollectionTest.UNORDERED;
    }

    @Override
    public MultiValuedMap<K, V> makeObject() {
        // Create a map with no-op transformers for basic functionality testing
        return TransformedMultiValuedMap.transformingMap(
            new ArrayListValuedHashMap<>(),
            TransformerUtils.<K>nopTransformer(), 
            TransformerUtils.<V>nopTransformer()
        );
    }

    /**
     * Tests transformingMap() factory method - should NOT transform existing entries,
     * but SHOULD transform new entries added after creation.
     */
    @Test
    void testTransformingMap_DoesNotTransformExistingEntries() {
        // Given: a base map with existing string entries
        final MultiValuedMap<String, String> baseMap = createBaseMapWithStringEntries();
        
        // When: creating a transforming map with string-to-integer value transformer
        final MultiValuedMap<String, String> transformingMap = TransformedMultiValuedMap.transformingMap(
            baseMap,
            null, // no key transformation
            getStringToIntegerTransformer()
        );
        
        // Then: existing entries should remain as strings (not transformed)
        assertEquals(3, transformingMap.size());
        assertContainsStringValue(transformingMap, KEY_A, VALUE_1);
        assertContainsStringValue(transformingMap, KEY_B, VALUE_2);
        assertContainsStringValue(transformingMap, KEY_C, VALUE_3);
        
        // When: adding a new entry
        transformingMap.put(KEY_D, VALUE_4);
        
        // Then: new entry should be transformed to integer
        assertContainsIntegerValue(transformingMap, KEY_D, 4);
    }

    /**
     * Tests transformedMap() factory method - should transform BOTH existing and new entries.
     */
    @Test
    void testTransformedMap_TransformsBothExistingAndNewEntries() {
        // Given: a base map with existing string entries
        final MultiValuedMap<String, String> baseMap = createBaseMapWithStringEntries();
        
        // When: creating a transformed map with string-to-integer value transformer
        final MultiValuedMap<String, String> transformedMap = TransformedMultiValuedMap.transformedMap(
            baseMap,
            null, // no key transformation
            getStringToIntegerTransformer()
        );
        
        // Then: existing entries should be transformed to integers
        assertEquals(3, transformedMap.size());
        assertContainsIntegerValue(transformedMap, KEY_A, 1);
        assertContainsIntegerValue(transformedMap, KEY_B, 2);
        assertContainsIntegerValue(transformedMap, KEY_C, 3);
        
        // When: adding a new entry
        transformedMap.put(KEY_D, VALUE_4);
        
        // Then: new entry should also be transformed to integer
        assertContainsIntegerValue(transformedMap, KEY_D, 4);
    }

    /**
     * Tests transformedMap() with empty base map to ensure it works correctly
     * when no existing entries need transformation.
     */
    @Test
    void testTransformedMap_WithEmptyBaseMap() {
        // Given: an empty base map
        final MultiValuedMap<String, String> emptyBaseMap = new ArrayListValuedHashMap<>();
        
        // When: creating a transformed map with string-to-integer value transformer
        final MultiValuedMap<String, String> transformedMap = TransformedMultiValuedMap.transformedMap(
            emptyBaseMap,
            null, // no key transformation
            getStringToIntegerTransformer()
        );
        
        // Then: map should be empty initially
        assertEquals(0, transformedMap.size());
        
        // When: adding an entry
        transformedMap.put(KEY_D, VALUE_4);
        
        // Then: entry should be transformed
        assertEquals(1, transformedMap.size());
        assertContainsIntegerValue(transformedMap, KEY_D, 4);
    }

    /**
     * Tests key transformation functionality.
     * Keys should be transformed when added, affecting storage and retrieval.
     */
    @Test
    void testKeyTransformation() {
        // Given: a transforming map with string-to-integer key transformer
        final MultiValuedMap<String, String> map = TransformedMultiValuedMap.transformingMap(
            new ArrayListValuedHashMap<>(),
            getStringToIntegerTransformer(),
            null // no value transformation
        );
        
        assertEquals(0, map.size());
        
        // When: adding entries with string keys
        for (int i = 0; i < TEST_ELEMENTS.length; i++) {
            String element = TEST_ELEMENTS[i];
            map.put(element, element);
            
            // Then: map size should increase
            assertEquals(i + 1, map.size());
            
            // Keys should be transformed to integers
            Integer transformedKey = Integer.valueOf(element);
            assertTrue(map.containsKey(transformedKey), 
                "Map should contain transformed key: " + transformedKey);
            assertFalse(map.containsKey(element), 
                "Map should not contain original string key: " + element);
            
            // Values should remain as strings
            assertTrue(map.containsValue(element), 
                "Map should contain original string value: " + element);
            assertTrue(map.get(transformedKey).contains(element), 
                "Transformed key should map to original string value");
        }

        // Test removal with original key (should fail)
        String firstElement = TEST_ELEMENTS[0];
        Collection<String> removedWithStringKey = map.remove(firstElement);
        assertNotNull(removedWithStringKey);
        assertEquals(0, removedWithStringKey.size(), 
            "Removal with original string key should return empty collection");

        // Test removal with transformed key (should succeed)
        Integer transformedFirstKey = Integer.valueOf(firstElement);
        Collection<String> removedWithIntegerKey = map.remove(transformedFirstKey);
        assertTrue(removedWithIntegerKey.contains(firstElement), 
            "Removal with transformed key should return the original value");
    }

    /**
     * Tests value transformation functionality.
     * Values should be transformed when added, affecting storage and retrieval.
     */
    @Test
    void testValueTransformation() {
        // Given: a transforming map with string-to-integer value transformer
        final MultiValuedMap<String, String> map = TransformedMultiValuedMap.transformingMap(
            new ArrayListValuedHashMap<>(),
            null, // no key transformation
            getStringToIntegerTransformer()
        );
        
        assertEquals(0, map.size());
        
        // When: adding entries with string values
        for (int i = 0; i < TEST_ELEMENTS.length; i++) {
            String element = TEST_ELEMENTS[i];
            map.put(element, element);
            
            // Then: map size should increase
            assertEquals(i + 1, map.size());
            
            // Values should be transformed to integers
            Integer transformedValue = Integer.valueOf(element);
            assertTrue(map.containsValue(transformedValue), 
                "Map should contain transformed value: " + transformedValue);
            assertFalse(map.containsValue(element), 
                "Map should not contain original string value: " + element);
            
            // Keys should remain as strings
            assertTrue(map.containsKey(element), 
                "Map should contain original string key: " + element);
            assertTrue(map.get(element).contains(transformedValue), 
                "Original key should map to transformed integer value");
        }

        // Test removal - should return transformed values
        String firstElement = TEST_ELEMENTS[0];
        Integer expectedTransformedValue = Integer.valueOf(firstElement);
        Collection<String> removedValues = map.remove(firstElement);
        assertTrue(removedValues.contains(expectedTransformedValue), 
            "Removed values should contain the transformed integer value");
    }

    // Helper methods for better test readability

    private MultiValuedMap<String, String> createBaseMapWithStringEntries() {
        final MultiValuedMap<String, String> baseMap = new ArrayListValuedHashMap<>();
        baseMap.put(KEY_A, VALUE_1);
        baseMap.put(KEY_B, VALUE_2);
        baseMap.put(KEY_C, VALUE_3);
        return baseMap;
    }

    @SuppressWarnings("unchecked")
    private Transformer<String, String> getStringToIntegerTransformer() {
        return (Transformer<String, String>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;
    }

    private void assertContainsStringValue(MultiValuedMap<String, String> map, String key, String expectedValue) {
        assertTrue(map.get(key).contains(expectedValue), 
            String.format("Map should contain string value '%s' for key '%s'", expectedValue, key));
    }

    private void assertContainsIntegerValue(MultiValuedMap<String, String> map, String key, Integer expectedValue) {
        assertTrue(map.get(key).contains(expectedValue), 
            String.format("Map should contain integer value %d for key '%s'", expectedValue, key));
    }

//    void testCreate() throws Exception {
//        writeExternalFormToDisk((java.io.Serializable) makeObject(),
//                "src/test/resources/data/test/TransformedMultiValuedMap.emptyCollection.version4.1.obj");
//        writeExternalFormToDisk((java.io.Serializable) makeFullMap(),
//                "src/test/resources/data/test/TransformedMultiValuedMap.fullCollection.version4.1.obj");
//    }
}