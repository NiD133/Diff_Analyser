package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FixedOrderComparator}.
 */
class FixedOrderComparatorTest extends AbstractComparatorTest<String> {

    /**
     * Top cities of the world, by population including metro areas.
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

    //--- AbstractComparatorTest implementation --------------------------------

    @Override
    public Comparator<String> makeObject() {
        // The setUp() method already creates a comparator instance for other tests.
        // This method fulfills the contract of the abstract parent class.
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

    //--- Tests for Constructors ---------------------------------------------

    @Test
    @DisplayName("Constructor with array creates a working comparator")
    void constructorWithArrayCreatesWorkingComparator() {
        assertSortsCorrectly(TOP_CITIES, comparator);
    }

    @Test
    @DisplayName("Constructor with array makes a defensive copy of the input")
    void constructorWithArrayMakesDefensiveCopy() {
        // 1. Setup: Create a mutable array and a comparator from it.
        final String[] mutableArray = TOP_CITIES.clone();
        final FixedOrderComparator<String> defensiveComparator = new FixedOrderComparator<>(mutableArray);

        // 2. Act: Modify the original array after creating the comparator.
        mutableArray[0] = "New City A";
        mutableArray[1] = "New City B";

        // 3. Assert: The comparator's behavior is unchanged, proving it uses a copy.
        assertSortsCorrectly(TOP_CITIES, defensiveComparator);
    }

    //--- Tests for equals() and hashCode() ------------------------------------

    @Nested
    @DisplayName("equals() and hashCode()")
    class EqualsAndHashCode {

        @Test
        @DisplayName("An object should be equal to itself")
        void isEqualToItself() {
            final FixedOrderComparator<Integer> comp = new FixedOrderComparator<>();
            assertTrue(comp.equals(comp));
        }

        @Test
        @DisplayName("Should be unequal to null")
        void isUnequalToNull() {
            final FixedOrderComparator<Integer> comp = new FixedOrderComparator<>();
            assertFalse(comp.equals(null));
        }

        @Test
        @DisplayName("Should be unequal to an object of a different type")
        void isUnequalToDifferentType() {
            final FixedOrderComparator<Integer> comp = new FixedOrderComparator<>();
            assertFalse(comp.equals(new Object()));
        }

        @Test
        @DisplayName("Two empty comparators should be equal")
        void emptyComparatorsAreEqual() {
            final FixedOrderComparator<Integer> comp1 = new FixedOrderComparator<>();
            final FixedOrderComparator<Integer> comp2 = new FixedOrderComparator<>();
            assertEquals(comp1, comp2);
            assertEquals(comp1.hashCode(), comp2.hashCode());
        }

        @Test
        @DisplayName("Comparators with different items should be unequal")
        void areUnequalForDifferentItems() {
            final FixedOrderComparator<Integer> comp1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comp2 = new FixedOrderComparator<>(1, 2, 4);
            assertNotEquals(comp1, comp2);
        }

        @Test
        @DisplayName("Comparators with different unknown object behaviors should be unequal")
        void areUnequalForDifferentUnknownObjectBehavior() {
            final FixedOrderComparator<Integer> comp1 = new FixedOrderComparator<>();
            comp1.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);

            final FixedOrderComparator<Integer> comp2 = new FixedOrderComparator<>();
            comp2.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);

            assertNotEquals(comp1, comp2);
        }

        @Test
        @DisplayName("A locked comparator should be unequal to an unlocked one")
        void isUnequalWhenOneIsLocked() {
            final FixedOrderComparator<Integer> comp1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comp2 = new FixedOrderComparator<>(1, 2, 3);

            // Lock one comparator by using it
            comp2.compare(1, 2);

            assertNotEquals(comp1, comp2);
        }

        @Test
        @DisplayName("Two comparators locked in the same state should be equal")
        void areEqualWhenBothLocked() {
            final FixedOrderComparator<Integer> comp1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comp2 = new FixedOrderComparator<>(1, 2, 3);

            // Lock both comparators
            comp1.compare(1, 2);
            comp2.compare(1, 2);

            assertEquals(comp1, comp2);
            assertEquals(comp1.hashCode(), comp2.hashCode());
        }
    }

    //--- Helper Methods -------------------------------------------------------

    /**
     * Shuffles a copy of the given array, sorts it using the comparator,
     * and asserts that the result matches the original ordered array.
     *
     * @param expectedOrder The array in the expected sorted order.
     * @param comparator    The comparator to test.
     */
    private void assertSortsCorrectly(final String[] expectedOrder, final Comparator<String> comparator) {
        final String[] toSort = expectedOrder.clone();
        final List<String> listToSort = Arrays.asList(toSort);

        // Shuffle the array to ensure we are actually testing the sort algorithm.
        // It's highly unlikely to loop more than once.
        int maxShuffleAttempts = 10;
        while (Arrays.equals(expectedOrder, toSort) && toSort.length > 1 && maxShuffleAttempts-- > 0) {
            Collections.shuffle(listToSort);
        }

        // The real test: sort and make sure the items are in the correct order.
        Arrays.sort(toSort, comparator);

        assertArrayEquals(expectedOrder, toSort, "The comparator did not sort the items into the expected fixed order.");
    }
}