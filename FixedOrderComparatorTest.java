package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for FixedOrderComparator.
 *
 * The tests favor clarity:
 * - Small, focused test methods
 * - Helper methods for common setup and assertions
 * - Descriptive variable names
 */
class FixedOrderComparatorTest extends AbstractComparatorTest<String> {

    /**
     * Top cities of the world, by population including metro areas.
     * These provide a stable, unique, non-sorted domain for testing.
     */
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

    private static final String CITY_MINNEAPOLIS = "Minneapolis";
    private static final String CITY_ST_PAUL = "St Paul";

    // ---------------------------------------------------------------------
    // Utilities
    // ---------------------------------------------------------------------

    /**
     * Shuffles a copy of expectedOrder until its order changes, then sorts it using the
     * provided comparator and asserts that the sorted array equals expectedOrder.
     */
    private void assertComparatorRestoresOrder(final String[] expectedOrder, final Comparator<String> comparator) {
        final String[] shuffled = expectedOrder.clone();

        if (shuffled.length <= 1) {
            assertArrayEquals(expectedOrder, shuffled);
            return;
        }

        // Shuffle until the order changes (extremely likely in 1 attempt).
        final Random random = new Random();
        for (int attempts = 0; attempts < 10; attempts++) {
            Collections.shuffle(Arrays.asList(shuffled), random);
            if (!Arrays.equals(expectedOrder, shuffled)) {
                break;
            }
        }

        Arrays.sort(shuffled, comparator);
        assertArrayEquals(expectedOrder, shuffled, "Sorted result must match the fixed order");
    }

    private FixedOrderComparator<String> newComparatorWithTopCities() {
        return new FixedOrderComparator<>(TOP_CITIES);
    }

    private FixedOrderComparator<Integer> newComparator(final Integer... values) {
        return new FixedOrderComparator<>(values);
    }

    private void lockByComparingAnyTwoKnownValues(final FixedOrderComparator<Integer> comparator) {
        comparator.compare(1, 2); // triggers locking
    }

    // ---------------------------------------------------------------------
    // AbstractComparatorTest hooks
    // ---------------------------------------------------------------------

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
        return newComparatorWithTopCities();
    }

//  // Uncomment to (re-)generate serialized form
//  void testCreate() throws Exception {
//      writeExternalFormToDisk((java.io.Serializable) makeObject(), "src/test/resources/data/test/FixedOrderComparator.version4.obj");
//  }

    // ---------------------------------------------------------------------
    // Constructor behavior
    // ---------------------------------------------------------------------

    @Test
    void arrayConstructorCopiesSourceAndRespectsGivenOrder() {
        final String[] expectedOrder = TOP_CITIES.clone();
        final String[] source = TOP_CITIES.clone();

        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(source);
        assertComparatorRestoresOrder(expectedOrder, comparator);

        // Mutating the source after construction must have no effect on the comparator.
        source[0] = "Brighton";
        assertComparatorRestoresOrder(expectedOrder, comparator);
    }

    @Test
    void listConstructorCopiesSourceAndRespectsGivenOrder() {
        final String[] expectedOrder = TOP_CITIES.clone();
        final List<String> source = new LinkedList<>(Arrays.asList(TOP_CITIES));

        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(source);
        assertComparatorRestoresOrder(expectedOrder, comparator);

        // Mutating the source after construction must have no effect on the comparator.
        source.set(0, "Brighton");
        assertComparatorRestoresOrder(expectedOrder, comparator);
    }

    @Test
    void addAddsItemsInGivenOrder() {
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>();
        for (final String city : TOP_CITIES) {
            comparator.add(city);
        }
        assertComparatorRestoresOrder(TOP_CITIES.clone(), comparator);
    }

    // ---------------------------------------------------------------------
    // Mutation methods
    // ---------------------------------------------------------------------

    @Test
    void addAsEqualTreatsNewItemAsEqualToExistingItem() {
        final FixedOrderComparator<String> comparator = newComparatorWithTopCities();

        comparator.addAsEqual("New York", CITY_MINNEAPOLIS);

        assertEquals(0, comparator.compare("New York", CITY_MINNEAPOLIS));
        assertEquals(-1, comparator.compare("Tokyo", CITY_MINNEAPOLIS));
        assertEquals(1, comparator.compare("Shanghai", CITY_MINNEAPOLIS));
    }

    @Test
    void locksAfterFirstCompareAndRejectsMutations() {
        final FixedOrderComparator<String> comparator = newComparatorWithTopCities();
        assertFalse(comparator.isLocked(), "Comparator should be unlocked before first comparison");

        comparator.compare("New York", "Tokyo");
        assertTrue(comparator.isLocked(), "Comparator should be locked after first comparison");

        assertThrows(UnsupportedOperationException.class, () -> comparator.add(CITY_MINNEAPOLIS),
            "Adding after lock should throw UnsupportedOperationException");
        assertThrows(UnsupportedOperationException.class, () -> comparator.addAsEqual("New York", CITY_MINNEAPOLIS),
            "addAsEqual after lock should throw UnsupportedOperationException");
    }

    // ---------------------------------------------------------------------
    // Unknown object behavior
    // ---------------------------------------------------------------------

    @Test
    void unknownBehaviorDefaultsToExceptionAndThrowsForUnknowns() {
        final FixedOrderComparator<String> comparator = newComparatorWithTopCities();
        assertEquals(FixedOrderComparator.UnknownObjectBehavior.EXCEPTION, comparator.getUnknownObjectBehavior());

        assertThrows(IllegalArgumentException.class,
            () -> comparator.compare("New York", CITY_MINNEAPOLIS),
            "Comparing known vs unknown should throw in EXCEPTION mode");

        assertThrows(IllegalArgumentException.class,
            () -> comparator.compare(CITY_MINNEAPOLIS, "New York"),
            "Comparing unknown vs known should throw in EXCEPTION mode");
    }

    @Test
    void unknownBeforePlacesUnknownBeforeAllKnownAndTreatsUnknownsAsEqual() {
        final FixedOrderComparator<String> comparator = newComparatorWithTopCities();
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
        assertEquals(FixedOrderComparator.UnknownObjectBehavior.BEFORE, comparator.getUnknownObjectBehavior());

        final LinkedList<String> expected = new LinkedList<>(Arrays.asList(TOP_CITIES));
        expected.addFirst(CITY_MINNEAPOLIS);
        assertComparatorRestoresOrder(expected.toArray(new String[0]), comparator);

        assertEquals(-1, comparator.compare(CITY_MINNEAPOLIS, "New York"));
        assertEquals(1, comparator.compare("New York", CITY_MINNEAPOLIS));
        assertEquals(0, comparator.compare(CITY_MINNEAPOLIS, CITY_ST_PAUL), "Two unknowns compare equal in BEFORE mode");
    }

    @Test
    void unknownAfterPlacesUnknownAfterAllKnownAndTreatsUnknownsAsEqual() {
        final FixedOrderComparator<String> comparator = newComparatorWithTopCities();
        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        assertEquals(FixedOrderComparator.UnknownObjectBehavior.AFTER, comparator.getUnknownObjectBehavior());

        final LinkedList<String> expected = new LinkedList<>(Arrays.asList(TOP_CITIES));
        expected.add(CITY_MINNEAPOLIS);
        assertComparatorRestoresOrder(expected.toArray(new String[0]), comparator);

        assertEquals(1, comparator.compare(CITY_MINNEAPOLIS, "New York"));
        assertEquals(-1, comparator.compare("New York", CITY_MINNEAPOLIS));
        assertEquals(0, comparator.compare(CITY_MINNEAPOLIS, CITY_ST_PAUL), "Two unknowns compare equal in AFTER mode");
    }

    // ---------------------------------------------------------------------
    // equals and related behavior
    // ---------------------------------------------------------------------

    @Nested
    class Equals {

        @Test
        void falseWhenComparedWithNull() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(null));
        }

        @Test
        void falseWhenComparedWithDifferentType() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertNotEquals(new Object(), comparator);
        }

        @Test
        void trueWhenSameInstance() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertTrue(comparator.equals(comparator));
        }

        @Test
        void falseWhenItemsDiffer() {
            final FixedOrderComparator<Integer> c1 = newComparator(1, 2, 3);
            final FixedOrderComparator<Integer> c2 = newComparator(2, 3, 4);
            assertNotEquals(c1, c2);
        }

        @Test
        void falseWhenUnknownObjectBehaviorDiffers() {
            final FixedOrderComparator<Integer> c1 = new FixedOrderComparator<>();
            c1.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);

            final FixedOrderComparator<Integer> c2 = new FixedOrderComparator<>();
            c2.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);

            assertNotEquals(c1, c2);
        }

        @Test
        void falseWhenOneComparatorIsLocked() {
            final FixedOrderComparator<Integer> c1 = newComparator(1, 2, 3);
            final FixedOrderComparator<Integer> c2 = newComparator(1, 2, 3);

            lockByComparingAnyTwoKnownValues(c2);

            assertNotEquals(c1, c2);
        }

        @Test
        void falseWhenOneComparatorHasDuplicateItems() {
            final FixedOrderComparator<Integer> c1 = newComparator(1, 2, 3);
            final FixedOrderComparator<Integer> c2 = newComparator(1, 2, 3, 3);
            assertNotEquals(c1, c2);
        }

        @Test
        void trueWhenBothComparatorsAreLockedWithSameState() {
            final FixedOrderComparator<Integer> c1 = newComparator(1, 2, 3);
            final FixedOrderComparator<Integer> c2 = newComparator(1, 2, 3);

            lockByComparingAnyTwoKnownValues(c1);
            lockByComparingAnyTwoKnownValues(c2);

            assertEquals(c1, c2);
        }

        @Test
        void trueWhenBothAreEmptyAndUnlocked() {
            final FixedOrderComparator<Integer> c1 = new FixedOrderComparator<>();
            final FixedOrderComparator<Integer> c2 = new FixedOrderComparator<>();
            assertEquals(c1, c2);
        }
    }
}