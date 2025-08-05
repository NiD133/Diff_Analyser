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

import org.apache.commons.collections4.Transformer;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link IndexedCollection}.
 */
public class IndexedCollectionTest {

    private List<String> list;
    private final Transformer<String, Integer> keyTransformer = String::length;

    @Before
    public void setUp() {
        list = new ArrayList<>();
        list.add("one");    // length 3
        list.add("two");    // length 3
        list.add("three");  // length 5
    }

    // --- Constructor Tests ---

    @Test(expected = NullPointerException.class)
    public void uniqueIndexedCollection_shouldThrowException_whenCollectionIsNull() {
        IndexedCollection.uniqueIndexedCollection(null, keyTransformer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void uniqueIndexedCollection_shouldThrowException_whenDuplicateKeysExistInInitialCollection() {
        // Arrange: "one" and "two" both have length 3, which is a duplicate key for a unique index.
        List<String> initialListWithDuplicates = Arrays.asList("one", "two");

        // Act & Assert
        IndexedCollection.uniqueIndexedCollection(initialListWithDuplicates, keyTransformer);
    }

    @Test(expected = RuntimeException.class)
    public void factory_shouldPropagateException_whenTransformerThrowsDuringInitialization() {
        // Arrange
        List<String> initialList = Arrays.asList("a");
        Transformer<String, Integer> throwingTransformer = input -> {
            throw new RuntimeException("Transform failed");
        };

        // Act & Assert
        IndexedCollection.uniqueIndexedCollection(initialList, throwingTransformer);
    }

    // --- add() and addAll() Tests ---

    @Test
    public void add_shouldStoreElementInCollectionAndIndex() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);

        // Act
        boolean result = indexedCollection.add("four"); // length 4

        // Assert
        assertTrue(result);
        assertEquals(4, indexedCollection.size());
        assertTrue(indexedCollection.contains("four"));
        assertEquals("four", indexedCollection.get(4)); // Key is length 4
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_shouldThrowException_whenKeyExistsInUniqueIndex() {
        // Arrange
        List<String> uniqueList = new ArrayList<>(Arrays.asList("a", "bb")); // lengths 1, 2
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.uniqueIndexedCollection(uniqueList, keyTransformer);

        // Act: "c" has length 1, which is a duplicate key ("a")
        indexedCollection.add("c");
    }

    @Test
    public void add_shouldAllowDuplicateKey_inNonUniqueIndex() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);

        // Act
        indexedCollection.add("six"); // "six" also has length 3

        // Assert
        Collection<String> valuesForKey3 = indexedCollection.values(3);
        assertEquals(3, valuesForKey3.size());
        assertTrue(valuesForKey3.containsAll(Arrays.asList("one", "two", "six")));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void addAll_shouldThrowException_whenKeyExistsInUniqueIndex() {
        // Arrange
        List<String> uniqueList = new ArrayList<>(Arrays.asList("a", "bb")); // lengths 1, 2
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.uniqueIndexedCollection(uniqueList, keyTransformer);
        
        // Act: "c" has length 1, which is a duplicate key ("a")
        indexedCollection.addAll(Collections.singletonList("c"));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void add_shouldThrowException_whenUnderlyingCollectionIsUnmodifiable() {
        // Arrange
        Collection<String> unmodifiableList = Collections.unmodifiableCollection(list);
        IndexedCollection<Integer, String> indexedCollection =
            IndexedCollection.nonUniqueIndexedCollection(unmodifiableList, keyTransformer);

        // Act
        indexedCollection.add("four");
    }

    // --- get() and values() Tests ---

    @Test
    public void get_shouldReturnObject_forExistingKey() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);

        // Act & Assert
        assertEquals("three", indexedCollection.get(5)); // Key for "three"
    }

    @Test
    public void get_shouldReturnFirstObject_whenMultipleObjectsMatchKeyInNonUniqueIndex() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);

        // Act & Assert: "one" is first in the decorated list with length 3
        assertEquals("one", indexedCollection.get(3));
    }

    @Test
    public void get_shouldReturnNull_forNonExistentKey() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);

        // Act & Assert
        assertNull(indexedCollection.get(99));
    }

    @Test
    public void values_shouldReturnAllObjects_forExistingKeyInNonUniqueIndex() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);

        // Act
        Collection<String> results = indexedCollection.values(3);

        // Assert
        assertNotNull(results);
        assertEquals(2, results.size());
        assertTrue(results.containsAll(Arrays.asList("one", "two")));
    }

    @Test
    public void values_shouldReturnNull_forNonExistentKey() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);

        // Act & Assert
        assertNull(indexedCollection.values(99));
    }

    // --- remove(), removeAll(), removeIf() Tests ---

    @Test
    public void remove_shouldRemoveFromCollectionAndIndex() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);

        // Act
        boolean result = indexedCollection.remove("two");

        // Assert
        assertTrue(result);
        assertEquals(2, indexedCollection.size());
        assertFalse(indexedCollection.contains("two"));

        // Verify the index was updated
        Collection<String> valuesForKey3 = indexedCollection.values(3);
        assertEquals(1, valuesForKey3.size());
        assertEquals("one", valuesForKey3.iterator().next());
    }

    @Test
    public void removeAll_shouldRemoveSpecifiedElementsAndupdateIndex() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);
        Collection<String> toRemove = Arrays.asList("one", "three", "nonexistent");

        // Act
        boolean result = indexedCollection.removeAll(toRemove);

        // Assert
        assertTrue(result);
        assertEquals(1, indexedCollection.size());
        assertTrue(indexedCollection.contains("two"));
        assertFalse(indexedCollection.contains("one"));
        assertFalse(indexedCollection.contains("three"));
        assertNull(indexedCollection.get(5)); // Index for "three" should be gone
    }

    @Test
    public void removeIf_shouldRemoveMatchingElementsAndUpdateIndex() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);

        // Act: Remove all strings with length 3
        boolean result = indexedCollection.removeIf(s -> s.length() == 3);

        // Assert
        assertTrue(result);
        assertEquals(1, indexedCollection.size());
        assertTrue(indexedCollection.contains("three"));
        assertFalse(indexedCollection.contains("one"));
        assertFalse(indexedCollection.contains("two"));
        assertNull(indexedCollection.values(3)); // Index for key 3 should be gone
    }

    // --- clear() Test ---

    @Test
    public void clear_shouldRemoveAllElementsFromCollectionAndIndex() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);
        assertFalse(indexedCollection.isEmpty());

        // Act
        indexedCollection.clear();

        // Assert
        assertTrue(indexedCollection.isEmpty());
        assertNull(indexedCollection.get(3)); // Verify index is cleared
        assertNull(indexedCollection.get(5));
    }

    // --- reindex() Tests ---

    @Test
    public void reindex_shouldUpdateIndex_afterExternalModificationOfDecoratedCollection() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);
        assertEquals("three", indexedCollection.get(5));

        // Act: Modify the underlying list directly, making the index out of sync.
        list.remove("three");

        // Assert: Index is still stale before re-indexing
        assertEquals("three", indexedCollection.get(5));

        // Act: Re-index the collection
        indexedCollection.reindex();

        // Assert: Index is now correct
        assertNull(indexedCollection.get(5));
        assertEquals(2, indexedCollection.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void reindex_shouldThrowException_whenDuplicateKeysFoundForUniqueIndex() {
        // Arrange
        List<String> uniqueList = new ArrayList<>(Arrays.asList("a", "bb", "ccc")); // lengths 1, 2, 3
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.uniqueIndexedCollection(uniqueList, keyTransformer);

        // Act: Modify underlying list to create a duplicate key, then reindex
        uniqueList.add("d"); // length 1, same as "a"
        indexedCollection.reindex();
    }

    // --- contains() and containsAll() Tests ---

    @Test
    public void contains_shouldReturnTrue_whenObjectIsPresent() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);

        // Assert
        assertTrue(indexedCollection.contains("one"));
    }

    @Test
    public void contains_shouldReturnFalse_whenObjectIsNotPresent() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);

        // Assert
        assertFalse(indexedCollection.contains("nonexistent"));
    }

    @Test
    public void containsAll_shouldReturnTrue_whenAllElementsArePresent() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);
        Collection<String> toCheck = Arrays.asList("one", "three");

        // Assert
        assertTrue(indexedCollection.containsAll(toCheck));
    }

    @Test
    public void containsAll_shouldReturnFalse_whenSomeElementsAreMissing() {
        // Arrange
        IndexedCollection<Integer, String> indexedCollection = IndexedCollection.nonUniqueIndexedCollection(list, keyTransformer);
        Collection<String> toCheck = Arrays.asList("one", "nonexistent");

        // Assert
        assertFalse(indexedCollection.containsAll(toCheck));
    }

    // --- Exception and Edge Case Tests ---

    @Test(expected = NullPointerException.class)
    public void add_shouldThrowException_whenTransformerIsNullAndElementIsAdded() {
        // Arrange: Start with an empty list to avoid NPE in constructor
        List<String> emptyList = new ArrayList<>();
        IndexedCollection<Integer, String> collectionWithNullTransformer =
            IndexedCollection.uniqueIndexedCollection(emptyList, null);

        // Act: Adding an element will trigger the use of the null transformer
        collectionWithNullTransformer.add("some-string");
    }

    @Test(expected = RuntimeException.class)
    public void remove_shouldPropagateException_whenTransformerThrows() {
        // Arrange
        Transformer<String, Integer> throwingTransformer = s -> {
            if ("two".equals(s)) {
                throw new RuntimeException("Transform failed");
            }
            return s.length();
        };
        IndexedCollection<Integer, String> indexedCollection =
            IndexedCollection.nonUniqueIndexedCollection(list, throwingTransformer);

        // Act
        indexedCollection.remove("two");
    }

    @Test
    public void add_shouldSupportNullElements_whenTransformerHandlesNull() {
        // Arrange
        list.add(null);
        // A transformer that maps null to key 0, and others to their length
        Transformer<String, Integer> nullSafeTransformer = s -> (s == null) ? 0 : s.length();
        IndexedCollection<Integer, String> indexedCollection =
            IndexedCollection.nonUniqueIndexedCollection(list, nullSafeTransformer);

        // Act
        indexedCollection.add(null);

        // Assert
        assertEquals(5, indexedCollection.size());
        assertEquals(2, indexedCollection.values(0).size());
        assertTrue(indexedCollection.remove(null));
        assertEquals(1, indexedCollection.values(0).size());
    }
}