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
 * Unless required by applicable law in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections4.collection;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractCollectionTest} for exercising the
 * {@link IndexedCollection} implementation.
 */
@SuppressWarnings("boxing")
class IndexedCollectionTest extends AbstractCollectionTest<String> {

    private static final class IntegerTransformer implements Transformer<String, Integer>, Serializable {
        private static final long serialVersionUID = 809439581555072949L;

        @Override
        public Integer transform(final String input) {
            return Integer.valueOf(input);
        }
    }

    // --- Test Framework Overrides ---

    @Override
    public String[] getFullElements() {
        return new String[] { "1", "3", "5", "7", "2", "4", "6" };
    }

    @Override
    public String[] getOtherElements() {
        return new String[] {"9", "88", "678", "87", "98", "78", "99"};
    }

    @Override
    public Collection<String> makeObject() {
        return decorateCollection(new ArrayList<>());
    }

    @Override
    public Collection<String> makeFullCollection() {
        return decorateCollection(new ArrayList<>(Arrays.asList(getFullElements())));
    }

    @Override
    public Collection<String> makeConfirmedCollection() {
        return new ArrayList<>();
    }

    @Override
    public Collection<String> makeConfirmedFullCollection() {
        return new ArrayList<>(Arrays.asList(getFullElements()));
    }

    @Override
    protected boolean skipSerializedCanonicalTests() {
        // FIXME: support canonical tests
        return true;
    }

    // --- Helper Methods ---

    /**
     * Factory method for creating a non-unique indexed collection.
     */
    protected Collection<String> decorateCollection(final Collection<String> collection) {
        return IndexedCollection.nonUniqueIndexedCollection(collection, new IntegerTransformer());
    }

    /**
     * Factory method for creating a unique indexed collection.
     */
    protected IndexedCollection<Integer, String> decorateUniqueCollection(final Collection<String> collection) {
        return IndexedCollection.uniqueIndexedCollection(collection, new IntegerTransformer());
    }

    /**
     * Creates an empty non-unique indexed collection for testing.
     */
    private IndexedCollection<Integer, String> makeEmptyNonUniqueIndexedCollection() {
        return (IndexedCollection<Integer, String>) makeObject();
    }

    /**
     * Creates an empty unique indexed collection for testing.
     */
    private IndexedCollection<Integer, String> makeEmptyUniqueIndexedCollection() {
        return decorateUniqueCollection(new ArrayList<>());
    }

    // --- Tests for IndexedCollection specific features ---

    @Test
    @DisplayName("get() should retrieve elements by key after they are added")
    void getShouldRetrieveAddedElements() {
        // Arrange
        final IndexedCollection<Integer, String> indexedCollection = makeEmptyNonUniqueIndexedCollection();

        // Act
        indexedCollection.add("12");
        indexedCollection.add("16");
        indexedCollection.add("1");
        indexedCollection.addAll(asList("2", "3", "4"));

        // Assert
        assertEquals("12", indexedCollection.get(12));
        assertEquals("16", indexedCollection.get(16));
        assertEquals("1", indexedCollection.get(1));
        assertEquals("2", indexedCollection.get(2));
        assertEquals("3", indexedCollection.get(3));
        assertEquals("4", indexedCollection.get(4));
    }

    @Test
    @DisplayName("Constructor should correctly index elements of a pre-populated collection")
    void constructorShouldIndexExistingElements() {
        // Arrange
        final List<String> initialElements = Arrays.asList("1", "2", "3");
        final Collection<String> underlyingCollection = new ArrayList<>(initialElements);

        // Act
        final IndexedCollection<Integer, String> indexedCollection = decorateUniqueCollection(underlyingCollection);

        // Assert
        assertEquals("1", indexedCollection.get(1));
        assertEquals("2", indexedCollection.get(2));
        assertEquals("3", indexedCollection.get(3));
    }

    @Test
    @DisplayName("add() should throw IllegalArgumentException for a duplicate key in a unique index")
    void addShouldThrowExceptionForDuplicateKeyInUniqueIndex() {
        // Arrange
        final Collection<String> uniqueIndexedCollection = makeEmptyUniqueIndexedCollection();
        uniqueIndexedCollection.add("1"); // This adds the element with key 1

        // Act & Assert
        // Adding another element that maps to the same key should fail.
        assertThrows(IllegalArgumentException.class, () -> uniqueIndexedCollection.add("1"));
    }

    @Test
    @DisplayName("reindex() should update the index after the decorated collection is modified externally")
    void reindexShouldUpdateIndexAfterExternalModification() {
        // Arrange
        final Collection<String> underlyingCollection = new ArrayList<>();
        final IndexedCollection<Integer, String> indexedCollection = decorateUniqueCollection(underlyingCollection);

        // Act: Modify the underlying collection directly, bypassing the decorator
        underlyingCollection.add("1");
        underlyingCollection.add("2");
        underlyingCollection.add("3");

        // Assert: The index is out of sync before reindexing
        assertNull(indexedCollection.get(1));
        assertNull(indexedCollection.get(2));
        assertNull(indexedCollection.get(3));

        // Act: Manually trigger a reindex
        indexedCollection.reindex();

        // Assert: The index is now synchronized with the underlying collection
        assertEquals("1", indexedCollection.get(1));
        assertEquals("2", indexedCollection.get(2));
        assertEquals("3", indexedCollection.get(3));
    }
}