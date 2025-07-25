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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.apache.commons.collections4.functors.NonePredicate;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link CollectionBag}.
 * <p>
 * This test class focuses on serialization support for the CollectionBag decorator.
 * The CollectionBag is extensively used and tested in AbstractBagTest.
 */
public class CollectionBagTest<T> extends AbstractCollectionTest<T> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    /**
     * Provides an empty collection for modification testing.
     *
     * @return a confirmed empty collection
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Provides a full collection for modification testing.
     *
     * @return a confirmed full collection
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> collection = makeConfirmedCollection();
        collection.addAll(Arrays.asList(getFullElements()));
        return collection;
    }

    @Override
    public Bag<T> makeObject() {
        return CollectionBag.collectionBag(new HashBag<>());
    }

    /**
     * Test adding elements with a custom comparator.
     */
    @Test
    void testAddWithCustomComparator() throws Throwable {
        final TreeBag<Predicate<Object>> treeBag = new TreeBag<>(Comparator.comparing(Predicate::toString));
        final CollectionBag<Predicate<Object>> collectionBag = new CollectionBag<>(treeBag);
        collectionBag.add(NonePredicate.nonePredicate(collectionBag), 24);
    }

    /**
     * Test adding elements with the default comparator, expecting a ClassCastException.
     */
    @Test
    void testAddWithDefaultComparator() throws Throwable {
        final TreeBag<Predicate<Object>> treeBag = new TreeBag<>();
        final CollectionBag<Predicate<Object>> collectionBag = new CollectionBag<>(treeBag);
        assertThrows(ClassCastException.class, () -> collectionBag.add(NonePredicate.nonePredicate(collectionBag), 24));
    }

    /**
     * Compares the current serialized form of the Bag against the canonical version in SCM.
     */
    @Test
    void testEmptyBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        final Bag<T> bag = makeObject();
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(bag));
            assertTrue(deserializedBag.isEmpty(), "Deserialized bag should be empty");
            assertEquals(bag, deserializedBag, "Deserialized bag should match the original");
        }
    }

    /**
     * Compares the current serialized form of the Bag against the canonical version in SCM.
     */
    @Test
    void testFullBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        final Bag<T> bag = (Bag<T>) makeFullCollection();
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(bag));
            assertEquals(bag.size(), deserializedBag.size(), "Deserialized bag should have the same size");
            assertEquals(bag, deserializedBag, "Deserialized bag should match the original");
        }
    }
}