package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FixedOrderComparator}.
 */
public class FixedOrderComparatorTest extends AbstractComparatorTest<String> {

    /**
     * Top cities of the world, by population including metro areas.
     * Used as the basis for the fixed order in tests.
     */
    private static final String[] TOP_CITIES = {
        "Tokyo", "Mexico City", "Mumbai", "Sao Paulo", "New York",
        "Shanghai", "Lagos", "Los Angeles", "Calcutta", "Buenos Aires"
    };

    //--- Test Fixture Methods ------------------------------------------------

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

    //--- Test Cases ----------------------------------------------------------

    @Test
    void shouldSortCorrectlyWhenCreatedFromList() {
        final List<String> cityList = Arrays.asList(TOP_CITIES);
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(cityList);
        assertSortsArrayToPrescribedOrder(TOP_CITIES, comparator);
    }

    @Test
    void shouldNotBeAffectedByChangesToOriginalList() {
        // Setup: create a mutable list and a comparator from it
        final List<String> cityList = new LinkedList<>(Arrays.asList(TOP_CITIES));
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(cityList);

        // Action: modify the original list after the comparator has been created
        cityList.set(0, "Brighton");
        cityList.add("London");

        // Assertion: the comparator's behavior remains unchanged and sorts to the original order,
        // proving it made a defensive copy.
        assertSortsArrayToPrescribedOrder(TOP_CITIES, comparator);
    }

    /**
     * Tests for the equals() and hashCode() methods.
     */
    @Nested
    @DisplayName("Equals and HashCode")
    class EqualsTests {

        @Test
        void shouldBeEqualToItself() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertTrue(comparator.equals(comparator));
        }

        @Test
        void shouldNotBeEqualToNull() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(null));
        }

        @Test
        void shouldNotBeEqualToObjectOfDifferentType() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(new Object()));
        }

        @Test
        void shouldBeEqualForTwoEmptyComparators() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        void shouldNotBeEqualForDifferentItems() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(2, 3, 4);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldNotBeEqualWhenOneListContainsDuplicates() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3, 3);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldNotBeEqualForDifferentUnknownObjectBehavior() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            comparator1.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);

            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            comparator2.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);

            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldNotBeEqualWhenOnlyOneIsLocked() {
            // A comparator becomes "locked" after the first comparison.
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);

            // Lock comparator2 by using it
            comparator2.compare(1, 2);

            assertFalse(comparator1.equals(comparator2), "An unlocked comparator should not equal a locked one.");
        }

        @Test
        void shouldBeEqualWhenBothAreLockedWithSameState() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);

            // Lock both comparators
            comparator1.compare(1, 2);
            comparator2.compare(1, 2);

            assertTrue(comparator1.equals(comparator2));
        }
    }

    //--- Helper Methods ------------------------------------------------------

    /**
     * Shuffles an array based on the given order and asserts that the comparator
     * can sort it back to that original order.
     *
     * @param prescribedOrder The expected order of objects after sorting.
     * @param comparator      The comparator to test.
     */
    private void assertSortsArrayToPrescribedOrder(final String[] prescribedOrder, final Comparator<String> comparator) {
        final List<String> shuffledList = new ArrayList<>(Arrays.asList(prescribedOrder));
        Collections.shuffle(shuffledList);

        final String[] actualOrder = shuffledList.toArray(new String[0]);
        Arrays.sort(actualOrder, comparator);

        assertArrayEquals(prescribedOrder, actualOrder,
            "Comparator should sort the shuffled array back to the prescribed fixed order.");
    }
}