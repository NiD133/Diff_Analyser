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
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test class for FixedOrderComparator.
 * 
 * FixedOrderComparator allows defining a specific order for objects and comparing them
 * according to that predefined order. Once a comparison is made, the comparator becomes
 * locked and cannot be modified.
 */
class FixedOrderComparatorTest extends AbstractComparatorTest<String> {

    /**
     * Test data: Top cities of the world by population (including metro areas).
     * This provides a consistent, meaningful ordering for testing.
     */
    private static final String[] TOP_CITIES_BY_POPULATION = {
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

    // Test constants for better readability
    private static final String UNKNOWN_CITY_1 = "Minneapolis";
    private static final String UNKNOWN_CITY_2 = "St Paul";
    private static final String KNOWN_CITY = "New York";
    private static final String ANOTHER_KNOWN_CITY = "Tokyo";

    @Nested
    class EqualsTests {

        @Test
        void shouldReturnFalse_whenComparatorsHaveDifferentItems() {
            // Given: Two comparators with different fixed orders
            final FixedOrderComparator<Integer> firstComparator = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> secondComparator = new FixedOrderComparator<>(2, 3, 4);
            
            // When & Then: They should not be equal
            assertFalse(firstComparator.equals(secondComparator));
        }

        @Test
        void shouldReturnFalse_whenComparatorsHaveDifferentUnknownObjectBehavior() {
            // Given: Two comparators with different unknown object behaviors
            final FixedOrderComparator<Integer> beforeComparator = new FixedOrderComparator<>();
            beforeComparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
            
            final FixedOrderComparator<Integer> afterComparator = new FixedOrderComparator<>();
            afterComparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
            
            // When & Then: They should not be equal
            assertFalse(beforeComparator.equals(afterComparator));
        }

        @Test
        void shouldReturnFalse_whenComparedWithNull() {
            // Given: A comparator
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            
            // When & Then: Comparison with null should return false
            assertFalse(comparator.equals(null));
        }

        @Test
        void shouldReturnFalse_whenComparedWithDifferentObjectType() {
            // Given: A comparator and a different type of object
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            
            // When & Then: Comparison with different object type should return false
            assertFalse(comparator.equals(new Object()));
        }

        @Test
        void shouldReturnFalse_whenOnlyOneComparatorIsLocked() {
            // Given: Two identical comparators, but one becomes locked by performing a comparison
            final FixedOrderComparator<Integer> unlockedComparator = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> lockedComparator = new FixedOrderComparator<>(1, 2, 3);
            
            // When: One comparator is locked by performing a comparison
            lockedComparator.compare(1, 2);
            
            // Then: They should not be equal (different lock states)
            assertFalse(unlockedComparator.equals(lockedComparator));
        }

        @Test
        void shouldReturnFalse_whenComparatorsHaveDifferentNumberOfItems() {
            // Given: Two comparators with different numbers of items (one has duplicates)
            final FixedOrderComparator<Integer> comparatorWithoutDuplicates = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparatorWithDuplicates = new FixedOrderComparator<>(1, 2, 3, 3);
            
            // When & Then: They should not be equal
            assertFalse(comparatorWithoutDuplicates.equals(comparatorWithDuplicates));
        }

        @Test
        void shouldReturnTrue_whenBothComparatorsAreLockedAndIdentical() {
            // Given: Two identical comparators
            final FixedOrderComparator<Integer> firstComparator = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> secondComparator = new FixedOrderComparator<>(1, 2, 3);
            
            // When: Both are locked by performing comparisons
            firstComparator.compare(1, 2);
            secondComparator.compare(1, 2);
            
            // Then: They should be equal
            assertTrue(firstComparator.equals(secondComparator));
        }

        @Test
        void shouldReturnTrue_whenBothComparatorsAreEmptyAndUnlocked() {
            // Given: Two empty comparators
            final FixedOrderComparator<Integer> firstComparator = new FixedOrderComparator<>();
            final FixedOrderComparator<Integer> secondComparator = new FixedOrderComparator<>();
            
            // When & Then: They should be equal
            assertTrue(firstComparator.equals(secondComparator));
        }

        @Test
        void shouldReturnTrue_whenComparatorIsComparedWithItself() {
            // Given: A comparator
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            
            // When & Then: Self-comparison should return true
            assertTrue(comparator.equals(comparator));
        }
    }

    @Nested
    class ConstructorTests {

        @Test
        void shouldMaintainOrderWhenConstructedWithArray() {
            // Given: An array of cities and a comparator constructed with that array
            final String[] originalOrder = TOP_CITIES_BY_POPULATION.clone();
            final String[] testArray = TOP_CITIES_BY_POPULATION.clone();
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(testArray);
            
            // When: We modify the original array after construction
            testArray[0] = "Brighton";
            
            // Then: The comparator should maintain the original order (array was copied)
            assertComparatorMaintainsOrder(originalOrder, comparator);
        }

        @Test
        void shouldMaintainOrderWhenConstructedWithList() {
            // Given: A list of cities and a comparator constructed with that list
            final String[] originalOrder = TOP_CITIES_BY_POPULATION.clone();
            final List<String> testList = new LinkedList<>(Arrays.asList(TOP_CITIES_BY_POPULATION));
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(testList);
            
            // When: We modify the original list after construction
            testList.set(0, "Brighton");
            
            // Then: The comparator should maintain the original order (list was copied)
            assertComparatorMaintainsOrder(originalOrder, comparator);
        }

        @Test
        void shouldMaintainOrderWhenBuiltIncrementally() {
            // Given: An empty comparator
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>();
            
            // When: We add cities one by one
            for (final String city : TOP_CITIES_BY_POPULATION) {
                comparator.add(city);
            }
            
            // Then: The comparator should maintain the order in which items were added
            final String[] expectedOrder = TOP_CITIES_BY_POPULATION.clone();
            assertComparatorMaintainsOrder(expectedOrder, comparator);
        }
    }

    @Nested
    class ComparisonBehaviorTests {

        @Test
        void shouldAddItemsAsEqual() {
            // Given: A comparator with predefined order
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES_BY_POPULATION);
            
            // When: We add Minneapolis as equal to New York
            comparator.addAsEqual(KNOWN_CITY, UNKNOWN_CITY_1);
            
            // Then: Minneapolis should compare equal to New York, but maintain New York's position relative to others
            assertEquals(0, comparator.compare(KNOWN_CITY, UNKNOWN_CITY_1));
            assertEquals(-1, comparator.compare(ANOTHER_KNOWN_CITY, UNKNOWN_CITY_1)); // Tokyo comes before New York
            assertEquals(1, comparator.compare("Shanghai", UNKNOWN_CITY_1)); // Shanghai comes after New York
        }

        @Test
        void shouldHandleUnknownObjectsBasedOnBehaviorSetting() {
            testUnknownObjectBehavior_Exception();
            testUnknownObjectBehavior_Before();
            testUnknownObjectBehavior_After();
        }

        private void testUnknownObjectBehavior_Exception() {
            // Given: A comparator with default EXCEPTION behavior for unknown objects
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES_BY_POPULATION);
            
            // Then: Default behavior should be EXCEPTION
            assertEquals(FixedOrderComparator.UnknownObjectBehavior.EXCEPTION, comparator.getUnknownObjectBehavior());
            
            // When & Then: Comparing with unknown objects should throw IllegalArgumentException
            assertThrows(IllegalArgumentException.class, 
                () -> comparator.compare(KNOWN_CITY, UNKNOWN_CITY_1),
                "Should throw IllegalArgumentException when comparing known object with unknown object");
                
            assertThrows(IllegalArgumentException.class, 
                () -> comparator.compare(UNKNOWN_CITY_1, KNOWN_CITY),
                "Should throw IllegalArgumentException when comparing unknown object with known object");
        }

        private void testUnknownObjectBehavior_Before() {
            // Given: A comparator set to treat unknown objects as coming BEFORE known objects
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES_BY_POPULATION);
            comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
            
            // Then: Behavior should be set correctly
            assertEquals(FixedOrderComparator.UnknownObjectBehavior.BEFORE, comparator.getUnknownObjectBehavior());
            
            // When: We create expected order with unknown object first
            final LinkedList<String> expectedOrder = new LinkedList<>(Arrays.asList(TOP_CITIES_BY_POPULATION));
            expectedOrder.addFirst(UNKNOWN_CITY_1);
            
            // Then: Unknown objects should sort before known objects
            assertComparatorMaintainsOrder(expectedOrder.toArray(ArrayUtils.EMPTY_STRING_ARRAY), comparator);
            assertEquals(-1, comparator.compare(UNKNOWN_CITY_1, KNOWN_CITY)); // Unknown before known
            assertEquals(1, comparator.compare(KNOWN_CITY, UNKNOWN_CITY_1));  // Known after unknown
            assertEquals(0, comparator.compare(UNKNOWN_CITY_1, UNKNOWN_CITY_2)); // Unknown objects are equal
        }

        private void testUnknownObjectBehavior_After() {
            // Given: A comparator set to treat unknown objects as coming AFTER known objects
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES_BY_POPULATION);
            comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
            
            // When: We create expected order with unknown object last
            final LinkedList<String> expectedOrder = new LinkedList<>(Arrays.asList(TOP_CITIES_BY_POPULATION));
            expectedOrder.add(UNKNOWN_CITY_1);
            
            // Then: Unknown objects should sort after known objects
            assertComparatorMaintainsOrder(expectedOrder.toArray(ArrayUtils.EMPTY_STRING_ARRAY), comparator);
            assertEquals(1, comparator.compare(UNKNOWN_CITY_1, KNOWN_CITY));  // Unknown after known
            assertEquals(-1, comparator.compare(KNOWN_CITY, UNKNOWN_CITY_1)); // Known before unknown
            assertEquals(0, comparator.compare(UNKNOWN_CITY_1, UNKNOWN_CITY_2)); // Unknown objects are equal
        }
    }

    @Nested
    class LockingBehaviorTests {

        @Test
        void shouldLockAfterFirstComparison() {
            // Given: An unlocked comparator
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES_BY_POPULATION);
            
            // Then: Initially should not be locked
            assertFalse(comparator.isLocked());
            
            // When: We perform a comparison
            comparator.compare(KNOWN_CITY, ANOTHER_KNOWN_CITY);
            
            // Then: Comparator should become locked
            assertTrue(comparator.isLocked());
        }

        @Test
        void shouldPreventModificationWhenLocked() {
            // Given: A locked comparator (locked by performing a comparison)
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES_BY_POPULATION);
            comparator.compare(KNOWN_CITY, ANOTHER_KNOWN_CITY); // This locks the comparator
            
            // When & Then: Attempts to modify should throw UnsupportedOperationException
            assertThrows(UnsupportedOperationException.class, 
                () -> comparator.add(UNKNOWN_CITY_1),
                "Should not allow adding items after comparator is locked");

            assertThrows(UnsupportedOperationException.class, 
                () -> comparator.addAsEqual(KNOWN_CITY, UNKNOWN_CITY_1),
                "Should not allow adding equal items after comparator is locked");
        }
    }

    // Inherited abstract methods implementation
    @Override
    public List<String> getComparableObjectsOrdered() {
        return Arrays.asList(TOP_CITIES_BY_POPULATION);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public Comparator<String> makeObject() {
        return new FixedOrderComparator<>(TOP_CITIES_BY_POPULATION);
    }

    // Helper methods
    
    /**
     * Verifies that the comparator maintains the specified order by:
     * 1. Shuffling the input array to create a random order
     * 2. Sorting the shuffled array using the comparator
     * 3. Asserting that the result matches the expected order
     * 
     * @param expectedOrder The order that should be maintained
     * @param comparator The comparator to test
     */
    private void assertComparatorMaintainsOrder(final String[] expectedOrder, final Comparator<String> comparator) {
        final String[] shuffledArray = expectedOrder.clone();

        // Shuffle the array until we get a different order (to ensure we're actually testing sorting)
        boolean isShuffled = false;
        final Random random = new Random();
        while (shuffledArray.length > 1 && !isShuffled) {
            // Perform Fisher-Yates shuffle
            for (int i = shuffledArray.length - 1; i > 0; i--) {
                final int randomIndex = random.nextInt(i + 1);
                final String temp = shuffledArray[i];
                shuffledArray[i] = shuffledArray[randomIndex];
                shuffledArray[randomIndex] = temp;
            }

            // Check if the order actually changed
            for (int i = 0; i < shuffledArray.length; i++) {
                if (!expectedOrder[i].equals(shuffledArray[i])) {
                    isShuffled = true;
                    break;
                }
            }
        }

        // Sort using the comparator and verify the result matches expected order
        Arrays.sort(shuffledArray, comparator);

        for (int i = 0; i < expectedOrder.length; i++) {
            assertEquals(expectedOrder[i], shuffledArray[i], 
                "Item at position " + i + " should match expected order");
        }
    }
}