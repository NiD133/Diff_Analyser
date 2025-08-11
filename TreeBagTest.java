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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.apache.commons.collections4.Bag;
import org.apache.commons.collections4.SortedBag;
import org.junit.jupiter.api.Test;

/**
 * Test suite for {@link TreeBag} implementation.
 * Extends {@link AbstractSortedBagTest} to inherit common sorted bag test cases.
 */
public class TreeBagTest<T> extends AbstractSortedBagTest<T> {

    private static final String COMPATIBILITY_VERSION = "4";
    
    // Test data constants for better maintainability
    private static final String ELEMENT_A = "A";
    private static final String ELEMENT_B = "B";
    private static final String ELEMENT_C = "C";
    private static final String ELEMENT_D = "D";

    @Override
    public String getCompatibilityVersion() {
        return COMPATIBILITY_VERSION;
    }

    @Override
    public SortedBag<T> makeObject() {
        return new TreeBag<>();
    }

    /**
     * Creates a TreeBag with sample data for testing.
     * Elements are added in non-sorted order: C, A, B, D
     * Expected sorted order should be: A, B, C, D
     */
    @SuppressWarnings("unchecked")
    public SortedBag<T> setupBag() {
        final SortedBag<T> bag = makeObject();
        // Add elements in non-alphabetical order to test sorting
        bag.add((T) ELEMENT_C);
        bag.add((T) ELEMENT_A);
        bag.add((T) ELEMENT_B);
        bag.add((T) ELEMENT_D);
        return bag;
    }

    /**
     * Test for COLLECTIONS-265: TreeBag should reject non-Comparable objects
     * when using natural ordering.
     * 
     * When adding an Object instance (which doesn't implement Comparable)
     * to a TreeBag using natural ordering, it should throw IllegalArgumentException.
     */
    @Test
    void testRejectsNonComparableObjectsWithNaturalOrdering() {
        final Bag<Object> bagWithNaturalOrdering = new TreeBag<>();
        final Object nonComparableObject = new Object();

        assertThrows(IllegalArgumentException.class, 
            () -> bagWithNaturalOrdering.add(nonComparableObject),
            "TreeBag should reject non-Comparable objects when using natural ordering");
    }

    /**
     * Test for COLLECTIONS-555: TreeBag should reject null values.
     * 
     * TreeBag should throw NullPointerException when attempting to add null,
     * both with natural ordering and custom comparator.
     */
    @Test
    void testRejectsNullValues() {
        // Test with natural ordering
        final Bag<Object> bagWithNaturalOrdering = new TreeBag<>();
        
        assertThrows(NullPointerException.class, 
            () -> bagWithNaturalOrdering.add(null),
            "TreeBag with natural ordering should reject null values");

        // Test with custom comparator
        final Bag<String> bagWithCustomComparator = new TreeBag<>(String::compareTo);
        
        // Add a non-null element first to ensure bag is not empty
        // (workaround for JDK bug where adding null to empty TreeMap works)
        bagWithCustomComparator.add("sample");

        assertThrows(NullPointerException.class, 
            () -> bagWithCustomComparator.add(null),
            "TreeBag with custom comparator should reject null values");
    }

    /**
     * Test that TreeBag maintains sorted order of elements.
     * 
     * Verifies that:
     * 1. Elements are returned in sorted order via toArray()
     * 2. first() returns the smallest element
     * 3. last() returns the largest element
     */
    @Test
    void testMaintainsSortedOrder() {
        final SortedBag<T> sortedBag = setupBag();
        final Object[] sortedElements = sortedBag.toArray();
        
        // Verify elements are in alphabetical order
        assertEquals(ELEMENT_A, sortedElements[0], 
            "First element should be 'A' (alphabetically first)");
        assertEquals(ELEMENT_B, sortedElements[1], 
            "Second element should be 'B'");
        assertEquals(ELEMENT_C, sortedElements[2], 
            "Third element should be 'C'");
        
        // Verify first() and last() methods
        assertEquals(ELEMENT_A, sortedBag.first(), 
            "first() should return the smallest element");
        assertEquals(ELEMENT_D, sortedBag.last(), 
            "last() should return the largest element");
    }

    // Commented out utility method for generating test data files
    // Uncomment and use when regenerating serialization test data
    //
    // void generateSerializationTestData() throws Exception {
    //     // Generate empty collection test data
    //     SortedBag<T> emptyBag = makeObject();
    //     writeExternalFormToDisk((java.io.Serializable) emptyBag, 
    //         "src/test/resources/data/test/TreeBag.emptyCollection.version4.obj");
    //     
    //     // Generate full collection test data
    //     SortedBag<T> fullBag = makeFullCollection();
    //     writeExternalFormToDisk((java.io.Serializable) fullBag, 
    //         "src/test/resources/data/test/TreeBag.fullCollection.version4.obj");
    // }
}