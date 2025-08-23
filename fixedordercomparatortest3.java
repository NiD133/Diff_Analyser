package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.lang3.ArrayUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FixedOrderComparator}.
 */
class FixedOrderComparatorTest extends AbstractComparatorTest<String> {

    /**
     * Top cities of the world, by population including metro areas. Used as the basis for the fixed order.
     */
    private static final String[] TOP_CITIES = {
        "Tokyo", "Mexico City", "Mumbai", "Sao Paulo", "New York",
        "Shanghai", "Lagos", "Los Angeles", "Calcutta", "Buenos Aires"
    };

    private FixedOrderComparator<String> comparator;

    @BeforeEach
    void setUp() {
        comparator = new FixedOrderComparator<>(TOP_CITIES);
    }

    //--- Overrides for AbstractComparatorTest ---

    @Override
    public Comparator<String> makeObject() {
        // Return a new instance for the abstract tests
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

    //--- Specific FixedOrderComparator Tests ---

    @Test
    void shouldSortCorrectlyWhenItemsAreAddedIndividually() {
        final FixedOrderComparator<String> emptyComparator = new FixedOrderComparator<>();
        for (final String city : TOP_CITIES) {
            emptyComparator.add(city);
        }
        assertComparatorSortsToExpectedOrder(TOP_CITIES, emptyComparator);
    }

    /**
     * A helper assertion that shuffles an array, sorts it using the given comparator,
     * and asserts that the result matches the original expected order.
     *
     * @param expectedOrder The array in the order the comparator should produce.
     * @param comparator    The comparator to test.
     */
    private void assertComparatorSortsToExpectedOrder(final String[] expectedOrder, final Comparator<String> comparator) {
        final String[] itemsToSort = expectedOrder.clone();
        ArrayUtils.shuffle(itemsToSort);

        // The actual test: sort the shuffled array and verify it matches the expected order.
        Arrays.sort(itemsToSort, comparator);

        assertArrayEquals(expectedOrder, itemsToSort, "Comparator should restore the original fixed order after sorting a shuffled array.");
    }

    @Nested
    class EqualsContract {

        @Test
        void shouldBeEqualToItself() {
            final FixedOrderComparator<Integer> comp = new FixedOrderComparator<>();
            assertTrue(comp.equals(comp));
        }

        @Test
        void shouldBeEqualWhenBothAreEmptyAndHaveSameBehavior() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        void shouldBeEqualWhenBothAreLockedWithSameState() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);
            // Lock both comparators by using them
            comparator1.compare(1, 2);
            comparator2.compare(1, 2);
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        void shouldBeUnequalWhenComparedToNull() {
            final FixedOrderComparator<Integer> comp = new FixedOrderComparator<>();
            assertFalse(comp.equals(null));
        }

        @Test
        void shouldBeUnequalWhenComparedToDifferentType() {
            final FixedOrderComparator<Integer> comp = new FixedOrderComparator<>();
            assertFalse(comp.equals(new Object()));
        }

        @Test
        void shouldBeUnequalWhenConstructedWithDifferentItems() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(2, 3, 4);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldBeUnequalWhenOneHasDuplicatesAndOtherDoesNot() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3, 3);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldBeUnequalWhenUnknownObjectBehaviorsDiffer() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            comparator1.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            comparator2.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldBeUnequalWhenOneIsLockedAndOtherIsNot() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);
            // Lock only one comparator
            comparator2.compare(1, 2);
            assertFalse(comparator1.equals(comparator2));
        }
    }
}