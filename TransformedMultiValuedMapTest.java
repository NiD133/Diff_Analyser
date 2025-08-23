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

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.Transformer;
import org.apache.commons.collections4.TransformerUtils;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TransformedMultiValuedMap}.
 *
 * This suite focuses on understandability by:
 * - Using concrete, consistent generic types (Object,Object) to avoid unchecked casts.
 * - Introducing small helper constants and methods.
 * - Using descriptive test names and messages.
 * - Clearly separating Arrange/Act/Assert steps.
 */
public class TransformedMultiValuedMapTest extends AbstractMultiValuedMapTest<Object, Object> {

    // Common test data
    private static final List<String> NUM_STRINGS = Arrays.asList("1", "3", "5", "7", "2", "4", "6");

    // Local, well-typed transformers to avoid unchecked casts from other test utilities.
    private static final Transformer<Object, Object> STRING_TO_INTEGER =
            in -> in == null ? null : Integer.valueOf(in.toString());

    private static final Transformer<Object, Object> NOP_OBJECT = TransformerUtils.nopTransformer();

    @Override
    protected int getIterationBehaviour() {
        return AbstractCollectionTest.UNORDERED;
    }

    @Override
    public MultiValuedMap<Object, Object> makeObject() {
        // A simple, no-op transforming map for the base behavior required by the abstract test.
        return TransformedMultiValuedMap.transformingMap(
                new ArrayListValuedHashMap<>(),
                NOP_OBJECT,
                NOP_OBJECT
        );
    }

    private static MultiValuedMap<Object, Object> newBaseWithABC() {
        final MultiValuedMap<Object, Object> base = new ArrayListValuedHashMap<>();
        base.put("A", "1");
        base.put("B", "2");
        base.put("C", "3");
        return base;
    }

    @Test
    @DisplayName("transformingMap: does not transform existing entries, but transforms new ones")
    void transformingMap_doesNotTransformExistingEntries_transformsNewOnes() {
        // Arrange
        final MultiValuedMap<Object, Object> base = newBaseWithABC();

        // Act
        final MultiValuedMap<Object, Object> transformed =
                TransformedMultiValuedMap.transformingMap(base, null, STRING_TO_INTEGER);

        // Assert existing entries are not transformed
        assertEquals(3, transformed.size(), "Existing entries should remain untransformed");
        assertTrue(transformed.get("A").contains("1"));
        assertTrue(transformed.get("B").contains("2"));
        assertTrue(transformed.get("C").contains("3"));

        // Assert newly added entries are transformed
        transformed.put("D", "4");
        assertTrue(transformed.get("D").contains(4), "Newly added value should be transformed to Integer");
    }

    @Test
    @DisplayName("transformedMap: transforms existing entries and new ones")
    void transformedMap_transformsExistingAndNewEntries() {
        // Arrange
        final MultiValuedMap<Object, Object> base = newBaseWithABC();

        // Act
        final MultiValuedMap<Object, Object> transformed =
                TransformedMultiValuedMap.transformedMap(base, null, STRING_TO_INTEGER);

        // Assert: existing entries transformed
        assertEquals(3, transformed.size(), "All existing entries should be transformed");
        assertTrue(transformed.get("A").contains(1));
        assertTrue(transformed.get("B").contains(2));
        assertTrue(transformed.get("C").contains(3));

        // Assert: new entries transformed
        transformed.put("D", "4");
        assertTrue(transformed.get("D").contains(4));

        // Also verify behavior for an initially empty base map
        final MultiValuedMap<Object, Object> emptyBase = new ArrayListValuedHashMap<>();
        final MultiValuedMap<Object, Object> transformedEmpty =
                TransformedMultiValuedMap.transformedMap(emptyBase, null, STRING_TO_INTEGER);

        assertEquals(0, transformedEmpty.size(), "Empty map should remain empty");
        transformedEmpty.put("D", "4");
        assertEquals(1, transformedEmpty.size(), "Size should increase after put");
        assertTrue(transformedEmpty.get("D").contains(4), "Inserted value should be transformed");
    }

    @Test
    @DisplayName("transformingMap with key transformer: keys are transformed on put and used for remove")
    void keyTransformer_appliesOnPut_andOnRemove() {
        // Arrange
        final MultiValuedMap<Object, Object> map = TransformedMultiValuedMap.transformingMap(
                new ArrayListValuedHashMap<>(),
                STRING_TO_INTEGER,   // transform keys
                null                 // do not transform values
        );

        assertEquals(0, map.size(), "New map should be empty");

        // Act + Assert: on put, the key should be transformed to Integer; value stored as-is.
        for (int i = 0; i < NUM_STRINGS.size(); i++) {
            final String s = NUM_STRINGS.get(i);
            map.put(s, s);

            assertEquals(i + 1, map.size(), "Size should reflect number of inserts");
            assertTrue(map.containsKey(Integer.valueOf(s)), "Should contain transformed Integer key");
            assertFalse(map.containsKey(s), "Should not contain the original String key");
            assertTrue(map.containsValue(s), "Value is not transformed and should be the original string");
            assertTrue(map.get(Integer.valueOf(s)).contains(s), "Lookup by transformed key should find value");
        }

        // Removing by original (untransformed) key should not remove anything
        final String first = NUM_STRINGS.get(0);
        final Collection<Object> removedByOriginalKey = map.remove(first);
        assertNotNull(removedByOriginalKey, "remove should return a non-null collection");
        assertEquals(0, removedByOriginalKey.size(), "Removing by original key should remove nothing");

        // Removing by transformed key should return the stored value
        final Collection<Object> removedByTransformedKey = map.remove(Integer.valueOf(first));
        assertTrue(removedByTransformedKey.contains(first), "Removing by transformed key should return the value");
    }

    @Test
    @DisplayName("transformingMap with value transformer: values are transformed on put and on remove result")
    void valueTransformer_appliesOnPut_andOnRemove() {
        // Arrange
        final MultiValuedMap<Object, Object> map = TransformedMultiValuedMap.transformingMap(
                new ArrayListValuedHashMap<>(),
                null,                // do not transform keys
                STRING_TO_INTEGER    // transform values
        );

        assertEquals(0, map.size(), "New map should be empty");

        // Act + Assert: on put, the value is transformed to Integer; key is stored as-is.
        for (int i = 0; i < NUM_STRINGS.size(); i++) {
            final String s = NUM_STRINGS.get(i);
            map.put(s, s);

            assertEquals(i + 1, map.size(), "Size should reflect number of inserts");
            assertTrue(map.containsValue(Integer.valueOf(s)), "Should contain transformed Integer value");
            assertFalse(map.containsValue(s), "Should not contain the original String value");
            assertTrue(map.containsKey(s), "Key is untransformed and should be the original string");
            assertTrue(map.get(s).contains(Integer.valueOf(s)), "Lookup by key should return transformed value");
        }

        // Remove by key returns the collection of transformed values
        final String first = NUM_STRINGS.get(0);
        final Collection<Object> removed = map.remove(first);
        assertTrue(removed.contains(Integer.valueOf(first)), "Removed values should be transformed");
    }

//    // One-off utility to regenerate serialized forms when needed.
//    void testCreate() throws Exception {
//        writeExternalFormToDisk((java.io.Serializable) makeObject(),
//                "src/test/resources/data/test/TransformedMultiValuedMap.emptyCollection.version4.1.obj");
//        writeExternalFormToDisk((java.io.Serializable) makeFullMap(),
//                "src/test/resources/data/test/TransformedMultiValuedMap.fullCollection.version4.1.obj");
//    }
}