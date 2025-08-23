package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link FixedOrderComparator}.
 */
@DisplayName("FixedOrderComparator Test")
public class FixedOrderComparatorTest extends AbstractComparatorTest<String> {

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
     * Tests that the comparator becomes locked after the first comparison,
     * preventing any further modifications.
     */
    @Test
    @DisplayName("Comparator is locked after first comparison and prevents modifications")
    void firstCompareShouldLockComparatorAndPreventFurtherModifications() {
        // Arrange
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
        assertFalse(comparator.isLocked(), "Comparator should not be locked before first use");

        // Act: Perform a comparison to trigger the lock.
        comparator.compare("New York", "Tokyo");

        // Assert: The comparator is now locked and modification attempts fail.
        assertTrue(comparator.isLocked(), "Comparator should be locked after first use");
        assertThrows(UnsupportedOperationException.class, () -> comparator.add("Minneapolis"));
        assertThrows(UnsupportedOperationException.class, () -> comparator.addAsEqual("New York", "Minneapolis"));
    }

    /**
     * Contains all tests for the {@link FixedOrderComparator#equals(Object)} method.
     */
    @Nested
    @DisplayName("equals() method")
    class EqualsTest {

        @Test
        @DisplayName("should return true for the same instance")
        void equalsShouldReturnTrueForSameInstance() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertTrue(comparator.equals(comparator));
        }

        @Test
        @DisplayName("should return true for two identical empty comparators")
        void equalsShouldReturnTrueForTwoEmptyComparators() {
            final FixedOrderComparator<Integer> comparatorA = new FixedOrderComparator<>();
            final FixedOrderComparator<Integer> comparatorB = new FixedOrderComparator<>();
            assertTrue(comparatorA.equals(comparatorB));
        }

        @Test
        @DisplayName("should return true for identical comparators that are both locked")
        void equalsShouldReturnTrueForIdenticalAndLockedComparators() {
            final FixedOrderComparator<Integer> comparatorA = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparatorB = new FixedOrderComparator<>(1, 2, 3);

            // Lock both comparators by using them
            comparatorA.compare(1, 2);
            comparatorB.compare(1, 2);

            assertTrue(comparatorA.equals(comparatorB));
        }

        @Test
        @DisplayName("should return false when compared with null")
        void equalsShouldReturnFalseWhenComparedWithNull() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(null));
        }

        @Test
        @DisplayName("should return false when compared with an object of a different type")
        void equalsShouldReturnFalseWhenComparedWithDifferentType() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(new Object()));
        }

        @Test
        @DisplayName("should return false if one comparator is locked and the other is not")
        void equalsShouldReturnFalseIfOnlyOneComparatorIsLocked() {
            final FixedOrderComparator<Integer> comparatorA = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparatorB = new FixedOrderComparator<>(1, 2, 3);

            // Lock only one comparator
            comparatorB.compare(1, 2);

            assertFalse(comparatorA.equals(comparatorB));
        }

        @Test
        @DisplayName("should return false for comparators with different item lists")
        void equalsShouldReturnFalseForDifferentItemLists() {
            final FixedOrderComparator<Integer> comparatorA = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparatorB = new FixedOrderComparator<>(2, 3, 4);
            assertFalse(comparatorA.equals(comparatorB));
        }

        @Test
        @DisplayName("should return false for comparators with different list sizes")
        void equalsShouldReturnFalseForDifferentListSizes() {
            final FixedOrderComparator<Integer> comparatorA = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparatorB = new FixedOrderComparator<>(1, 2, 3, 3);
            assertFalse(comparatorA.equals(comparatorB));
        }

        @Test
        @DisplayName("should return false for different UnknownObjectBehavior settings")
        void equalsShouldReturnFalseForDifferentUnknownObjectBehaviors() {
            final FixedOrderComparator<Integer> comparatorA = new FixedOrderComparator<>();
            comparatorA.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);

            final FixedOrderComparator<Integer> comparatorB = new FixedOrderComparator<>();
            comparatorB.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);

            assertFalse(comparatorA.equals(comparatorB));
        }
    }
}