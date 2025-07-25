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
 * Unless required by applicable law or agreed to in writing, software,
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link CollectionBag}.
 * <p>
 * This test focuses primarily on the serialization and basic functionality
 * of {@link CollectionBag}.  For more comprehensive bag functionality testing,
 * see {@link AbstractBagTest}.
 * </p>
 */
public class CollectionBagTest<T> extends AbstractCollectionTest<T> {

    private Bag<T> bag;

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    /**
     * Creates an empty ArrayList for use in modification testing.
     *
     * @return a confirmed empty collection
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Creates a full ArrayList for use in modification testing.
     *
     * @return a confirmed full collection
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> collection = makeConfirmedCollection();
        collection.addAll(Arrays.asList(getFullElements()));
        return collection;
    }

    /**
     * Creates a {@link CollectionBag} instance for testing.  It wraps a
     * {@link HashBag}.
     *
     * @return a new CollectionBag instance
     */
    @Override
    public Bag<T> makeObject() {
        return CollectionBag.collectionBag(new HashBag<>());
    }

    @BeforeEach
    public void setup() {
        bag = makeObject();
    }


    /**
     * Tests the {@link CollectionBag#add(Object, int)} method when the underlying
     * bag is a {@link TreeBag} using a custom comparator, and attempts to add
     * a {@link Predicate} object.
     * <p>
     * This test specifically checks if the custom comparator is correctly used when
     * adding elements to the TreeBag.
     */
    @Test
    void testAddWithPredicateAndCustomComparator() {
        final TreeBag<Predicate<Object>> treeBag = new TreeBag<>(Comparator.comparing(Predicate::toString));
        final CollectionBag<Predicate<Object>> collectionBag = new CollectionBag<>(treeBag);
        collectionBag.add(NonePredicate.nonePredicate(collectionBag), 24);
        assertEquals(24, collectionBag.getCount(NonePredicate.nonePredicate(collectionBag)), "Element should be added with correct count.");
    }

    /**
     * Tests the {@link CollectionBag#add(Object, int)} method when the underlying
     * bag is a {@link TreeBag} using the default comparator, and attempts to add
     * a {@link Predicate} object.
     * <p>
     * This test expects a {@link ClassCastException} because {@link Predicate} does not
     * implement {@link Comparable} and no comparator is provided for the {@link TreeBag}.
     */
    @Test
    void testAddWithPredicateAndDefaultComparator() {
        final TreeBag<Predicate<Object>> treeBag = new TreeBag<>();
        final CollectionBag<Predicate<Object>> collectionBag = new CollectionBag<>(treeBag);
        assertThrows(ClassCastException.class, () -> collectionBag.add(NonePredicate.nonePredicate(collectionBag), 24),
                "Expected ClassCastException due to missing comparator for Predicate in TreeBag.");
    }

    /**
     * Tests the serialization compatibility of an empty {@link CollectionBag}.
     * <p>
     * This test reads a serialized version of an empty CollectionBag from disk
     * and compares it to a newly created empty CollectionBag to ensure that the
     * serialized form has been preserved.
     */
    @Test
    void testEmptyBagCompatibility() throws IOException, ClassNotFoundException {
        // test to make sure the canonical form has been preserved
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(bag));
            assertTrue(deserializedBag.isEmpty(), "Deserialized Bag should be empty");
            assertEquals(bag, deserializedBag, "Deserialized Bag should be equal to the original.");
        }
    }

    /**
     * Tests the serialization compatibility of a full {@link CollectionBag}.
     * <p>
     * This test reads a serialized version of a full CollectionBag from disk
     * and compares it to a newly created full CollectionBag to ensure that the
     * serialized form has been preserved.
     */
    @Test
    void testFullBagCompatibility() throws IOException, ClassNotFoundException {
        // test to make sure the canonical form has been preserved
        final Bag<T> fullBag = (Bag<T>) makeFullCollection();
        if (fullBag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(fullBag));
            assertEquals(fullBag.size(), deserializedBag.size(), "Deserialized Bag should have the same size as the original.");
            assertEquals(fullBag, deserializedBag, "Deserialized Bag should be equal to the original.");
        }
    }
}