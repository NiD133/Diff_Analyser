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
 * Note: This test focuses primarily on serialization and specific edge cases of CollectionBag.
 * Extensive general Bag functionality is covered in AbstractBagTest.
 */
public class CollectionBagTest<T> extends AbstractCollectionTest<T> {

    private CollectionBag<Predicate<Object>> collectionBagOfPredicateOfObject;
    private TreeBag<Predicate<Object>> treeBagOfPredicateOfObject;

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    protected int getIterationBehaviour() {
        return UNORDERED;
    }

    /**
     * Returns an empty List for use in modification testing.
     *
     * @return a confirmed empty collection
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Returns a full Set for use in modification testing.
     *
     * @return a confirmed full collection
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> set = makeConfirmedCollection();
        set.addAll(Arrays.asList(getFullElements()));
        return set;
    }

    @Override
    public Bag<T> makeObject() {
        return CollectionBag.collectionBag(new HashBag<>());
    }

    @BeforeEach
    public void setup() {
        treeBagOfPredicateOfObject = new TreeBag<>();
        collectionBagOfPredicateOfObject = new CollectionBag<>(treeBagOfPredicateOfObject);
    }

    @Test
    void testAdd_Predicate_ComparatorCustom() {
        // Given
        treeBagOfPredicateOfObject = new TreeBag<>(Comparator.comparing(Predicate::toString));
        collectionBagOfPredicateOfObject = new CollectionBag<>(treeBagOfPredicateOfObject);
        final Predicate<Object> predicate = NonePredicate.nonePredicate(collectionBagOfPredicateOfObject);

        // When
        collectionBagOfPredicateOfObject.add(predicate, 24);

        // Then: No exception is thrown.  The bag allows Predicates because a custom comparator is provided.
        assertEquals(24, collectionBagOfPredicateOfObject.getCount(predicate));
    }

    @Test
    void testAdd_Predicate_ComparatorDefault() {
        // Given:  A TreeBag without a custom comparator for Predicates.
        final Predicate<Object> predicate = NonePredicate.nonePredicate(collectionBagOfPredicateOfObject);

        // When/Then:  Adding a Predicate without a comparator causes a ClassCastException because TreeBag needs to compare the elements.
        assertThrows(ClassCastException.class, () -> collectionBagOfPredicateOfObject.add(predicate, 24),
                "Expected ClassCastException when adding Predicate to TreeBag without a comparator.");
    }

    /**
     * Compares the current serialized form of an empty Bag
     * against the canonical version in SCM.  This verifies that the serialized
     * form of the class has not changed in a way that breaks backwards compatibility.
     */
    @Test
    void testEmptyBagCompatibility() throws IOException, ClassNotFoundException {
        // Given
        final Bag<T> bag = makeObject();

        // When/Then
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final Bag<?> bag2 = (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(bag));
            assertTrue(bag2.isEmpty(), "Bag should be empty after deserialization.");
            assertEquals(bag, bag2, "Deserialized bag should be equal to the original.");
        }
    }

    /**
     * Compares the current serialized form of a full Bag
     * against the canonical version in SCM. This verifies that the serialized
     * form of the class has not changed in a way that breaks backwards compatibility.
     */
    @Test
    void testFullBagCompatibility() throws IOException, ClassNotFoundException {
        // Given
        final Bag<T> bag = (Bag<T>) makeFullCollection();

        // When/Then
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            final Bag<?> bag2 = (Bag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(bag));
            assertEquals(bag.size(), bag2.size(), "Deserialized bag should have the same size as the original.");
            assertEquals(bag, bag2, "Deserialized bag should be equal to the original.");
        }
    }
}