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
package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ComparableComparator}.
 * <p>
 * This test suite verifies the basic functionality of the {@link ComparableComparator},
 * ensuring it correctly compares {@link Comparable} objects.
 * </p>
 */
@SuppressWarnings("boxing")
class ComparableComparatorTest {

    private Comparator<Integer> comparator;
    private List<Integer> orderedList;

    @BeforeEach
    void setup() {
        comparator = new ComparableComparator<>();
        orderedList = createOrderedList();
    }

    /**
     * Creates a list of Integers in ascending order for testing.
     *
     * @return A list of Integers [1, 2, 3, 4, 5].
     */
    private List<Integer> createOrderedList() {
        final List<Integer> list = new LinkedList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        return list;
    }

    /**
     * Tests that the comparator compares two comparable objects correctly.
     */
    @Test
    void testCompare() {
        assertEquals(0, comparator.compare(3, 3), "Should return 0 when objects are equal.");
        assertTrue(comparator.compare(1, 5) < 0, "Should return negative value when first object is less than second.");
        assertTrue(comparator.compare(5, 1) > 0, "Should return positive value when first object is greater than second.");
    }

    /**
     * Tests that the comparator is equal to itself and other instances of ComparableComparator.
     */
    @Test
    void testEquals() {
        Comparator<Integer> sameComparator = new ComparableComparator<>();
        assertEquals(comparator, comparator, "Comparator should be equal to itself.");
        assertEquals(comparator, sameComparator, "Comparator should be equal to another instance of ComparableComparator.");
    }

    /**
     * Tests that the hash code is consistent with the equals method.
     */
    @Test
    void testHashCode() {
        Comparator<Integer> sameComparator = new ComparableComparator<>();
        assertEquals(comparator.hashCode(), sameComparator.hashCode(), "Hash codes should be equal for equal comparators.");
    }

    // The following test was commented out in the original code.  It's generally good practice
    // to remove commented out code, or uncomment and fix it if it's still relevant.
    // In this case, it appears to be related to serialization testing, which could be useful.
    // However, without the writeExternalFormToDisk method, it's incomplete.  Leaving it out for now.
    // /**
    //  * Tests the creation and serialization of the comparator.
    //  */
    // @Test
    // void testCreate() throws Exception {
    //     writeExternalFormToDisk((java.io.Serializable) comparator, "src/test/resources/data/test/ComparableComparator.version4.obj");
    // }

}