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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractBagTest} for exercising the {@link TreeBag}
 * implementation.
 */
public class TreeBagTest<T> extends AbstractSortedBagTest<T> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public SortedBag<T> makeObject() {
        return new TreeBag<>();
    }

    /**
     * Creates a bag with a known set of elements.
     *
     * @return a new bag containing "A", "B", "C", "D"
     */
    @SuppressWarnings("unchecked")
    private SortedBag<T> setupBagWithKnownElements() {
        final SortedBag<T> bag = makeObject();
        bag.add((T) "C");
        bag.add((T) "A");
        bag.add((T) "B");
        bag.add((T) "D");
        return bag;
    }

    @Test
    @DisplayName("add(Object) should throw IllegalArgumentException for non-comparable types")
    void testAddNonComparableTypeThrowsException() {
        final Bag<Object> bag = new TreeBag<>();
        // A TreeBag with no comparator requires elements to be Comparable.
        assertThrows(IllegalArgumentException.class, () -> bag.add(new Object()),
            "Adding an object that doesn't implement Comparable should fail.");
    }

    @Test
    @DisplayName("add(null) should throw NullPointerException for a bag with natural ordering")
    void testAddNullToNaturalOrderBagThrowsException() {
        final Bag<Object> bag = new TreeBag<>();
        assertThrows(NullPointerException.class, () -> bag.add(null),
            "Adding null to a TreeBag using natural ordering should fail.");
    }

    @Test
    @DisplayName("add(null) should throw NullPointerException for a bag with a comparator")
    void testAddNullToBagWithComparatorThrowsException() {
        final Bag<String> bag = new TreeBag<>(String::compareTo);
        // Per TreeMap Javadoc, behavior with nulls depends on the comparator.
        // String::compareTo throws a NullPointerException.
        // We add an element first to avoid edge cases with empty TreeMaps.
        bag.add("a");

        assertThrows(NullPointerException.class, () -> bag.add(null),
            "Adding null to a TreeBag with a non-null-safe comparator should fail.");
    }

    @Test
    @DisplayName("TreeBag should maintain elements in sorted order")
    void testOrdering() {
        final SortedBag<T> bag = setupBagWithKnownElements();

        // Elements added were "C", "A", "B", "D". Expected sorted order is "A", "B", "C", "D".
        final Object[] expectedOrder = {"A", "B", "C", "D"};
        assertArrayEquals(expectedOrder, bag.toArray(), "Elements should be sorted in toArray()");

        assertEquals("A", bag.first(), "first() should return the lowest element");
        assertEquals("D", bag.last(), "last() should return the highest element");
    }

}