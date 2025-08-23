package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
public class FixedOrderComparatorTest extends AbstractComparatorTest<String> {

    /**
     * Top cities of the world, by population, used as the fixed order for tests.
     */
    private static final String[] TOP_CITIES_IN_ORDER = {
        "Tokyo", "Mexico City", "Mumbai", "Sao Paulo", "New York",
        "Shanghai", "Lagos", "Los Angeles", "Calcutta", "Buenos Aires"
    };

    //--- AbstractComparatorTest implementation --------------------------------

    @Override
    public Comparator<String> makeObject() {
        return new FixedOrderComparator<>(TOP_CITIES_IN_ORDER);
    }

    @Override
    public List<String> getComparableObjectsOrdered() {
        return Arrays.asList(TOP_CITIES_IN_ORDER);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    //--- Method-specific tests ------------------------------------------------

    @Nested
    @DisplayName("addAsEqual()")
    class AddAsEqualTests {

        @Test
        @DisplayName("should make a new item compare as equal to an existing one")
        void addAsEqualMakesNewItemEqualToExisting() {
            // Arrange
            final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES_IN_ORDER);
            final String existingCity = "New York";
            final String newCity = "Minneapolis";

            // Act
            comparator.addAsEqual(existingCity, newCity);

            // Assert
            assertEquals(0, comparator.compare(existingCity, newCity),
                "New item should compare as equal to the existing item");
            assertEquals(-1, comparator.compare("Tokyo", newCity),
                "New item should be ordered relative to other items");
            assertEquals(1, comparator.compare("Shanghai", newCity),
                "New item should be ordered relative to other items");
        }
    }

    @Nested
    @DisplayName("equals() contract")
    class EqualsContract {

        @Test
        @DisplayName("should return true for the same instance")
        void returnsTrueForSameInstance() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertTrue(comparator.equals(comparator));
        }

        @Test
        @DisplayName("should return true for two empty comparators")
        void returnsTrueForTwoEmptyComparators() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        @DisplayName("should return true for two locked comparators with the same order")
        void returnsTrueForTwoIdenticalLockedComparators() {
            // Arrange: Create two identical comparators
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);

            // Act: Lock both comparators by using them
            comparator1.compare(1, 2);
            comparator2.compare(1, 2);

            // Assert
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        @DisplayName("should return false when compared with null")
        void returnsFalseWhenComparedWithNull() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(null));
        }

        @Test
        @DisplayName("should return false when compared with an object of a different type")
        void returnsFalseWhenComparedWithDifferentType() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(new Object()));
        }

        @Test
        @DisplayName("should return false for comparators with different items")
        void returnsFalseForDifferentItems() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(2, 3, 4);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        @DisplayName("should return false for comparators with different item counts")
        void returnsFalseForDifferentItemCounts() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3, 3); // Contains a duplicate
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        @DisplayName("should return false for different UnknownObjectBehavior settings")
        void returnsFalseForDifferentUnknownObjectBehavior() {
            // Arrange
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            comparator1.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);

            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            comparator2.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);

            // Assert
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        @DisplayName("should return false if only one comparator is locked")
        void returnsFalseWhenOneIsLockedAndOtherIsNot() {
            // Arrange: Create two identical comparators
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);

            // Act: Lock only one comparator by using it
            comparator2.compare(1, 2);

            // Assert
            assertFalse(comparator1.equals(comparator2));
        }
    }
}