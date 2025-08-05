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

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Locale;
import java.util.NoSuchElementException;

/**
 * Unit tests for {@link TreeBag}.
 */
public class TreeBagTest {

    //-----------------------------------------------------------------------
    // Constructor tests
    //-----------------------------------------------------------------------

    @Test(expected = NullPointerException.class)
    public void constructorWithNullCollection_throwsNullPointerException() {
        new TreeBag<>((Collection<String>) null);
    }

    @Test(expected = NullPointerException.class)
    public void constructorWithNullIterable_throwsNullPointerException() {
        new TreeBag<>((Iterable<String>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithCollectionOfNonComparable_throwsIllegalArgumentException() {
        Collection<Object> nonComparables = new ArrayList<>();
        nonComparables.add(new Object());
        new TreeBag<>(nonComparables);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorWithIterableOfNonComparable_throwsIllegalArgumentException() {
        Iterable<Object> nonComparables = Collections.singletonList(new Object());
        new TreeBag<>(nonComparables);
    }

    //-----------------------------------------------------------------------
    // add() tests
    //-----------------------------------------------------------------------

    @Test
    public void add_withComparatorTreatingElementsAsEqual_returnsTrueForFirstAndFalseForSubsequent() {
        // A comparator that makes all objects equal
        Comparator<Object> comparator = (o1, o2) -> 0;
        TreeBag<Object> bag = new TreeBag<>(comparator);

        // First add should return true as the element is new to the bag
        assertTrue("Adding a new element should return true", bag.add("A"));
        // Second add should return false as an "equal" element is already present
        assertFalse("Adding an existing element should return false", bag.add("B"));

        assertEquals("Bag size should be 2", 2, bag.size());
        assertEquals("Bag should have 1 unique element", 1, bag.uniqueSet().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void add_nonComparableToNaturallyOrderedBag_throwsIllegalArgumentException() {
        TreeBag<Object> bag = new TreeBag<>();
        bag.add(new Object()); // Object is not Comparable
    }

    @Test(expected = ClassCastException.class)
    public void add_incompatibleTypeToNaturallyOrderedBag_throwsClassCastException() {
        // Bag is created with a Comparable type (Enum)
        TreeBag<Object> bag = new TreeBag<>(EnumSet.of(Locale.Category.FORMAT));
        // Adding a String, which is not comparable to Locale.Category, causes CCE
        bag.add("a string");
    }

    @Test(expected = NullPointerException.class)
    public void add_nullToNaturallyOrderedBag_throwsNullPointerException() {
        TreeBag<String> bag = new TreeBag<>();
        bag.add(null);
    }

    //-----------------------------------------------------------------------
    // first() and last() tests
    //-----------------------------------------------------------------------

    @Test
    public void first_onNaturallyOrderedBag_returnsSmallestElement() {
        TreeBag<String> bag = new TreeBag<>();
        bag.add("C");
        bag.add("A");
        bag.add("B");
        assertEquals("A", bag.first());
    }

    @Test
    public void last_onNaturallyOrderedBag_returnsLargestElement() {
        TreeBag<String> bag = new TreeBag<>();
        bag.add("C");
        bag.add("A");
        bag.add("B");
        assertEquals("C", bag.last());
    }

    @Test(expected = NoSuchElementException.class)
    public void first_onEmptyBag_throwsNoSuchElementException() {
        new TreeBag<>().first();
    }

    @Test(expected = NoSuchElementException.class)
    public void last_onEmptyBag_throwsNoSuchElementException() {
        new TreeBag<>().last();
    }
    
    @Test
    public void first_withCustomComparatorAndNullElement_returnsNull() {
        // A comparator that handles nulls
        Comparator<Object> comparator = (o1, o2) -> 0;
        TreeBag<Object> bag = new TreeBag<>(comparator);
        bag.add(null);
        assertNull(bag.first());
    }

    @Test
    public void last_withCustomComparatorAndNullElement_returnsNull() {
        // A comparator that handles nulls
        Comparator<Object> comparator = (o1, o2) -> 0;
        TreeBag<Object> bag = new TreeBag<>(comparator);
        bag.add(null);
        assertNull(bag.last());
    }

    //-----------------------------------------------------------------------
    // comparator() tests
    //-----------------------------------------------------------------------

    @Test
    public void comparator_whenUsingNaturalOrdering_returnsNull() {
        TreeBag<String> bag = new TreeBag<>();
        assertNull(bag.comparator());
    }

    @Test
    public void comparator_whenUsingCustomComparator_returnsTheGivenComparator() {
        Comparator<Object> customComparator = (o1, o2) -> 0;
        TreeBag<Object> bag = new TreeBag<>(customComparator);
        assertSame(customComparator, bag.comparator());
    }

    //-----------------------------------------------------------------------
    // getMap() tests (requires test to be in same package)
    //-----------------------------------------------------------------------

    @Test
    public void getMap_onEmptyBag_isEmpty() {
        TreeBag<String> bag = new TreeBag<>();
        assertTrue("Underlying map should be empty", bag.getMap().isEmpty());
    }

    @Test
    public void getMap_onNonEmptyBag_isNonEmpty() {
        TreeBag<String> bag = new TreeBag<>();
        bag.add("A");
        assertFalse("Underlying map should not be empty", bag.getMap().isEmpty());
    }
}