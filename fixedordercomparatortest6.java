package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("FixedOrderComparator")
class FixedOrderComparatorTest extends AbstractComparatorTest<String> {

    /**
     * Top cities of the world, by population including metro areas.
     */
    private static final String[] TOP_CITIES = {
        "Tokyo", "Mexico City", "Mumbai", "Sao Paulo", "New York",
        "Shanghai", "Lagos", "Los Angeles", "Calcutta", "Buenos Aires"
    };

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

    /**
     * Shuffles a copy of the expected array, sorts it with the given comparator,
     * and asserts that the result matches the expected order.
     */
    private void assertComparatorSortsToExpectedOrder(final String[] expectedOrder, final Comparator<String> comparator) {
        final List<String> shuffledList = new ArrayList<>(Arrays.asList(expectedOrder));
        Collections.shuffle(shuffledList);

        final String[] actualOrder = shuffledList.toArray(new String[0]);
        Arrays.sort(actualOrder, comparator);

        assertArrayEquals(expectedOrder, actualOrder, "Comparator should sort the shuffled array back to the fixed order.");
    }

    @Nested
    @DisplayName("Unknown Object Behavior")
    class UnknownObjectBehaviorTests {

        private final String knownObject = "New York";
        private final String unknownObject1 = "Minneapolis";
        private final String unknownObject2 = "St Paul";
        private FixedOrderComparator<String> comparator;

        @BeforeEach
        void setUp() {
            comparator = new FixedOrderComparator<>(TOP_CITIES);
        }

        @Test
        @DisplayName("by default, throws IllegalArgumentException for unknown objects")
        void defaultBehaviorThrowsExceptionForUnknownObjects() {
            assertEquals(FixedOrderComparator.UnknownObjectBehavior.EXCEPTION, comparator.getUnknownObjectBehavior());

            assertThrows(IllegalArgumentException.class, () -> comparator.compare(knownObject, unknownObject1));
            assertThrows(IllegalArgumentException.class, () -> comparator.compare(unknownObject1, knownObject));
        }

        @Test
        @DisplayName("when set to BEFORE, places unknown objects first")
        void withBeforeBehaviorPlacesUnknownsFirst() {
            comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
            assertEquals(FixedOrderComparator.UnknownObjectBehavior.BEFORE, comparator.getUnknownObjectBehavior());

            assertEquals(-1, comparator.compare(unknownObject1, knownObject), "Unknown should come before known");
            assertEquals(1, comparator.compare(knownObject, unknownObject1), "Known should come after unknown");
            assertEquals(0, comparator.compare(unknownObject1, unknownObject2), "Two unknowns should be equal");

            final String[] expectedOrder = ArrayUtils.insert(0, TOP_CITIES, unknownObject1);
            assertComparatorSortsToExpectedOrder(expectedOrder, comparator);
        }

        @Test
        @DisplayName("when set to AFTER, places unknown objects last")
        void withAfterBehaviorPlacesUnknownsLast() {
            comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
            assertEquals(FixedOrderComparator.UnknownObjectBehavior.AFTER, comparator.getUnknownObjectBehavior());

            assertEquals(1, comparator.compare(unknownObject1, knownObject), "Unknown should come after known");
            assertEquals(-1, comparator.compare(knownObject, unknownObject1), "Known should come before unknown");
            assertEquals(0, comparator.compare(unknownObject1, unknownObject2), "Two unknowns should be equal");

            final String[] expectedOrder = ArrayUtils.add(TOP_CITIES, unknownObject1);
            assertComparatorSortsToExpectedOrder(expectedOrder, comparator);
        }
    }

    @Nested
    @DisplayName("equals() contract")
    class EqualsContract {

        @Test
        @DisplayName("should be true for the same instance")
        void isTrueForSameInstance() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertTrue(comparator.equals(comparator));
        }

        @Test
        @DisplayName("should be true for two empty, unlocked comparators")
        void isTrueForTwoEmptyUnlockedComparators() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        @DisplayName("should be true for two identical, locked comparators")
        void isTrueForTwoIdenticalLockedComparators() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);
            comparator1.compare(1, 2); // lock comparator1
            comparator2.compare(1, 2); // lock comparator2
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        @DisplayName("should be false when compared to null")
        void isFalseForNull() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(null));
        }

        @Test
        @DisplayName("should be false when compared to an object of a different type")
        void isFalseForDifferentType() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(new Object()));
        }

        @Test
        @DisplayName("should be false for comparators with different items")
        void isFalseForDifferentItems() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(2, 3, 4);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        @DisplayName("should be false for comparators with different item counts (due to duplicates)")
        void isFalseForDifferentItemCounts() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3, 3);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        @DisplayName("should be false for comparators with different unknown object behaviors")
        void isFalseForDifferentUnknownObjectBehaviors() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            comparator1.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            comparator2.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        @DisplayName("should be false when only one comparator is locked")
        void isFalseWhenOneIsLocked() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);
            comparator2.compare(1, 2); // lock comparator2
            assertFalse(comparator1.equals(comparator2), "Unlocked comparator should not equal a locked one.");
        }
    }
}