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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test class for {@link CollectionBag}.
 * <p>
 * Note: This test is mainly for serialization support, the CollectionBag decorator
 * is extensively used and tested in AbstractBagTest.
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

    @Test
    @DisplayName("add() should succeed for a non-comparable element when the decorated bag uses a custom comparator")
    void add_shouldSucceed_forNonComparableElementWithCustomComparator() {
        // Arrange
        // A TreeBag requires elements to be Comparable or have a Comparator. Predicate is not Comparable.
        final TreeBag<Predicate<Object>> decoratedBag = new TreeBag<>(Comparator.comparing(Object::toString));
        final Bag<Predicate<Object>> collectionBag = CollectionBag.collectionBag(decoratedBag);
        final Predicate<Object> predicate = NonePredicate.nonePredicate(collectionBag);
        final int copiesToAdd = 24;

        // Act
        collectionBag.add(predicate, copiesToAdd);

        // Assert
        assertEquals(copiesToAdd, collectionBag.getCount(predicate),
                "The bag should contain the correct number of copies of the added element.");
    }

    @Test
    @DisplayName("add() should throw ClassCastException for a non-comparable element when the decorated bag uses natural ordering")
    void add_shouldThrowException_forNonComparableElementWithDefaultComparator() {
        // Arrange
        // A TreeBag without a comparator uses natural ordering, which will fail for Predicate.
        final TreeBag<Predicate<Object>> decoratedBag = new TreeBag<>();
        final Bag<Predicate<Object>> collectionBag = CollectionBag.collectionBag(decoratedBag);
        final Predicate<Object> predicate = NonePredicate.nonePredicate(collectionBag);

        // Act & Assert
        assertThrows(ClassCastException.class, () -> collectionBag.add(predicate, 24),
                "Should not be able to add a non-comparable element to a TreeBag without a suitable comparator.");
    }

    @Test
    @DisplayName("Serialization of an empty bag should be compatible with the canonical form")
    void testEmptyBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        // Arrange
        final Bag<T> bag = makeObject();

        // This test is only performed on platforms that support serialization
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            // Act
            final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(bag));

            // Assert
            assertTrue(deserializedBag.isEmpty(), "Deserialized bag should be empty");
            assertEquals(bag, deserializedBag, "Deserialized bag should equal a new empty bag");
        }
    }

    @Test
    @DisplayName("Serialization of a full bag should be compatible with the canonical form")
    void testFullBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        // Arrange
        final Bag<T> bag = makeFullCollection();

        // This test is only performed on platforms that support serialization
        if (bag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            // Act
            final Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(bag));

            // Assert
            assertEquals(bag.size(), deserializedBag.size(), "Deserialized bag should have the same size");
            assertEquals(bag, deserializedBag, "Deserialized bag should equal a new full bag");
        }
    }
}