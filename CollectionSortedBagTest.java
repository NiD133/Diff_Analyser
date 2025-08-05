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

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.IntStream;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.collection.AbstractCollectionTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Verifies the serialization compatibility of {@link CollectionSortedBag}.
 * <p>
 * The core bag functionality is tested in {@link AbstractSortedBagTest}. This class
 * primarily ensures that the serialization format remains consistent with canonical
 * versions stored on disk.
 * </p>
 */
@DisplayName("CollectionSortedBag Serialization")
public class CollectionSortedBagTest extends AbstractCollectionTest<Integer> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    /**
     * Overridden to return an array of comparable {@link Integer} objects.
     */
    @Override
    public Integer[] getFullNonNullElements() {
        return IntStream.range(0, 30)
                .map(i -> i * 2 + 1)
                .boxed()
                .toArray(Integer[]::new);
    }

    /**
     * Overridden to return an array of other comparable {@link Integer} objects.
     */
    @Override
    public Integer[] getOtherNonNullElements() {
        return IntStream.range(0, 30)
                .map(i -> i * 2 + 2)
                .boxed()
                .toArray(Integer[]::new);
    }

    /**
     * Overridden because SortedBags don't allow null elements.
     * @return false
     */
    @Override
    public boolean isNullSupported() {
        return false;
    }

    /**
     * Returns an empty {@link ArrayList} for use in modification testing.
     *
     * @return a confirmed empty collection.
     */
    @Override
    public Collection<Integer> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    /**
     * Returns a full {@link ArrayList} for use in modification testing.
     *
     * @return a confirmed full collection.
     */
    @Override
    public Collection<Integer> makeConfirmedFullCollection() {
        final Collection<Integer> collection = makeConfirmedCollection();
        Collections.addAll(collection, getFullElements());
        return collection;
    }

    @Override
    public Bag<Integer> makeObject() {
        return CollectionSortedBag.collectionSortedBag(new TreeBag<>());
    }

//    void testCreate() throws Exception {
//        resetEmpty();
//        writeExternalFormToDisk((java.io.Serializable) getCollection(), "src/test/resources/data/test/CollectionSortedBag.emptyCollection.version4.obj");
//        resetFull();
//        writeExternalFormToDisk((java.io.Serializable) getCollection(), "src/test/resources/data/test/CollectionSortedBag.fullCollection.version4.obj");
//    }

    @Test
    @DisplayName("An empty bag should match its canonical serialized form")
    void testEmptyBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        // Arrange
        final Bag<Integer> bag = makeObject();
        final String canonicalFileName = getCanonicalEmptyCollectionName(bag);

        // Act & Assert
        assertSerializationIsCompatible(bag, canonicalFileName);
    }

    @Test
    @DisplayName("A full bag should match its canonical serialized form")
    void testFullBagSerializationCompatibility() throws IOException, ClassNotFoundException {
        // Arrange
        final Bag<Integer> bag = makeFullCollection();
        final String canonicalFileName = getCanonicalFullCollectionName(bag);

        // Act & Assert
        assertSerializationIsCompatible(bag, canonicalFileName);
    }

    /**
     * Asserts that a given bag, when deserialized from a canonical file,
     * is equal to the original bag.
     *
     * @param originalBag The bag to check for compatibility.
     * @param canonicalFileName The path to the canonical serialized file.
     */
    private void assertSerializationIsCompatible(final Bag<Integer> originalBag, final String canonicalFileName)
            throws IOException, ClassNotFoundException {
        // These checks from the base class ensure we only run serialization tests
        // when they are enabled and the object is actually serializable.
        if (originalBag instanceof Serializable && !skipSerializedCanonicalTests() && isTestSerialization()) {
            // Act: Deserialize the canonical form from a file.
            @SuppressWarnings("unchecked")
            final Bag<Integer> deserializedBag = (Bag<Integer>) readExternalFormFromDisk(canonicalFileName);

            // Assert: The deserialized bag should be identical to the original.
            assertAll("Deserialized bag should be equal to the original",
                () -> assertEquals(originalBag.size(), deserializedBag.size(), "should have the same size"),
                () -> assertEquals(originalBag, deserializedBag, "should be equal based on the equals() method")
            );
        }
    }
}