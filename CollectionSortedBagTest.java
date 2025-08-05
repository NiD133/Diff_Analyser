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
 * Test class for {@link CollectionSortedBag}.
 * <p>
 * This test focuses primarily on serialization compatibility. The CollectionSortedBag decorator
 * functionality is extensively tested in AbstractSortedBagTest.
 */
public class CollectionSortedBagTest<T> extends AbstractCollectionTest<T> {

    // Test data constants
    private static final int TEST_ELEMENT_COUNT = 30;
    private static final String COMPATIBILITY_VERSION = "4";
    
    @Override
    public String getCompatibilityVersion() {
        return COMPATIBILITY_VERSION;
    }

    /**
     * Creates test elements that are comparable (Integer objects).
     * Elements are odd numbers: 1, 3, 5, 7, ..., 59
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getFullNonNullElements() {
        final Object[] elements = new Object[TEST_ELEMENT_COUNT];

        for (int i = 0; i < TEST_ELEMENT_COUNT; i++) {
            // Generate odd numbers: 2*i + 1
            elements[i] = Integer.valueOf(2 * i + 1);
        }
        return (T[]) elements;
    }

    /**
     * Creates alternative test elements that are comparable (Integer objects).
     * Elements are even numbers: 2, 4, 6, 8, ..., 60
     */
    @Override
    @SuppressWarnings("unchecked")
    public T[] getOtherNonNullElements() {
        final Object[] elements = new Object[TEST_ELEMENT_COUNT];
        
        for (int i = 0; i < TEST_ELEMENT_COUNT; i++) {
            // Generate even numbers: 2*i + 2
            elements[i] = Integer.valueOf(2 * i + 2);
        }
        return (T[]) elements;
    }

    /**
     * SortedBags typically don't allow null elements due to comparison requirements.
     */
    @Override
    public boolean isNullSupported() {
        return false;
    }

    /**
     * Creates an empty ArrayList as the reference collection for testing.
     * This serves as the "expected" collection to compare against.
     */
    @Override
    public Collection<T> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Creates a full ArrayList containing all test elements.
     * This serves as the "expected" full collection to compare against.
     */
    @Override
    public Collection<T> makeConfirmedFullCollection() {
        final Collection<T> confirmedCollection = makeConfirmedCollection();
        confirmedCollection.addAll(Arrays.asList(getFullElements()));
        return confirmedCollection;
    }

    /**
     * Creates the CollectionSortedBag instance under test.
     * Uses TreeBag as the underlying sorted bag implementation.
     */
    @Override
    public Bag<T> makeObject() {
        return CollectionSortedBag.collectionSortedBag(new TreeBag<>());
    }

    /**
     * Tests backward compatibility of empty CollectionSortedBag serialization.
     * Verifies that an empty bag can be deserialized from the canonical form
     * and matches the current implementation.
     */
    @Test
    void testEmptyBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        final Bag<T> currentEmptyBag = makeObject();
        
        // Only test serialization if the bag supports it and serialization testing is enabled
        if (shouldTestSerialization(currentEmptyBag)) {
            final Bag<?> deserializedEmptyBag = deserializeCanonicalEmptyBag(currentEmptyBag);
            
            assertEquals(0, deserializedEmptyBag.size(), 
                "Deserialized empty bag should have size 0");
            assertEquals(currentEmptyBag, deserializedEmptyBag, 
                "Deserialized empty bag should equal current empty bag");
        }
    }

    /**
     * Tests backward compatibility of full CollectionSortedBag serialization.
     * Verifies that a full bag can be deserialized from the canonical form
     * and matches the current implementation.
     */
    @Test
    void testFullBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        final SortedBag<T> currentFullBag = (SortedBag<T>) makeFullCollection();
        
        // Only test serialization if the bag supports it and serialization testing is enabled
        if (shouldTestSerialization(currentFullBag)) {
            final SortedBag<?> deserializedFullBag = deserializeCanonicalFullBag(currentFullBag);
            
            assertEquals(currentFullBag.size(), deserializedFullBag.size(), 
                "Deserialized full bag should have same size as current full bag");
            assertEquals(currentFullBag, deserializedFullBag, 
                "Deserialized full bag should equal current full bag");
        }
    }

    // Helper methods for better readability

    /**
     * Determines if serialization testing should be performed for the given bag.
     */
    private boolean shouldTestSerialization(Bag<T> bag) {
        return bag instanceof Serializable 
            && !skipSerializedCanonicalTests() 
            && isTestSerialization();
    }

    /**
     * Deserializes the canonical empty bag from disk.
     */
    private Bag<?> deserializeCanonicalEmptyBag(Bag<T> bag) throws IOException, ClassNotFoundException {
        return (Bag<?>) readExternalFormFromDisk(getCanonicalEmptyCollectionName(bag));
    }

    /**
     * Deserializes the canonical full bag from disk.
     */
    private SortedBag<?> deserializeCanonicalFullBag(SortedBag<T> bag) throws IOException, ClassNotFoundException {
        return (SortedBag<?>) readExternalFormFromDisk(getCanonicalFullCollectionName(bag));
    }

    // Commented out utility method for generating canonical serialized forms
    // This would be used during development to create the reference serialized objects
    /*
    void generateCanonicalSerializedForms() throws Exception {
        resetEmpty();
        writeExternalFormToDisk((Serializable) getCollection(), 
            "src/test/resources/data/test/CollectionSortedBag.emptyCollection.version4.obj");
        resetFull();
        writeExternalFormToDisk((Serializable) getCollection(), 
            "src/test/resources/data/test/CollectionSortedBag.fullCollection.version4.obj");
    }
    */
}