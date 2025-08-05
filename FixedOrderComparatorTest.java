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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test class for FixedOrderComparator.
 */
class FixedOrderComparatorTest extends AbstractComparatorTest<String> {

    private static final String UNKNOWN_CITY = "Minneapolis";
    private static final String DUPLICATE_CITY = "Sao Paulo";
    private static final String[] TOP_CITIES = {
        "Tokyo",
        "Mexico City",
        "Mumbai",
        "Sao Paulo",
        "New York",
        "Shanghai",
        "Lagos",
        "Los Angeles",
        "Calcutta",
        "Buenos Aires"
    };

    @Override
    public List<String> getComparableObjectsOrdered() {
        return Arrays.asList(TOP_CITIES);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public Comparator<String> makeObject() {
        return new FixedOrderComparator<>(TOP_CITIES);
    }

    /**
     * Asserts that the comparator sorts shuffled keys back to original order.
     * 
     * @param expectedOrder The correctly ordered items
     * @param comparator Comparator to test
     */
    private void assertComparatorYieldsOrder(String[] expectedOrder, Comparator<String> comparator) {
        final List<String> shuffledList = new LinkedList<>(Arrays.asList(expectedOrder));
        
        // Edge case: single element requires no shuffle
        if (shuffledList.size() > 1) {
            // Ensure we get a different order
            do {
                Collections.shuffle(shuffledList, new Random());
            } while (shuffledList.equals(Arrays.asList(expectedOrder)));
        }
        
        final String[] shuffledArray = shuffledList.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
        Arrays.sort(shuffledArray, comparator);
        
        assertTrue(Arrays.equals(expectedOrder, shuffledArray),
                "Comparator failed to sort items correctly. Expected: " + Arrays.toString(expectedOrder) +
                " but was: " + Arrays.toString(shuffledArray));
    }

    @Nested
    class Constructors {
        @Test
        void shouldMaintainOrder_whenUsingArrayConstructor() {
            final String[] originalOrder = TOP_CITIES.clone();
            final String[] mutableArray = TOP_CITIES.clone();
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(mutableArray);
            
            // Mutate after construction - should not affect comparator
            mutableArray[0] = "ModifiedCity";
            
            assertComparatorYieldsOrder(originalOrder, comparator);
        }

        @Test
        void shouldMaintainOrder_whenUsingListConstructor() {
            final String[] originalOrder = TOP_CITIES.clone();
            final List<String> mutableList = new LinkedList<>(Arrays.asList(TOP_CITIES));
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(mutableList);
            
            // Mutate after construction - should not affect comparator
            mutableList.set(0, "ModifiedCity");
            
            assertComparatorYieldsOrder(originalOrder, comparator);
        }

        @Test
        void shouldMaintainOrder_whenUsingAddMethod() {
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>();
            for (String city : TOP_CITIES) {
                comparator.add(city);
            }
            
            assertComparatorYieldsOrder(TOP_CITIES, comparator);
        }
    }

    @Nested
    class AddAsEqual {
        @Test
        void shouldTreatAddedItemsAsEqual() {
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            comparator.addAsEqual("New York", UNKNOWN_CITY);
            
            assertEquals(0, comparator.compare("New York", UNKNOWN_CITY),
                    "Items added as equal should compare equal");
            assertEquals(-1, comparator.compare("Tokyo", UNKNOWN_CITY),
                    "Known item before equal group should be smaller");
            assertEquals(1, comparator.compare("Shanghai", UNKNOWN_CITY),
                    "Known item after equal group should be larger");
        }

        @Test
        void shouldThrowException_whenAddingEqualToUnknownItem() {
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            
            assertThrows(IllegalArgumentException.class, 
                () -> comparator.addAsEqual("UnknownCity", UNKNOWN_CITY),
                "Should throw when base item is unknown");
        }
    }

    @Nested
    class Locking {
        @Test
        void shouldLockAfterFirstComparison() {
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            
            assertFalse(comparator.isLocked(), "Comparator should be unlocked initially");
            comparator.compare("New York", "Tokyo");
            assertTrue(comparator.isLocked(), "Comparator should lock after comparison");
        }

        @Test
        void shouldPreventModificationsWhenLocked() {
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            comparator.compare("New York", "Tokyo"); // Locks comparator
            
            assertThrows(UnsupportedOperationException.class, 
                () -> comparator.add("Minneapolis"),
                "Should prevent additions when locked");
                
            assertThrows(UnsupportedOperationException.class, 
                () -> comparator.addAsEqual("New York", "Minneapolis"),
                "Should prevent addAsEqual when locked");
        }
    }

    @Nested
    class UnknownObjectBehavior {
        @Test
        void shouldThrowExceptionForUnknownObjectsByDefault() {
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            
            assertEquals(FixedOrderComparator.UnknownObjectBehavior.EXCEPTION, 
                         comparator.getUnknownObjectBehavior());
                         
            assertThrows(IllegalArgumentException.class, 
                () -> comparator.compare("New York", UNKNOWN_CITY));
            assertThrows(IllegalArgumentException.class, 
                () -> comparator.compare(UNKNOWN_CITY, "New York"));
        }

        @Test
        void shouldTreatUnknownObjectsAsBefore() {
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
            
            final String[] expectedOrder = ArrayUtils.addFirst(TOP_CITIES, UNKNOWN_CITY);
            assertComparatorYieldsOrder(expectedOrder, comparator);
            
            assertEquals(-1, comparator.compare(UNKNOWN_CITY, "New York"));
            assertEquals(1, comparator.compare("New York", UNKNOWN_CITY));
            assertEquals(0, comparator.compare(UNKNOWN_CITY, "St Paul"));
        }

        @Test
        void shouldTreatUnknownObjectsAsAfter() {
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
            
            final String[] expectedOrder = ArrayUtils.add(TOP_CITIES, UNKNOWN_CITY);
            assertComparatorYieldsOrder(expectedOrder, comparator);
            
            assertEquals(1, comparator.compare(UNKNOWN_CITY, "New York"));
            assertEquals(-1, comparator.compare("New York", UNKNOWN_CITY));
            assertEquals(0, comparator.compare(UNKNOWN_CITY, "St Paul"));
        }
    }

    @Nested
    class Equals {
        @Test
        void shouldBeEqual_whenBothEmpty() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        void shouldBeEqual_whenSameInstance() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertTrue(comparator.equals(comparator));
        }

        @Test
        void shouldBeEqual_whenSameItemsAndBothLocked() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);
            comparator1.compare(1, 2);
            comparator2.compare(1, 2);
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        void shouldNotBeEqual_whenDifferentItems() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(2, 3, 4);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldNotBeEqual_whenDuplicateItems() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3, 3);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldNotBeEqual_whenDifferentLockState() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);
            comparator2.compare(1, 2); // Lock only one comparator
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldNotBeEqual_whenDifferentUnknownObjectBehavior() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            comparator1.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
            
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            comparator2.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
            
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldNotBeEqual_whenComparedWithNull() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(null));
        }

        @Test
        void shouldNotBeEqual_whenComparedWithDifferentType() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(new Object()));
        }
    }
}