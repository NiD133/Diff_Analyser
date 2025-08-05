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
 * Note: This test focuses primarily on serialization support, as the CollectionBag decorator
 * is extensively tested through AbstractBagTest.
 */
public class CollectionBagTest<T> extends AbstractCollectionTest<T> {

    // Constants for test configuration
    private static final String COMPATIBILITY_VERSION = "4";
    private static final int PREDICATE_COUNT = 24;

    @Override
    public String getCompatibilityVersion() {
        return COMPATIBILITY_VERSION;
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
     * Creates a full ArrayList populated with test elements for use in modification testing.
     *
     * @return a confirmed full collection containing all test elements
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> collection = makeConfirmedCollection();
        collection.addAll(Arrays.asList(getFullElements()));
        return collection;
    }

    /**
     * Creates a new CollectionBag instance wrapping a HashBag for testing.
     *
     * @return a new CollectionBag instance
     */
    @Override
    public Bag<T> makeObject() {
        return CollectionBag.collectionBag(new HashBag<>());
    }

    /**
     * Tests that adding predicates to a CollectionBag works correctly when the underlying
     * TreeBag has a custom comparator that can handle Predicate objects.
     * 
     * This test verifies that CollectionBag properly delegates to the underlying bag
     * when adding elements with a count.
     */
    @Test
    void testAddPredicateWithCustomComparator() throws Throwable {
        // Given: A TreeBag with a custom comparator that can compare Predicate objects
        final Comparator<Predicate<Object>> predicateComparator = Comparator.comparing(Predicate::toString);
        final TreeBag<Predicate<Object>> treeBag = new TreeBag<>(predicateComparator);
        final CollectionBag<Predicate<Object>> collectionBag = new CollectionBag<>(treeBag);
        
        // When: Adding a predicate with a specific count
        final Predicate<Object> testPredicate = NonePredicate.nonePredicate(collectionBag);
        
        // Then: The operation should complete successfully without throwing an exception
        collectionBag.add(testPredicate, PREDICATE_COUNT);
    }

    /**
     * Tests that adding predicates to a CollectionBag fails when the underlying TreeBag
     * uses the default comparator, which cannot compare Predicate objects.
     * 
     * This test verifies that CollectionBag properly propagates ClassCastException
     * from the underlying bag when incompatible types are added.
     */
    @Test
    void testAddPredicateWithDefaultComparatorThrowsException() throws Throwable {
        // Given: A TreeBag with default comparator (cannot compare Predicate objects)
        final TreeBag<Predicate<Object>> treeBag = new TreeBag<>();
        final CollectionBag<Predicate<Object>> collectionBag = new CollectionBag<>(treeBag);
        
        // When/Then: Adding a predicate should throw ClassCastException
        final Predicate<Object> testPredicate = NonePredicate.nonePredicate(collectionBag);
        assertThrows(ClassCastException.class, 
            () -> collectionBag.add(testPredicate, PREDICATE_COUNT),
            "Adding predicates to TreeBag with default comparator should throw ClassCastException");
    }

    /**
     * Tests that an empty CollectionBag can be properly serialized and deserialized,
     * maintaining compatibility with the canonical serialized form.
     * 
     * This ensures backward compatibility when deserializing CollectionBag instances
     * that were serialized with previous versions of the library.
     */
    @Test
    void testEmptyBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        // Given: An empty CollectionBag
        final Bag<T> emptyBag = makeObject();
        
        // When: The bag is serializable and serialization testing is enabled
        if (isSerializableAndTestingEnabled(emptyBag)) {
            // Then: It should be compatible with the canonical serialized form
            final Bag<?> deserializedBag = deserializeCanonicalEmptyBag(emptyBag);
            
            assertTrue(deserializedBag.isEmpty(), "Deserialized bag should be empty");
            assertEquals(emptyBag, deserializedBag, "Deserialized bag should equal original empty bag");
        }
    }

    /**
     * Tests that a full CollectionBag can be properly serialized and deserialized,
     * maintaining compatibility with the canonical serialized form.
     * 
     * This ensures backward compatibility when deserializing CollectionBag instances
     * that were serialized with previous versions of the library.
     */
    @Test
    void testFullBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        // Given: A full CollectionBag containing test elements
        final Bag<T> fullBag = (Bag<T>) makeFullCollection();
        
        // When: The bag is serializable and serialization testing is enabled
        if (isSerializableAndTestingEnabled(fullBag)) {
            // Then: It should be compatible with the canonical serialized form
            final Bag<?> deserializedBag = deserializeCanonicalFullBag(fullBag);
            
            assertEquals(fullBag.size(), deserializedBag.size(), 
                "Deserialized bag should have the same size as original");
            assertEquals(fullBag, deserializedBag, 
                "Deserialized bag should equal original full bag");
        }
    }

    // Helper methods to improve readability and reduce duplication

    /**
     * Checks if the bag is serializable and serialization testing is enabled.
     */
    private boolean isSerializableAndTestingEnabled(final Bag<T> bag) {
        return bag instanceof Serializable && 
               !skipSerializedCanonicalTests() && 
               isTestSerialization();
    }

    /**
     * Deserializes the canonical empty bag form from disk.
     */
    private Bag<?> deserializeCanonicalEmptyBag(final Bag<T> bag) throws IOException, ClassNotFoundException {
        return (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(bag));
    }

    /**
     * Deserializes the canonical full bag form from disk.
     */
    private Bag<?> deserializeCanonicalFullBag(final Bag<T> bag) throws IOException, ClassNotFoundException {
        return (Bag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(bag));
    }

    // Commented out utility method for generating test data files
    // Uncomment and run when new canonical serialized forms need to be created
    /*
    void generateCanonicalSerializedForms() throws Exception {
        resetEmpty();
        writeExternalFormToDisk((java.io.Serializable) getCollection(), 
            "src/test/resources/data/test/CollectionBag.emptyCollection.version4.obj");
        resetFull();
        writeExternalFormToDisk((java.io.Serializable) getCollection(), 
            "src/test/resources/data/test/CollectionBag.fullCollection.version4.obj");
    }
    */
}