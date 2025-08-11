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

import org.apache.commons.collections4.Predicate;
import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.functors.TruePredicate;
import org.apache.commons.collections4.functors.UniquePredicate;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link CollectionSortedBag}.
 * This class focuses on the decorator's behavior, ensuring it correctly
 * delegates calls and adapts the Bag interface to the Collection contract.
 */
public class CollectionSortedBagTest {

    private SortedBag<String> decoratedBag;
    private SortedBag<String> collectionSortedBag;

    @Before
    public void setUp() {
        // Use TreeBag as a standard concrete implementation of SortedBag
        decoratedBag = new TreeBag<>();
        collectionSortedBag = CollectionSortedBag.collectionSortedBag(decoratedBag);
    }

    // --- Constructor and Factory Tests ---

    @Test
    public void constructor_decoratesGivenBag() {
        // Arrange
        decoratedBag.add("A");

        // Assert
        // The decorator should reflect the state of the decorated bag.
        assertEquals(1, collectionSortedBag.size());
        assertTrue(collectionSortedBag.contains("A"));
    }

    @Test(expected = NullPointerException.class)
    public void constructor_withNullBag_throwsNullPointerException() {
        // Act
        new CollectionSortedBag<>(null);
    }

    @Test(expected = NullPointerException.class)
    public void collectionSortedBag_factoryMethodWithNull_throwsNullPointerException() {
        // Act
        CollectionSortedBag.collectionSortedBag(null);
    }

    // --- Collection#add Tests ---

    @Test
    public void add_whenElementIsNew_returnsTrueAndIncreasesSize() {
        // Act
        boolean changed = collectionSortedBag.add("A");

        // Assert
        assertTrue(changed);
        assertEquals(1, collectionSortedBag.size());
        assertEquals(1, collectionSortedBag.getCount("A"));
    }

    @Test
    public void add_whenElementExists_returnsTrueAndIncreasesCount() {
        // Arrange
        collectionSortedBag.add("A");

        // Act
        boolean changed = collectionSortedBag.add("A");

        // Assert
        assertTrue(changed);
        assertEquals(2, collectionSortedBag.size());
        assertEquals(2, collectionSortedBag.getCount("A"));
    }

    // --- Collection#remove Tests ---

    @Test
    public void remove_whenElementExists_returnsTrueAndDecreasesCountByOne() {
        // Arrange
        collectionSortedBag.add("A", 3); // Add "A" three times

        // Act
        boolean changed = collectionSortedBag.remove("A");

        // Assert
        assertTrue(changed);
        assertEquals(2, collectionSortedBag.size());
        assertEquals(2, collectionSortedBag.getCount("A"));
    }

    @Test
    public void remove_whenElementDoesNotExist_returnsFalse() {
        // Arrange
        collectionSortedBag.add("A");

        // Act
        boolean changed = collectionSortedBag.remove("B");

        // Assert
        assertFalse(changed);
        assertEquals(1, collectionSortedBag.size());
    }

    // --- Collection#addAll Tests ---

    @Test
    public void addAll_withElements_addsAllAndReturnsTrue() {
        // Arrange
        List<String> elementsToAdd = Arrays.asList("A", "B", "A");

        // Act
        boolean changed = collectionSortedBag.addAll(elementsToAdd);

        // Assert
        assertTrue(changed);
        assertEquals(3, collectionSortedBag.size());
        assertEquals(2, collectionSortedBag.getCount("A"));
        assertEquals(1, collectionSortedBag.getCount("B"));
    }

    @Test
    public void addAll_withEmptyCollection_returnsFalse() {
        // Arrange
        collectionSortedBag.add("A");

        // Act
        boolean changed = collectionSortedBag.addAll(Collections.emptyList());

        // Assert
        assertFalse(changed);
        assertEquals(1, collectionSortedBag.size());
    }

    @Test(expected = NullPointerException.class)
    public void addAll_withNullCollection_throwsNullPointerException() {
        // Act
        collectionSortedBag.addAll(null);
    }

    // --- Collection#removeAll Tests ---

    @Test
    public void removeAll_withSubsetOfElements_removesElementsAndReturnsTrue() {
        // Arrange
        collectionSortedBag.add("A", 2);
        collectionSortedBag.add("B", 1);
        collectionSortedBag.add("C", 1);
        List<String> elementsToRemove = Arrays.asList("A", "C", "D"); // "D" is not in the bag

        // Act
        boolean changed = collectionSortedBag.removeAll(elementsToRemove);

        // Assert
        assertTrue(changed);
        assertEquals(1, collectionSortedBag.size());
        assertFalse(collectionSortedBag.contains("A"));
        assertTrue(collectionSortedBag.contains("B"));
        assertFalse(collectionSortedBag.contains("C"));
    }

    @Test
    public void removeAll_withNoCommonElements_returnsFalse() {
        // Arrange
        collectionSortedBag.add("A");
        collectionSortedBag.add("B");
        List<String> elementsToRemove = Arrays.asList("C", "D");

        // Act
        boolean changed = collectionSortedBag.removeAll(elementsToRemove);

        // Assert
        assertFalse(changed);
        assertEquals(2, collectionSortedBag.size());
    }

    // --- Collection#retainAll Tests ---

    @Test
    public void retainAll_withSubset_removesElementsNotInSubset() {
        // Arrange
        collectionSortedBag.add("A", 2);
        collectionSortedBag.add("B", 1);
        collectionSortedBag.add("C", 1);
        List<String> elementsToRetain = Arrays.asList("B", "C", "D"); // "D" is not in the bag

        // Act
        boolean changed = collectionSortedBag.retainAll(elementsToRetain);

        // Assert
        assertTrue(changed);
        assertEquals(2, collectionSortedBag.size());
        assertFalse(collectionSortedBag.contains("A"));
        assertTrue(collectionSortedBag.contains("B"));
        assertTrue(collectionSortedBag.contains("C"));
    }

    @Test
    public void retainAll_withSelf_doesNothingAndReturnsFalse() {
        // Arrange
        collectionSortedBag.add("A");
        collectionSortedBag.add("B");

        // Act
        boolean changed = collectionSortedBag.retainAll(collectionSortedBag);

        // Assert
        assertFalse(changed);
        assertEquals(2, collectionSortedBag.size());
    }

    // --- Collection#containsAll Tests ---

    @Test
    public void containsAll_whenAllElementsPresent_returnsTrue() {
        // Arrange
        collectionSortedBag.add("A", 2);
        collectionSortedBag.add("B");
        List<String> elementsToCheck = Arrays.asList("A", "B", "A");

        // Act & Assert
        assertTrue(collectionSortedBag.containsAll(elementsToCheck));
    }


    @Test
    public void containsAll_whenSomeElementsMissing_returnsFalse() {
        // Arrange
        collectionSortedBag.add("A");
        collectionSortedBag.add("B");
        List<String> elementsToCheck = Arrays.asList("A", "C");

        // Act & Assert
        assertFalse(collectionSortedBag.containsAll(elementsToCheck));
    }

    @Test
    public void containsAll_withEmptyCollection_returnsTrue() {
        // Arrange
        collectionSortedBag.add("A");

        // Act & Assert
        assertTrue(collectionSortedBag.containsAll(Collections.emptyList()));
    }

    // --- Exception Handling with Other Decorators ---

    @Test(expected = UnsupportedOperationException.class)
    public void modificationMethods_onUnmodifiableDecoratedBag_throwException() {
        // Arrange
        SortedBag<String> unmodifiable = UnmodifiableSortedBag.unmodifiableSortedBag(decoratedBag);
        SortedBag<String> unmodifiableCollectionBag = CollectionSortedBag.collectionSortedBag(unmodifiable);

        // Act
        unmodifiableCollectionBag.add("A"); // Any modification should fail
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_onPredicatedBagThatRejects_throwsIllegalArgumentException() {
        // Arrange
        // This predicate only allows unique elements
        Predicate<String> uniquePredicate = UniquePredicate.uniquePredicate();
        SortedBag<String> predicated = PredicatedSortedBag.predicatedSortedBag(decoratedBag, uniquePredicate);
        SortedBag<String> predicatedCollectionBag = CollectionSortedBag.collectionSortedBag(predicated);
        predicatedCollectionBag.add("A"); // First add is fine

        // Act
        predicatedCollectionBag.add("A"); // Second add should be rejected
    }

    @Test(expected = ClassCastException.class)
    public void methodWithIncompatibleType_onDefaultTreeBag_throwsClassCastException() {
        // Arrange
        // TreeBag (without a custom comparator) cannot compare different types.
        SortedBag rawBag = new TreeBag();
        SortedBag collectionRawBag = CollectionSortedBag.collectionSortedBag(rawBag);
        collectionRawBag.add("A String");

        // Act
        // Attempting to check for an Integer will cause a ClassCastException during comparison.
        collectionRawBag.contains(123);
    }

    @Test(expected = NullPointerException.class)
    public void remove_nullFromDefaultTreeBag_throwsNullPointerException() {
        // Arrange
        // A standard TreeBag does not allow nulls.
        collectionSortedBag.add("A");

        // Act
        collectionSortedBag.remove(null);
    }
}