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
import org.junit.jupiter.api.Test;

/**
 * Tests {@link TransformedMultiValuedMap}.
 *
 * This version of the test class has been improved for understandability by:
 * 1. Using descriptive test method names.
 * 2. Removing generics from the test class to eliminate casting and improve type safety.
 * 3. Structuring tests using the Arrange-Act-Assert pattern.
 * 4. Creating focused tests that verify a single behavior.
 * 5. Adding assertion messages for clarity.
 */
public class TransformedMultiValuedMapTest extends AbstractMultiValuedMapTest<String, String> {

    private static final Transformer<Object, Object> STRING_TO_INTEGER_TRANSFORMER = input -> Integer.valueOf(input.toString());

    @Override
    protected int getIterationBehaviour() {
        return AbstractCollectionTest.UNORDERED;
    }

    @Override
    public MultiValuedMap<String, String> makeObject() {
        return TransformedMultiValuedMap.transformingMap(new ArrayListValuedHashMap<>(),
                TransformerUtils.nopTransformer(), TransformerUtils.nopTransformer());
    }

    /**
     * Tests that {@code transformingMap()} decorates the map, transforming
     * new values that are added, but leaving existing values untouched.
     */
    @Test
    void testTransformingMap_doesNotTransformExistingValues() {
        // Arrange
        final MultiValuedMap<String, Object> baseMap = new ArrayListValuedHashMap<>();
        baseMap.put("A", "1"); // String value
        baseMap.put("B", "2"); // String value

        // Act: Decorate the map. This should not transform existing elements.
        final MultiValuedMap<String, Object> transformedMap =
                TransformedMultiValuedMap.transformingMap(baseMap, null, STRING_TO_INTEGER_TRANSFORMER);

        // Assert: Existing values are not transformed
        assertEquals(2, transformedMap.size());
        assertTrue(transformedMap.get("A").contains("1"), "Existing value for key 'A' should be the original String '1'");
        assertTrue(transformedMap.get("B").contains("2"), "Existing value for key 'B' should be the original String '2'");

        // Act: Add a new value, which should be transformed
        transformedMap.put("C", "3");

        // Assert: New value is transformed
        assertEquals(3, transformedMap.size());
        assertTrue(transformedMap.get("C").contains(3), "New value for key 'C' should be transformed to Integer 3");
        assertFalse(transformedMap.containsValue("3"), "Original string '3' should not be in the map's values");
    }

    /**
     * Tests that {@code transformedMap()} transforms both existing and new values.
     */
    @Test
    void testTransformedMap_transformsExistingAndNewValues() {
        // Arrange
        final MultiValuedMap<String, Object> baseMap = new ArrayListValuedHashMap<>();
        baseMap.put("A", "1");
        baseMap.put("B", "2");

        // Act: Create the transformed map, which transforms existing elements
        final MultiValuedMap<String, Object> transformedMap =
                TransformedMultiValuedMap.transformedMap(baseMap, null, STRING_TO_INTEGER_TRANSFORMER);

        // Assert: Existing values are transformed
        assertEquals(2, transformedMap.size());
        assertTrue(transformedMap.get("A").contains(1), "Existing value for 'A' should be transformed to Integer 1");
        assertTrue(transformedMap.get("B").contains(2), "Existing value for 'B' should be transformed to Integer 2");

        // Act: Add a new value
        transformedMap.put("C", "3");

        // Assert: New value is also transformed
        assertEquals(3, transformedMap.size());
        assertTrue(transformedMap.get("C").contains(3), "New value for 'C' should be transformed to Integer 3");
    }

    /**
     * Tests that a key transformer correctly transforms keys on put and that
     * the map must be accessed with the transformed key.
     */
    @Test
    void testTransformingMap_withKeyTransformer_usesTransformedKeyForAccess() {
        // Arrange
        final MultiValuedMap<Object, String> map = TransformedMultiValuedMap.transformingMap(
                new ArrayListValuedHashMap<>(), STRING_TO_INTEGER_TRANSFORMER, null);

        // Act
        map.put("1", "one");
        map.put("2", "two");

        // Assert
        assertEquals(2, map.size());

        // Assert that map must be accessed using the transformed key (Integer)
        assertTrue(map.containsKey(1));
        assertFalse(map.containsKey("1"));
        assertTrue(map.get(1).contains("one"));

        assertTrue(map.containsKey(2));
        assertFalse(map.containsKey("2"));
        assertTrue(map.get(2).contains("two"));

        // Assert values are not transformed
        assertTrue(map.containsValue("one"));
        assertTrue(map.containsValue("two"));
    }

    /**
     * Tests that remove() on a key-transformed map requires the transformed key.
     */
    @Test
    void testTransformingMap_withKeyTransformer_usesTransformedKeyForRemove() {
        // Arrange
        final MultiValuedMap<Object, String> map = TransformedMultiValuedMap.transformingMap(
                new ArrayListValuedHashMap<>(), STRING_TO_INTEGER_TRANSFORMER, null);
        map.put("1", "one");
        assertEquals(1, map.size());

        // Act: try to remove with original key
        final Collection<String> removedWithOriginalKey = map.remove("1");

        // Assert: removal with original key fails
        assertNotNull(removedWithOriginalKey);
        assertTrue(removedWithOriginalKey.isEmpty(), "Removing with original key should not find the entry.");
        assertEquals(1, map.size(), "Map size should be unchanged after failed remove.");

        // Act: remove with transformed key
        final Collection<String> removedWithTransformedKey = map.remove(1);

        // Assert: removal with transformed key succeeds
        assertNotNull(removedWithTransformedKey);
        assertTrue(removedWithTransformedKey.contains("one"), "Removing with transformed key should return the value.");
        assertEquals(0, map.size(), "Map should be empty after successful remove.");
    }

    /**
     * Tests that a value transformer correctly transforms values on put and that
     * the map contains the transformed value.
     */
    @Test
    void testTransformingMap_withValueTransformer_transformsValueOnPut() {
        // Arrange
        final MultiValuedMap<String, Object> map = TransformedMultiValuedMap.transformingMap(
                new ArrayListValuedHashMap<>(), null, STRING_TO_INTEGER_TRANSFORMER);

        // Act
        map.put("A", "1");
        map.put("B", "2");

        // Assert
        assertEquals(2, map.size());

        // Assert that keys are not transformed
        assertTrue(map.containsKey("A"));
        assertTrue(map.containsKey("B"));

        // Assert that values are transformed
        assertTrue(map.containsValue(1));
        assertFalse(map.containsValue("1"));
        assertTrue(map.get("A").contains(1));

        assertTrue(map.containsValue(2));
        assertFalse(map.containsValue("2"));
        assertTrue(map.get("B").contains(2));

        // Assert that removing by key returns the transformed value
        assertTrue(map.remove("A").contains(1));
        assertEquals(1, map.size());
    }

}