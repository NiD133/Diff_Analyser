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
     * Tests the transformingSortedMap factory method that applies transformations
     * only to newly added elements (existing elements remain unchanged).
     */
    @Test
    @SuppressWarnings("unchecked")
    void testTransformingSortedMapFactory_OnlyTransformsNewElements() {
        // Given: A base map with existing string values
        final SortedMap<K, V> baseMap = createBaseMapWithStringValues();

        // When: Creating a transforming map that converts string values to integers
        final SortedMap<K, V> transformingMap = TransformedSortedMap.transformingSortedMap(
                baseMap,
                null, // No key transformation
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);

        // Then: Existing values remain as strings (not transformed)
        assertEquals(3, transformingMap.size());
        assertEquals(VALUE_1, transformingMap.get(KEY_A)); // Still string
        assertEquals(VALUE_2, transformingMap.get(KEY_B)); // Still string
        assertEquals(VALUE_3, transformingMap.get(KEY_C)); // Still string
        
        // But new values are transformed to integers
        transformingMap.put((K) KEY_D, (V) VALUE_4);
        assertEquals(Integer.valueOf(4), transformingMap.get(KEY_D)); // Transformed to integer
    }

    /**
     * Tests the transformedSortedMap factory method that applies transformations
     * to both existing and newly added elements.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testTransformedSortedMapFactory_TransformsAllElements() {
        // Given: A base map with existing string values
        final SortedMap<K, V> baseMap = createBaseMapWithStringValues();

        // When: Creating a transformed map that converts string values to integers
        final SortedMap<K, V> transformedMap = TransformedSortedMap.transformedSortedMap(
                baseMap,
                null, // No key transformation
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);

        // Then: All values (existing and new) are transformed to integers
        assertEquals(3, transformedMap.size());
        assertEquals(Integer.valueOf(1), transformedMap.get(KEY_A)); // Transformed to integer
        assertEquals(Integer.valueOf(2), transformedMap.get(KEY_B)); // Transformed to integer
        assertEquals(Integer.valueOf(3), transformedMap.get(KEY_C)); // Transformed to integer
        
        // New values are also transformed
        transformedMap.put((K) KEY_D, (V) VALUE_4);
        assertEquals(Integer.valueOf(4), transformedMap.get(KEY_D)); // Transformed to integer
    }

    /**
     * Tests comprehensive transformation behavior including key transformations,
     * value transformations, and entry set modifications.
     */
    @Test
    @SuppressWarnings("unchecked")
    void testTransformationBehavior() {
        testKeyTransformation();
        testValueTransformation();
        testEntrySetModification();
    }

    /**
     * Tests key transformation from strings to integers.
     */
    @SuppressWarnings("unchecked")
    private void testKeyTransformation() {
        // Given: A map that transforms string keys to integers
        SortedMap<K, V> keyTransformingMap = TransformedSortedMap.transformingSortedMap(
                new TreeMap<>(),
                (Transformer<? super K, ? extends K>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER,
                null); // No value transformation

        // When: Adding elements with string keys
        assertEquals(0, keyTransformingMap.size());
        for (int i = 0; i < TEST_ELEMENTS.length; i++) {
            String element = TEST_ELEMENTS[i];
            keyTransformingMap.put((K) element, (V) element);
            
            // Then: Keys are transformed to integers
            assertEquals(i + 1, keyTransformingMap.size());
            Integer transformedKey = Integer.valueOf(element);
            assertTrue(keyTransformingMap.containsKey(transformedKey));
            
            // Original string keys are not found (they were transformed)
            final SortedMap<K, V> finalMap = keyTransformingMap;
            final String originalKey = element;
            assertThrows(ClassCastException.class, () -> finalMap.containsKey(originalKey));
            
            // Values remain unchanged
            assertTrue(keyTransformingMap.containsValue(element));
            assertEquals(element, keyTransformingMap.get(transformedKey));
        }

        // Test removal with transformed key
        String firstElement = TEST_ELEMENTS[0];
        Integer firstTransformedKey = Integer.valueOf(firstElement);
        
        // Cannot remove with original string key
        final SortedMap<K, V> finalMap = keyTransformingMap;
        assertThrows(ClassCastException.class, () -> finalMap.remove(firstElement));
        
        // Can remove with transformed integer key
        assertEquals(firstElement, keyTransformingMap.remove(firstTransformedKey));
    }

    /**
     * Tests value transformation from strings to integers.
     */
    @SuppressWarnings("unchecked")
    private void testValueTransformation() {
        // Given: A map that transforms string values to integers
        SortedMap<K, V> valueTransformingMap = TransformedSortedMap.transformingSortedMap(
                new TreeMap<>(),
                null, // No key transformation
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);

        // When: Adding elements with string values
        assertEquals(0, valueTransformingMap.size());
        for (int i = 0; i < TEST_ELEMENTS.length; i++) {
            String element = TEST_ELEMENTS[i];
            valueTransformingMap.put((K) element, (V) element);
            
            // Then: Values are transformed to integers
            assertEquals(i + 1, valueTransformingMap.size());
            Integer transformedValue = Integer.valueOf(element);
            assertTrue(valueTransformingMap.containsValue(transformedValue));
            
            // Original string values are not found (they were transformed)
            assertFalse(valueTransformingMap.containsValue(element));
            
            // Keys remain unchanged
            assertTrue(valueTransformingMap.containsKey(element));
            assertEquals(transformedValue, valueTransformingMap.get(element));
        }

        // Test removal returns transformed value
        String firstElement = TEST_ELEMENTS[0];
        Integer expectedTransformedValue = Integer.valueOf(firstElement);
        assertEquals(expectedTransformedValue, valueTransformingMap.remove(firstElement));
    }

    /**
     * Tests that entry set modifications properly apply value transformations.
     */
    @SuppressWarnings("unchecked")
    private void testEntrySetModification() {
        // Given: A map with value transformation and some existing data
        SortedMap<K, V> valueTransformingMap = TransformedSortedMap.transformingSortedMap(
                new TreeMap<>(),
                null,
                (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER);
        
        // Add test data (skip first element for removal test)
        for (int i = 1; i < TEST_ELEMENTS.length; i++) {
            valueTransformingMap.put((K) TEST_ELEMENTS[i], (V) TEST_ELEMENTS[i]);
        }

        // When: Modifying values through entry set array
        final Set<Map.Entry<K, V>> entrySet = valueTransformingMap.entrySet();
        final Map.Entry<K, V>[] entryArray = entrySet.toArray(new Map.Entry[0]);
        
        entryArray[0].setValue((V) "66");
        
        // Then: Value is transformed and stored correctly
        assertEquals(Integer.valueOf(66), entryArray[0].getValue());
        assertEquals(Integer.valueOf(66), valueTransformingMap.get(entryArray[0].getKey()));

        // When: Modifying values through entry set iterator
        final Map.Entry<K, V> iteratorEntry = entrySet.iterator().next();
        iteratorEntry.setValue((V) "88");
        
        // Then: Value is transformed and stored correctly
        assertEquals(Integer.valueOf(88), iteratorEntry.getValue());
        assertEquals(Integer.valueOf(88), valueTransformingMap.get(iteratorEntry.getKey()));
    }

    /**
     * Helper method to create a base map with string values for testing.
     */
    @SuppressWarnings("unchecked")
    private SortedMap<K, V> createBaseMapWithStringValues() {
        final SortedMap<K, V> baseMap = new TreeMap<>();
        baseMap.put((K) KEY_A, (V) VALUE_1);
        baseMap.put((K) KEY_B, (V) VALUE_2);
        baseMap.put((K) KEY_C, (V) VALUE_3);
        return baseMap;
    }

//    void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "src/test/resources/data/test/TransformedSortedMap.emptyCollection.version4.obj");
//        resetFull();
//        writeExternalFormToDisk(
//            (java.io.Serializable) map,
//            "src/test/resources/data/test/TransformedSortedMap.fullCollection.version4.obj");
//    }
}