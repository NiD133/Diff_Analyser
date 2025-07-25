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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link CollectionSortedBag}.
 * <p>
 * This test focuses on the serialization aspects of {@link CollectionSortedBag}.
 * The underlying {@link CollectionSortedBag} functionality is thoroughly tested in {@link AbstractSortedBagTest}.
 */
public class CollectionSortedBagTest<T extends Comparable<T>> extends AbstractCollectionTest<T> {

    private static final int NUM_ELEMENTS = 30;

    /**
     *  The version number for compatibility testing.  Override if the class being tested
     *  has changed versions.
     */
    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * Creates a {@link CollectionSortedBag} for testing.
     * @return a new empty bag
     */
    @Override
    public Bag<T> makeObject() {
        return CollectionSortedBag.collectionSortedBag(new TreeBag<>());
    }

    /**
     * Creates an array of non-null comparable elements to populate the bag.
     * @return an array of non-null elements
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getFullNonNullElements() {
        final Object[] elements = new Object[NUM_ELEMENTS];

        for (int i = 0; i < NUM_ELEMENTS; i++) {
            elements[i] = Integer.valueOf(i + i + 1);
        }
        return (T[]) elements;
    }

    /**
     * Creates an array of different non-null comparable elements.
     * @return an array of non-null different elements
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getOtherNonNullElements() {
        final Object[] elements = new Object[NUM_ELEMENTS];
        for (int i = 0; i < NUM_ELEMENTS; i++) {
            elements[i] = Integer.valueOf(i + i + 2);
        }
        return (T[]) elements;
    }

    /**
     * Overridden because SortedBags don't allow null elements (normally).
     * @return false
     */
    @Override
    public boolean isNullSupported() {
        return false;
    }

    /**
     * Returns an empty ArrayList for use in modification testing.
     *
     * @return a confirmed empty collection
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Returns a full ArrayList for use in modification testing.
     *
     * @return a confirmed full collection
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> collection = makeConfirmedCollection();
        collection.addAll(Arrays.asList(getFullElements()));
        return collection;
    }

    // Test(s)
    /**
     * Tests serialization compatibility of an empty bag.  Compares the serialized
     * form of a newly created empty bag against a canonical version stored in SCM.
     * @throws IOException if serialization fails
     * @throws ClassNotFoundException if deserialization fails
     */
    @Test
    void testEmptyBagCompatibility() throws IOException, ClassNotFoundException {
        final Bag<T> bag = makeObject();
        assertSerializedFormIsCompatible(bag, getCanonicalEmptyCollectionName(bag));
    }

    /**
     * Tests serialization compatibility of a full bag. Compares the serialized
     * form of a newly created full bag against a canonical version stored in SCM.
     * @throws IOException if serialization fails
     * @throws ClassNotFoundException if deserialization fails
     */
    @Test
    void testFullBagCompatibility() throws IOException, ClassNotFoundException {
        final SortedBag<T> bag = (SortedBag<T>) makeFullCollection();
        assertSerializedFormIsCompatible(bag, getCanonicalFullCollectionName(bag));
    }

    // Helper method to reduce duplication
    private void assertSerializedFormIsCompatible(final Object bag, final String canonicalName)
            throws IOException, ClassNotFoundException {
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final Object deserializedBag = readExternalFormFromDisk(canonicalName);
            assertEquals(bag, deserializedBag, "The bag and deserialized bag should be equal.");
            assertEquals(((Collection<?>) bag).size(), ((Collection<?>) deserializedBag).size(), "Bag sizes should match");
        }
    }
}