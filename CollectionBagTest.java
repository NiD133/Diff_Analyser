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
 * Note: This test is mainly for serialization support, as the CollectionBag decorator
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

    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

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

    /**
     * Tests that adding a Predicate to a TreeBag with a custom Comparator
     * does not throw an exception. The custom Comparator (based on toString)
     * ensures the Predicate can be properly compared and added.
     */
    @Test
    void testAddWithCustomComparatorSucceeds() {
        // Create a TreeBag with a custom Comparator that uses Predicate's toString
        TreeBag<Predicate<Object>> treeBag = new TreeBag<>(Comparator.comparing(Predicate::toString));
        CollectionBag<Predicate<Object>> bag = new CollectionBag<>(treeBag);

        // Add the NonePredicate - should succeed due to custom Comparator
        bag.add(NonePredicate.nonePredicate(bag), 24);
    }

    /**
     * Tests that adding a Predicate to a TreeBag without a custom Comparator
     * throws ClassCastException. The default Comparator relies on natural ordering,
     * which Predicate does not implement.
     */
    @Test
    void testAddWithoutCustomComparatorThrowsException() {
        // Create a TreeBag with natural ordering (requires Comparable elements)
        TreeBag<Predicate<Object>> treeBag = new TreeBag<>();
        CollectionBag<Predicate<Object>> bag = new CollectionBag<>(treeBag);

        // Attempt to add NonePredicate - should fail with ClassCastException
        assertThrows(ClassCastException.class, () -> 
            bag.add(NonePredicate.nonePredicate(bag), 24)
        );
    }

    /**
     * Tests compatibility of the serialized form of an empty CollectionBag
     * against a canonical version stored in SCM. Ensures serialization
     * preserves the empty state and structure of the bag.
     */
    @Test
    void testEmptyBagCompatibility() throws IOException, ClassNotFoundException {
        Bag<T> emptyBag = makeObject();
        
        // Skip test if conditions not met (non-serializable, etc.)
        if (!(emptyBag instanceof Serializable) || skipSerializedCanonicalTests() || !isTestSerialization()) {
            return;
        }
        
        // Deserialize the canonical empty bag from disk
        Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(emptyBag));
        
        // Verify deserialized bag is empty and structurally equal
        assertTrue(deserializedBag.isEmpty(), "Deserialized bag should be empty");
        assertEquals(emptyBag, deserializedBag, "Deserialized bag should match original empty bag");
    }

    /**
     * Tests compatibility of the serialized form of a full CollectionBag
     * against a canonical version stored in SCM. Ensures serialization
     * preserves the size and structure of the bag.
     */
    @Test
    void testFullBagCompatibility() throws IOException, ClassNotFoundException {
        Bag<T> fullBag = (Bag<T>) makeFullCollection();
        
        // Skip test if conditions not met (non-serializable, etc.)
        if (!(fullBag instanceof Serializable) || skipSerializedCanonicalTests() || !isTestSerialization()) {
            return;
        }
        
        // Deserialize the canonical full bag from disk
        Bag<?> deserializedBag = (Bag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(fullBag));
        
        // Verify deserialized bag has the correct size and structure
        assertEquals(fullBag.size(), deserializedBag.size(), "Deserialized bag size should match original");
        assertEquals(fullBag, deserializedBag, "Deserialized bag should match original full bag");
    }
}