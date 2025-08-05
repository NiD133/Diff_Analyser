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
 * Tests for {@link TransformedMultiValuedMap}.
 */
public class TransformedMultiValuedMapTest<K, V> extends AbstractMultiValuedMapTest<K, V> {

    @Override
    protected int getIterationBehaviour() {
        return AbstractCollectionTest.UNORDERED;
    }

    @Override
    public MultiValuedMap<K, V> makeObject() {
        return TransformedMultiValuedMap.transformingMap(
            new ArrayListValuedHashMap<>(),
            TransformerUtils.nopTransformer(),
            TransformerUtils.nopTransformer()
        );
    }

    @Test
    @SuppressWarnings("unchecked")
    void testTransformingMap_DoesNotTransformExistingElements_TransformsNewElements() {
        // Arrange: Create base map with initial elements
        final MultiValuedMap<K, V> baseMap = new ArrayListValuedHashMap<>();
        baseMap.put((K) "A", (V) "1");
        baseMap.put((K) "B", (V) "2");
        baseMap.put((K) "C", (V) "3");
        final Transformer<String, Integer> stringToInteger = 
            TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

        // Act: Create transformed map (only values will be transformed for new entries)
        final MultiValuedMap<K, V> transformedMap = TransformedMultiValuedMap.transformingMap(
            baseMap,
            null,
            (Transformer<? super V, ? extends V>) stringToInteger
        );

        // Assert: Existing elements remain untransformed
        assertEquals(3, transformedMap.size(), "Size should match base map");
        assertTrue(transformedMap.get((K) "A").contains("1"), "Value 'A' should contain '1'");
        assertTrue(transformedMap.get((K) "B").contains("2"), "Value 'B' should contain '2'");
        assertTrue(transformedMap.get((K) "C").contains("3"), "Value 'C' should contain '3'");

        // Act: Add new entry
        transformedMap.put((K) "D", (V) "4");
        
        // Assert: New value is transformed
        assertTrue(transformedMap.get((K) "D").contains(4), "Value 'D' should contain transformed integer 4");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testTransformedMap_TransformsExistingAndNewElements() {
        // Arrange: Create base map with initial elements
        final MultiValuedMap<K, V> baseMap = new ArrayListValuedHashMap<>();
        baseMap.put((K) "A", (V) "1");
        baseMap.put((K) "B", (V) "2");
        baseMap.put((K) "C", (V) "3");
        final Transformer<String, Integer> stringToInteger = 
            TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER;

        // Act: Create transformed map (transforms existing and new values)
        final MultiValuedMap<K, V> transformedMap = TransformedMultiValuedMap.transformedMap(
            baseMap,
            null,
            (Transformer<? super V, ? extends V>) stringToInteger
        );

        // Assert: Existing elements are transformed
        assertEquals(3, transformedMap.size(), "Size should match base map");
        assertTrue(transformedMap.get((K) "A").contains(1), "Value 'A' should contain transformed integer 1");
        assertTrue(transformedMap.get((K) "B").contains(2), "Value 'B' should contain transformed integer 2");
        assertTrue(transformedMap.get((K) "C").contains(3), "Value 'C' should contain transformed integer 3");

        // Act: Add new entry
        transformedMap.put((K) "D", (V) "4");
        
        // Assert: New value is transformed
        assertTrue(transformedMap.get((K) "D").contains(4), "Value 'D' should contain transformed integer 4");

        // Test with empty base map
        // Arrange: Create empty base map
        final MultiValuedMap<K, V> emptyBaseMap = new ArrayListValuedHashMap<>();
        
        // Act: Create transformed map from empty map
        final MultiValuedMap<K, V> transformedEmptyMap = TransformedMultiValuedMap.transformedMap(
            emptyBaseMap,
            null,
            (Transformer<? super V, ? extends V>) stringToInteger
        );
        
        // Assert: Map starts empty
        assertEquals(0, transformedEmptyMap.size(), "Size should be 0 for empty map");
        
        // Act: Add entry to empty map
        transformedEmptyMap.put((K) "D", (V) "4");
        
        // Assert: New value is transformed
        assertEquals(1, transformedEmptyMap.size(), "Size should be 1 after addition");
        assertTrue(transformedEmptyMap.get((K) "D").contains(4), "Value 'D' should contain transformed integer 4");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testKeyTransformation() {
        // Arrange: Create map with key transformer (string->integer)
        final MultiValuedMap<K, V> transformedMap = TransformedMultiValuedMap.transformingMap(
            new ArrayListValuedHashMap<>(),
            (Transformer<? super K, ? extends K>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER,
            null
        );
        final String[] testElements = {"1", "3", "5", "7", "2", "4", "6"};

        // Act & Assert: Add elements and verify transformation
        for (int i = 0; i < testElements.length; i++) {
            final String element = testElements[i];
            final Integer transformedKey = Integer.valueOf(element);
            
            // Act: Add entry
            transformedMap.put((K) element, (V) element);
            
            // Assert: Verify size and transformation
            assertEquals(i + 1, transformedMap.size(), "Size should increment after addition");
            assertTrue(transformedMap.containsKey(transformedKey), 
                "Map should contain transformed key: " + transformedKey);
            assertFalse(transformedMap.containsKey(element), 
                "Map should not contain untransformed key: " + element);
            assertTrue(transformedMap.containsValue(element), 
                "Map should contain value: " + element);
            assertTrue(transformedMap.get((K) transformedKey).contains(element), 
                "Values for key " + transformedKey + " should contain: " + element);
        }

        // Test removal
        final String elementToRemove = testElements[0];
        final Integer transformedKey = Integer.valueOf(elementToRemove);
        
        // Act: Attempt removal with untransformed key
        final Collection<V> failedRemoveResult = transformedMap.remove(elementToRemove);
        assertNotNull(failedRemoveResult, "Remove should return collection even for missing key");
        assertEquals(0, failedRemoveResult.size(), "Collection should be empty when removing with untransformed key");
        
        // Act: Remove with transformed key
        final Collection<V> removeResult = transformedMap.remove(transformedKey);
        assertTrue(removeResult.contains(elementToRemove), 
            "Removed collection should contain: " + elementToRemove);
    }

    @Test
    @SuppressWarnings("unchecked")
    void testValueTransformation() {
        // Arrange: Create map with value transformer (string->integer)
        final MultiValuedMap<K, V> transformedMap = TransformedMultiValuedMap.transformingMap(
            new ArrayListValuedHashMap<>(),
            null,
            (Transformer<? super V, ? extends V>) TransformedCollectionTest.STRING_TO_INTEGER_TRANSFORMER
        );
        final String[] testElements = {"1", "3", "5", "7", "2", "4", "6"};

        // Act & Assert: Add elements and verify transformation
        for (int i = 0; i < testElements.length; i++) {
            final String element = testElements[i];
            final Integer transformedValue = Integer.valueOf(element);
            
            // Act: Add entry
            transformedMap.put((K) element, (V) element);
            
            // Assert: Verify size and transformation
            assertEquals(i + 1, transformedMap.size(), "Size should increment after addition");
            assertTrue(transformedMap.containsValue(transformedValue), 
                "Map should contain transformed value: " + transformedValue);
            assertFalse(transformedMap.containsValue(element), 
                "Map should not contain untransformed value: " + element);
            assertTrue(transformedMap.containsKey(element), 
                "Map should contain key: " + element);
            assertTrue(transformedMap.get((K) element).contains(transformedValue), 
                "Values for key " + element + " should contain transformed value: " + transformedValue);
        }

        // Test removal
        final String elementToRemove = testElements[0];
        final Integer expectedTransformedValue = Integer.valueOf(elementToRemove);
        
        // Act: Remove values by key
        final Collection<V> removedValues = transformedMap.remove(elementToRemove);
        
        // Assert: Verify removed value was transformed
        assertTrue(removedValues.contains(expectedTransformedValue), 
            "Removed values should contain transformed value: " + expectedTransformedValue);
    }
}