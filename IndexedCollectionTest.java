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

@SuppressWarnings("boxing")
class IndexedCollectionTest extends AbstractCollectionTest<String> {

    private static final class StringToIntegerTransformer implements Transformer<String, Integer>, Serializable {
        private static final long serialVersionUID = 809439581555072949L;

        @Override
        public Integer transform(final String input) {
            return Integer.valueOf(input);
        }
    }

    @Override
    protected Collection<String> decorateCollection(final Collection<String> collection) {
        return IndexedCollection.nonUniqueIndexedCollection(collection, new StringToIntegerTransformer());
    }

    protected IndexedCollection<Integer, String> decorateUniqueCollection(final Collection<String> collection) {
        return IndexedCollection.uniqueIndexedCollection(collection, new StringToIntegerTransformer());
    }

    @Override
    public String[] getFullElements() {
        return new String[] { "1", "3", "5", "7", "2", "4", "6" };
    }

    @Override
    public String[] getOtherElements() {
        return new String[] {"9", "88", "678", "87", "98", "78", "99"};
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

    private Collection<String> makeNonUniqueIndexedCollection() {
        return decorateCollection(new ArrayList<>());
    }

    private Collection<String> makeUniqueIndexedCollection() {
        return decorateUniqueCollection(new ArrayList<>());
    }

    @Override
    protected boolean skipSerializedCanonicalTests() {
        // FIXME: support canonical tests
        return true;
    }

    @Test
    void testRetrieveElementsByKeyAfterAdding() {
        // Arrange: Create non-unique indexed collection
        final Collection<String> coll = makeNonUniqueIndexedCollection();
        
        // Act: Add elements individually and as a group
        coll.add("12");
        coll.add("16");
        coll.add("1");
        coll.addAll(asList("2", "3", "4"));

        // Assert: Verify elements can be retrieved by their integer keys
        final IndexedCollection<Integer, String> indexed = (IndexedCollection<Integer, String>) coll;
        assertEquals("12", indexed.get(12), "Element with key=12");
        assertEquals("16", indexed.get(16), "Element with key=16");
        assertEquals("1", indexed.get(1), "Element with key=1");
        assertEquals("2", indexed.get(2), "Element with key=2");
        assertEquals("3", indexed.get(3), "Element with key=3");
        assertEquals("4", indexed.get(4), "Element with key=4");
    }

    @Test
    void testPreBuiltIndexOnUniqueCollectionCreation() {
        // Arrange: Create full collection and decorate as unique indexed
        final Collection<String> original = makeFullCollection();
        
        // Act: Create unique indexed collection
        final IndexedCollection<Integer, String> indexed = decorateUniqueCollection(original);

        // Assert: Verify index contains expected elements
        assertEquals("1", indexed.get(1), "Element with key=1");
        assertEquals("2", indexed.get(2), "Element with key=2");
        assertEquals("3", indexed.get(3), "Element with key=3");
    }

    @Test
    void testUniqueIndexRejectsDuplicateKeys() {
        // Arrange: Create unique indexed collection
        final Collection<String> uniqueColl = makeUniqueIndexedCollection();
        uniqueColl.add("1");

        // Act & Assert: Verify duplicate key throws exception
        assertThrows(IllegalArgumentException.class, 
            () -> uniqueColl.add("1"),
            "Adding duplicate key should throw IllegalArgumentException"
        );
    }

    @Test
    void testReindexSynchronizesAfterExternalModifications() {
        // Arrange: Create backing collection and unique index
        final Collection<String> backingCollection = new ArrayList<>();
        final IndexedCollection<Integer, String> indexedColl = decorateUniqueCollection(backingCollection);

        // Act: Modify backing collection directly
        backingCollection.add("1");
        backingCollection.add("2");
        backingCollection.add("3");

        // Assert: Index is out-of-sync initially
        assertNull(indexedColl.get(1), "Index should be out-of-sync for key=1");
        assertNull(indexedColl.get(2), "Index should be out-of-sync for key=2");
        assertNull(indexedColl.get(3), "Index should be out-of-sync for key=3");

        // Act: Rebuild index
        indexedColl.reindex();

        // Assert: Index now reflects current state
        assertEquals("1", indexedColl.get(1), "Element with key=1 after reindex");
        assertEquals("2", indexedColl.get(2), "Element with key=2 after reindex");
        assertEquals("3", indexedColl.get(3), "Element with key=3 after reindex");
    }
}