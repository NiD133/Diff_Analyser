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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for FixedOrderComparator.
 */
@DisplayName("FixedOrderComparator")
class FixedOrderComparatorTest extends AbstractComparatorTest<String> {

    /**
     * Top cities of the world, by population including metro areas.
     * Used as the basis for the fixed order in tests.
     */
    private static final String[] TOP_CITIES = {
        "Tokyo", "Mexico City", "Mumbai", "Sao Paulo", "New York",
        "Shanghai", "Lagos", "Los Angeles", "Calcutta", "Buenos Aires"
    };

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        @Test
        void shouldCreateComparatorFromArrayAndSortCorrectly() {
            // Arrange
            final Comparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);

            // Act & Assert
            assertSortsCorrectly(TOP_CITIES, comparator);
        }

        @Test
        void shouldCreateComparatorFromListAndSortCorrectly() {
            // Arrange
            final List<String> cityList = Arrays.asList(TOP_CITIES);
            final Comparator<String> comparator = new FixedOrderComparator<>(cityList);

            // Act & Assert
            assertSortsCorrectly(TOP_CITIES, comparator);
        }

        @Test
        void shouldDefensivelyCopyInputArray() {
            // Arrange
            final String[] cityArray = TOP_CITIES.clone();
            final Comparator<String> comparator = new FixedOrderComparator<>(cityArray);

            // Act: Modify the original array after comparator creation
            cityArray[0] = "Brighton";

            // Assert: The comparator's order remains unchanged
            assertSortsCorrectly(TOP_CITIES, comparator);
        }

        @Test
        void shouldDefensivelyCopyInputList() {
            // Arrange
            final List<String> cityList = new LinkedList<>(Arrays.asList(TOP_CITIES));
            final Comparator<String> comparator = new FixedOrderComparator<>(cityList);

            // Act: Modify the original list after comparator creation
            cityList.set(0, "Brighton");

            // Assert: The comparator's order remains unchanged
            assertSortsCorrectly(TOP_CITIES, comparator);
        }
    }

    @Nested
    @DisplayName("Modification")
    class ModificationTests {

        @Test
        void shouldPreserveOrderWhenAddingItemsSequentially() {
            // Arrange
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>();

            // Act
            for (final String city : TOP_CITIES) {
                comparator.add(city);
            }

            // Assert
            assertSortsCorrectly(TOP_CITIES, comparator);
        }

        @Test
        void shouldTreatItemsAddedAsEqualAsEquivalent() {
            // Arrange
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            final String existingCity = "New York";
            final String newCity = "Minneapolis";

            // Act
            comparator.addAsEqual(existingCity, newCity);

            // Assert
            assertEquals(0, comparator.compare(existingCity, newCity), "New York and Minneapolis should be equal");
            assertTrue(comparator.compare("Tokyo", newCity) < 0, "An earlier item (Tokyo) should come before the new city");
            assertTrue(comparator.compare("Shanghai", newCity) > 0, "A later item (Shanghai) should come after the new city");
        }
    }

    @Nested
    @DisplayName("Locking Behavior")
    class LockingBehaviorTests {

        @Test
        void shouldBeUnlockedBeforeFirstCompare() {
            // Arrange
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            // Act & Assert
            assertFalse(comparator.isLocked(), "Comparator should not be locked before first use");
        }

        @Test
        void shouldBeLockedAfterFirstCompare() {
            // Arrange
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            // Act
            comparator.compare("New York", "Tokyo");
            // Assert
            assertTrue(comparator.isLocked(), "Comparator should be locked after first use");
        }

        @Test
        void shouldThrowExceptionWhenAddingToLockedComparator() {
            // Arrange
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            comparator.compare("New York", "Tokyo"); // Lock the comparator

            // Act & Assert
            assertThrows(UnsupportedOperationException.class, () -> comparator.add("Minneapolis"));
        }

        @Test
        void shouldThrowExceptionWhenAddingAsEqualToLockedComparator() {
            // Arrange
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            comparator.compare("New York", "Tokyo"); // Lock the comparator

            // Act & Assert
            assertThrows(UnsupportedOperationException.class, () -> comparator.addAsEqual("New York", "Minneapolis"));
        }
    }

    @Nested
    @DisplayName("Unknown Object Behavior")
    class UnknownObjectBehaviorTests {

        private static final String UNKNOWN_CITY_1 = "Minneapolis";
        private static final String UNKNOWN_CITY_2 = "St Paul";
        private static final String KNOWN_CITY = "New York";

        @Test
        void shouldThrowExceptionForUnknownObjectByDefault() {
            // Arrange
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);

            // Assert default behavior
            assertEquals(FixedOrderComparator.UnknownObjectBehavior.EXCEPTION, comparator.getUnknownObjectBehavior());

            // Act & Assert
            assertThrows(IllegalArgumentException.class, () -> comparator.compare(KNOWN_CITY, UNKNOWN_CITY_1));
            assertThrows(IllegalArgumentException.class, () -> comparator.compare(UNKNOWN_CITY_1, KNOWN_CITY));
        }

        @Test
        void shouldPlaceUnknownObjectsBeforeKnownObjectsWhenSetToBefore() {
            // Arrange
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);

            // Assert comparison results
            assertTrue(comparator.compare(UNKNOWN_CITY_1, KNOWN_CITY) < 0, "Unknown should be less than known");
            assertTrue(comparator.compare(KNOWN_CITY, UNKNOWN_CITY_1) > 0, "Known should be greater than unknown");
            assertEquals(0, comparator.compare(UNKNOWN_CITY_1, UNKNOWN_CITY_2), "Two unknowns should be equal");

            // Assert sorting order
            final String[] expectedOrder = ArrayUtils.add(TOP_CITIES, 0, UNKNOWN_CITY_1);
            assertSortsCorrectly(expectedOrder, comparator);
        }

        @Test
        void shouldPlaceUnknownObjectsAfterKnownObjectsWhenSetToAfter() {
            // Arrange
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
            comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);

            // Assert comparison results
            assertTrue(comparator.compare(UNKNOWN_CITY_1, KNOWN_CITY) > 0, "Unknown should be greater than known");
            assertTrue(comparator.compare(KNOWN_CITY, UNKNOWN_CITY_1) < 0, "Known should be less than unknown");
            assertEquals(0, comparator.compare(UNKNOWN_CITY_1, UNKNOWN_CITY_2), "Two unknowns should be equal");

            // Assert sorting order
            final String[] expectedOrder = ArrayUtils.add(TOP_CITIES, UNKNOWN_CITY_1);
            assertSortsCorrectly(expectedOrder, comparator);
        }
    }

    @Nested
    @DisplayName("equals() Method")
    class EqualsTests {

        @Test
        void shouldReturnTrueForSameObject() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertTrue(comparator.equals(comparator));
        }

        @Test
        void shouldReturnTrueForEqualEmptyComparators() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        void shouldReturnTrueForTwoEqualAndLockedComparators() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);
            comparator1.compare(1, 2);
            comparator2.compare(1, 2);
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        void shouldReturnFalseWhenComparedWithNull() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(null));
        }

        @Test
        void shouldReturnFalseWhenComparedWithDifferentObjectType() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(new Object()));
        }

        @Test
        void shouldReturnFalseForComparatorsWithDifferentItems() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(2, 3, 4);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldReturnFalseForComparatorsWithDifferentDuplicateItems() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3, 3);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldReturnFalseForComparatorsWithDifferentUnknownObjectBehavior() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            comparator1.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            comparator2.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldReturnFalseWhenOneComparatorIsLockedAndOtherIsNot() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);
            comparator2.compare(1, 2); // lock comparator2
            assertFalse(comparator1.equals(comparator2));
        }
    }

    // Helper Methods
    //-----------------------------------------------------------------------

    /**
     * Asserts that the given comparator correctly sorts a shuffled version of the
     * expected order.
     *
     * @param expectedOrder The array of objects in the correct order.
     * @param comparator    The comparator to test.
     */
    private void assertSortsCorrectly(final String[] expectedOrder, final Comparator<String> comparator) {
        // Arrange: Create a shuffled version of the expected order
        final List<String> listToSort = new ArrayList<>(Arrays.asList(expectedOrder));
        Collections.shuffle(listToSort);
        final String[] toSort = listToSort.toArray(new String[0]);

        // To be robust, ensure the shuffle actually changed the order,
        // which is highly likely for non-trivial arrays but not guaranteed.
        if (expectedOrder.length > 1) {
            assertNotEquals(Arrays.asList(expectedOrder), listToSort, "Test setup failed: array was not shuffled");
        }

        // Act: Sort the shuffled array using the comparator
        Arrays.sort(toSort, comparator);

        // Assert: The sorted array should match the expected order
        assertArrayEquals(expectedOrder, toSort);
    }

    // AbstractComparatorTest Implementation
    //-----------------------------------------------------------------------

    @Override
    public Comparator<String> makeObject() {
        return new FixedOrderComparator<>(TOP_CITIES);
    }

    @Override
    public List<String> getComparableObjectsOrdered() {
        return Arrays.asList(TOP_CITIES);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }
}