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
 * Unit tests for {@link CollectionSortedBag}.
 * <p>
 * This test class focuses on serialization support for CollectionSortedBag.
 * The functionality of the decorator is extensively tested in AbstractSortedBagTest.
 */
public class CollectionSortedBagTest<T> extends AbstractCollectionTest<T> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * Provides an array of comparable non-null elements for testing.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getFullNonNullElements() {
        final Object[] elements = new Object[30];
        for (int i = 0; i < 30; i++) {
            elements[i] = Integer.valueOf(i + i + 1);
        }
        return (T[]) elements;
    }

    /**
     * Provides an alternative array of comparable non-null elements for testing.
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getOtherNonNullElements() {
        final Object[] elements = new Object[30];
        for (int i = 0; i < 30; i++) {
            elements[i] = Integer.valueOf(i + i + 2);
        }
        return (T[]) elements;
    }

    /**
     * Indicates that null elements are not supported by SortedBags.
     */
    @Override
    public boolean isNullSupported() {
        return false;
    }

    /**
     * Creates an empty collection for modification testing.
     *
     * @return an empty collection
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Creates a full collection for modification testing.
     *
     * @return a full collection
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> collection = makeConfirmedCollection();
        collection.addAll(Arrays.asList(getFullElements()));
        return collection;
    }

    /**
     * Creates a CollectionSortedBag instance for testing.
     *
     * @return a new CollectionSortedBag instance
     */
    @Override
    public Bag<T> makeObject() {
        return CollectionSortedBag.collectionSortedBag(new TreeBag<>());
    }

    /**
     * Tests the compatibility of an empty CollectionSortedBag with its serialized form.
     */
    @Test
    void testEmptyBagCompatibility() throws IOException, ClassNotFoundException {
        final Bag<T> bag = makeObject();
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(bag));
            assertEquals(0, deserializedBag.size(), "Deserialized bag should be empty");
            assertEquals(bag, deserializedBag, "Deserialized bag should match the original");
        }
    }

    /**
     * Tests the compatibility of a full CollectionSortedBag with its serialized form.
     */
    @Test
    void testFullBagCompatibility() throws IOException, ClassNotFoundException {
        final SortedBag<T> bag = (SortedBag<T>) makeFullCollection();
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final SortedBag<?> deserializedBag = (SortedBag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(bag));
            assertEquals(bag.size(), deserializedBag.size(), "Deserialized bag should have the correct size");
            assertEquals(bag, deserializedBag, "Deserialized bag should match the original");
        }
    }
}