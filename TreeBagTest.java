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
import org.junit.jupiter.api.Test;

/**
 * Extension of {@link AbstractBagTest} for exercising the {@link TreeBag}
 * implementation.
 */
public class TreeBagTest extends AbstractSortedBagTest<String> {

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public SortedBag<String> makeObject() {
        return new TreeBag<>();
    }

    /**
     * Creates a populated bag with elements "A", "B", "C", "D" in unsorted order.
     * The bag should maintain natural ordering of elements.
     */
    private SortedBag<String> createPopulatedBag() {
        final SortedBag<String> bag = makeObject();
        bag.add("C");
        bag.add("A");
        bag.add("B");
        bag.add("D");
        return bag;
    }

    /**
     * Tests that adding a non-comparable object to an empty TreeBag
     * (with natural ordering) throws IllegalArgumentException.
     */
    @Test
    void testAddNonComparableObjectThrowsException() {
        final Bag<Object> bag = new TreeBag<>();
        assertThrows(IllegalArgumentException.class, 
            () -> bag.add(new Object()),
            "Adding non-comparable object should throw IllegalArgumentException");
    }

    /**
     * Tests that adding null to a TreeBag with natural ordering 
     * throws NullPointerException.
     */
    @Test
    void testAddNullToNaturalOrderBagThrowsException() {
        final Bag<String> bag = new TreeBag<>();
        assertThrows(NullPointerException.class, 
            () -> bag.add(null),
            "Adding null to natural-order bag should throw NullPointerException");
    }

    /**
     * Tests that adding null to a non-empty TreeBag with custom comparator
     * throws NullPointerException. The bag is populated first to avoid edge cases
     * with empty collections (JDK TreeMap allows adding null to empty collection).
     */
    @Test
    void testAddNullToCustomOrderBagThrowsException() {
        final Bag<String> bag = new TreeBag<>(String::compareTo);
        bag.add("a");  // Ensure bag is non-empty before adding null
        
        assertThrows(NullPointerException.class, 
            () -> bag.add(null),
            "Adding null to custom-order bag should throw NullPointerException");
    }

    /**
     * Tests that the toArray() method returns elements in their natural sorted order.
     */
    @Test
    void testToArrayReturnsElementsInNaturalOrder() {
        final SortedBag<String> bag = createPopulatedBag();
        final Object[] expectedOrder = {"A", "B", "C", "D"};
        assertArrayEquals(expectedOrder, bag.toArray(),
            "toArray() should return elements in natural sorted order");
    }

    /**
     * Tests that the first() method returns the smallest element.
     */
    @Test
    void testFirstReturnsSmallestElement() {
        final SortedBag<String> bag = createPopulatedBag();
        assertEquals("A", bag.first(),
            "first() should return the smallest element");
    }

    /**
     * Tests that the last() method returns the largest element.
     */
    @Test
    void testLastReturnsLargestElement() {
        final SortedBag<String> bag = createPopulatedBag();
        assertEquals("D", bag.last(),
            "last() should return the largest element");
    }

}