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
package org.apache.commons.collections4.bag;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.junit.jupiter.api.Test;

/**
 * Tests serialization compatibility for {@link CollectionSortedBag}.
 * <p>
 * This test focuses on verifying that serialized forms of empty and full bags remain stable.
 * Functional behavior is covered in {@link AbstractSortedBagTest}.
 * </p>
 */
public class CollectionSortedBagTest<T> extends AbstractCollectionTest<T> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * Generates 30 non-null, unique, and comparable elements (odd integers).
     * Required for sorted bag tests to ensure elements are comparable.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getFullNonNullElements() {
        // Generate 30 consecutive odd integers starting from 1
        final Integer[] elements = new Integer[30];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = 2 * i + 1; // 1, 3, 5, ..., 59
        }
        return (T[]) elements;
    }

    /**
     * Generates 30 alternative non-null elements (even integers).
     * Used for modification tests.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getOtherNonNullElements() {
        // Generate 30 consecutive even integers starting from 2
        final Integer[] elements = new Integer[30];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = 2 * i + 2; // 2, 4, 6, ..., 60
        }
        return (T[]) elements;
    }

    /**
     * Sorted bags do not support null elements.
     * @return false
     */
    @Override
    public boolean isNullSupported() {
        return false;
    }

    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> collection = makeConfirmedCollection();
        collection.addAll(Arrays.asList(getFullElements()));
        return collection;
    }

    @Override
    public Bag<T> makeObject() {
        return CollectionSortedBag.collectionSortedBag(new TreeBag<>());
    }

    /**
     * Verifies that the serialized form of an empty bag matches the canonical version.
     * <p>
     * This ensures backward compatibility for serialized empty bags.
     */
    @Test
    void testEmptyBagCompatibility() throws IOException, ClassNotFoundException {
        final Bag<T> emptyBag = makeObject();

        if (shouldTestSerializationCompatibility(emptyBag)) {
            final String serializedFileName = getCanonicalEmptyCollectionName(emptyBag);
            final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(serializedFileName);

            assertEquals(0, deserializedBag.size(), "Deserialized empty bag should have size 0");
            assertEquals(emptyBag, deserializedBag, "Deserialized empty bag should match original");
        }
    }

    /**
     * Verifies that the serialized form of a full bag matches the canonical version.
     * <p>
     * This ensures backward compatibility for serialized full bags.
     */
    @Test
    void testFullBagCompatibility() throws IOException, ClassNotFoundException {
        final SortedBag<T> fullBag = (SortedBag<T>) makeFullCollection();

        if (shouldTestSerializationCompatibility(fullBag)) {
            final String serializedFileName = getCanonicalFullCollectionName(fullBag);
            final SortedBag<?> deserializedBag = (SortedBag<?>) readExternalFormFromDisk(serializedFileName);

            assertEquals(fullBag.size(), deserializedBag.size(), "Deserialized full bag size mismatch");
            assertEquals(fullBag, deserializedBag, "Deserialized full bag contents mismatch");
        }
    }

    /**
     * Checks if serialization compatibility tests should run for the given bag.
     * 
     * @param bag The bag to test
     * @return true if tests should execute, false otherwise
     */
    private boolean shouldTestSerializationCompatibility(final Bag<?> bag) {
        return bag instanceof Serializable && 
               !skipSerializedCanonicalTests() && 
               isTestSerialization();
    }
}