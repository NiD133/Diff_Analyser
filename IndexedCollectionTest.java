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
package org.apache.commons.collections4.collection;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link IndexedCollection} implementation.
 * 
 * IndexedCollection provides a Map-like view onto a Collection where elements
 * can be retrieved by a key derived from the element using a transformer.
 */
@SuppressWarnings("boxing")
class IndexedCollectionTest extends AbstractCollectionTest<String> {

    /**
     * Transformer that converts String elements to Integer keys.
     * For example: "123" -> 123
     */
    private static final class StringToIntegerTransformer implements Transformer<String, Integer>, Serializable {
        private static final long serialVersionUID = 809439581555072949L;

        @Override
        public Integer transform(final String input) {
            return Integer.valueOf(input);
        }
    }

    // Test data constants for better maintainability
    private static final String[] SAMPLE_ELEMENTS = {"1", "3", "5", "7", "2", "4", "6"};
    private static final String[] OTHER_ELEMENTS = {"9", "88", "678", "87", "98", "78", "99"};

    /**
     * Creates an IndexedCollection that allows duplicate keys.
     */
    protected Collection<String> decorateCollection(final Collection<String> collection) {
        return IndexedCollection.nonUniqueIndexedCollection(collection, new StringToIntegerTransformer());
    }

    /**
     * Creates an IndexedCollection that enforces unique keys.
     */
    protected IndexedCollection<Integer, String> decorateUniqueCollection(final Collection<String> collection) {
        return IndexedCollection.uniqueIndexedCollection(collection, new StringToIntegerTransformer());
    }

    @Override
    public String[] getFullElements() {
        return SAMPLE_ELEMENTS.clone();
    }

    @Override
    public String[] getOtherElements() {
        return OTHER_ELEMENTS.clone();
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
    public Collection<String> makeFullCollection() {
        return decorateCollection(new ArrayList<>(Arrays.asList(getFullElements())));
    }

    @Override
    public Collection<String> makeObject() {
        return decorateCollection(new ArrayList<>());
    }

    public Collection<String> makeTestCollection() {
        return decorateCollection(new ArrayList<>());
    }

    public Collection<String> makeUniqueTestCollection() {
        return decorateUniqueCollection(new ArrayList<>());
    }

    @Override
    protected boolean skipSerializedCanonicalTests() {
        // FIXME: support canonical tests
        return true;
    }

    @Test
    void shouldRetrieveElementsByTransformedKey_WhenElementsAreAdded() throws Exception {
        // Given: An empty indexed collection
        final Collection<String> indexedCollection = makeTestCollection();
        
        // When: Adding elements individually and in batch
        indexedCollection.add("12");
        indexedCollection.add("16");
        indexedCollection.add("1");
        indexedCollection.addAll(asList("2", "3", "4"));

        // Then: Elements should be retrievable by their integer key (transformed from string value)
        @SuppressWarnings("unchecked")
        final IndexedCollection<Integer, String> indexed = (IndexedCollection<Integer, String>) indexedCollection;
        
        assertEquals("12", indexed.get(12), "Element '12' should be retrievable by key 12");
        assertEquals("16", indexed.get(16), "Element '16' should be retrievable by key 16");
        assertEquals("1", indexed.get(1), "Element '1' should be retrievable by key 1");
        assertEquals("2", indexed.get(2), "Element '2' should be retrievable by key 2");
        assertEquals("3", indexed.get(3), "Element '3' should be retrievable by key 3");
        assertEquals("4", indexed.get(4), "Element '4' should be retrievable by key 4");
    }

    @Test
    void shouldIndexExistingElements_WhenCollectionIsDecorated() throws Exception {
        // Given: A collection with existing elements
        final Collection<String> originalCollection = makeFullCollection();
        
        // When: Decorating it as an indexed collection
        final IndexedCollection<Integer, String> indexedCollection = decorateUniqueCollection(originalCollection);

        // Then: Existing elements should be immediately accessible by their keys
        assertEquals("1", indexedCollection.get(1), "Pre-existing element '1' should be indexed");
        assertEquals("2", indexedCollection.get(2), "Pre-existing element '2' should be indexed");
        assertEquals("3", indexedCollection.get(3), "Pre-existing element '3' should be indexed");
    }

    @Test
    void shouldThrowException_WhenDuplicateKeyIsAddedToUniqueIndex() throws Exception {
        // Given: A unique indexed collection with one element
        final Collection<String> uniqueCollection = makeUniqueTestCollection();
        uniqueCollection.add("1");

        // When & Then: Adding another element with the same key should throw IllegalArgumentException
        assertThrows(IllegalArgumentException.class, 
                    () -> uniqueCollection.add("1"),
                    "Adding duplicate key to unique index should throw IllegalArgumentException");
    }

    @Test
    void shouldUpdateIndex_WhenReindexIsCalledAfterDirectModificationOfUnderlyingCollection() throws Exception {
        // Given: An indexed collection wrapping an original collection
        final Collection<String> originalCollection = new ArrayList<>();
        final IndexedCollection<Integer, String> indexedCollection = decorateUniqueCollection(originalCollection);

        // When: Modifying the original collection directly (bypassing the indexed wrapper)
        originalCollection.add("1");
        originalCollection.add("2");
        originalCollection.add("3");

        // Then: Elements should not be accessible via index until reindex is called
        assertNull(indexedCollection.get(1), "Element should not be indexed before reindex()");
        assertNull(indexedCollection.get(2), "Element should not be indexed before reindex()");
        assertNull(indexedCollection.get(3), "Element should not be indexed before reindex()");

        // When: Calling reindex to synchronize the index with the underlying collection
        indexedCollection.reindex();

        // Then: Elements should now be accessible via their keys
        assertEquals("1", indexedCollection.get(1), "Element '1' should be indexed after reindex()");
        assertEquals("2", indexedCollection.get(2), "Element '2' should be indexed after reindex()");
        assertEquals("3", indexedCollection.get(3), "Element '3' should be indexed after reindex()");
    }
}