package org.apache.commons.collections4.comparators;

import static org.junit.jupiter.api.Assertions.*;

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
 */
class FixedOrderComparatorTest extends AbstractComparatorTest<String> {

    // Constants
    private static final String[] TOP_CITIES = {
        "Tokyo", "Mexico City", "Mumbai", "Sao Paulo", "New York",
        "Shanghai", "Lagos", "Los Angeles", "Calcutta", "Buenos Aires"
    };

    // Nested classes for organizing tests
    @Nested
    class EqualsTests {

        @Test
        void shouldReturnFalseForComparatorsWithDifferentItems() {
            FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(2, 3, 4);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldReturnFalseForComparatorsWithDifferentUnknownObjectBehavior() {
            FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            comparator1.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
            FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            comparator2.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldReturnFalseWhenComparedWithNull() {
            FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(null));
        }

        @Test
        void shouldReturnFalseWhenComparedWithDifferentObjectType() {
            FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(new Object()));
        }

        @Test
        void shouldReturnFalseWhenOneComparatorIsLocked() {
            FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);
            comparator2.compare(1, 2);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldReturnFalseForComparatorsWithDuplicateItems() {
            FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3, 3);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void shouldReturnTrueWhenBothComparatorsAreLocked() {
            FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);
            comparator1.compare(1, 2);
            comparator2.compare(1, 2);
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        void shouldReturnTrueWhenBothComparatorsAreEmpty() {
            FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        void shouldReturnTrueWhenComparedWithItself() {
            FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertTrue(comparator.equals(comparator));
        }
    }

    // Helper method to verify sorting order
    private void verifySortingOrder(final String[] expectedOrder, final Comparator<String> comparator) {
        String[] shuffledKeys = expectedOrder.clone();
        shuffleUntilOrderChanges(shuffledKeys, expectedOrder);

        Arrays.sort(shuffledKeys, comparator);
        assertArrayEquals(expectedOrder, shuffledKeys);
    }

    private void shuffleUntilOrderChanges(String[] keys, String[] originalOrder) {
        Random random = new Random();
        boolean isOrderChanged = false;

        while (keys.length > 1 && !isOrderChanged) {
            for (int i = keys.length - 1; i > 0; i--) {
                int j = random.nextInt(i + 1);
                String temp = keys[i];
                keys[i] = keys[j];
                keys[j] = temp;
            }

            for (int i = 0; i < keys.length; i++) {
                if (!originalOrder[i].equals(keys[i])) {
                    isOrderChanged = true;
                    break;
                }
            }
        }
    }

    // AbstractComparatorTest methods
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

    // Test cases for FixedOrderComparator
    @Test
    void testAddAsEqual() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
        comparator.addAsEqual("New York", "Minneapolis");
        assertEquals(0, comparator.compare("New York", "Minneapolis"));
        assertEquals(-1, comparator.compare("Tokyo", "Minneapolis"));
        assertEquals(1, comparator.compare("Shanghai", "Minneapolis"));
    }

    @Test
    void testArrayConstructor() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES.clone());
        verifySortingOrder(TOP_CITIES, comparator);

        // Verify that changes to the input array don't affect the comparator
        TOP_CITIES[0] = "Brighton";
        verifySortingOrder(TOP_CITIES, comparator);
    }

    @Test
    void testConstructorPlusAdd() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>();
        for (String city : TOP_CITIES) {
            comparator.add(city);
        }
        verifySortingOrder(TOP_CITIES, comparator);
    }

    @Test
    void testListConstructor() {
        List<String> cityList = new LinkedList<>(Arrays.asList(TOP_CITIES));
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(cityList);
        verifySortingOrder(TOP_CITIES, comparator);

        // Verify that changes to the input list don't affect the comparator
        cityList.set(0, "Brighton");
        verifySortingOrder(TOP_CITIES, comparator);
    }

    @Test
    void testLock() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);
        assertFalse(comparator.isLocked());

        comparator.compare("New York", "Tokyo");
        assertTrue(comparator.isLocked());

        assertThrows(UnsupportedOperationException.class, () -> comparator.add("Minneapolis"));
        assertThrows(UnsupportedOperationException.class, () -> comparator.addAsEqual("New York", "Minneapolis"));
    }

    @Test
    void testUnknownObjectBehavior() {
        FixedOrderComparator<String> comparator = new FixedOrderComparator<>(TOP_CITIES);

        assertThrows(IllegalArgumentException.class, () -> comparator.compare("New York", "Minneapolis"));
        assertThrows(IllegalArgumentException.class, () -> comparator.compare("Minneapolis", "New York"));

        assertEquals(FixedOrderComparator.UnknownObjectBehavior.EXCEPTION, comparator.getUnknownObjectBehavior());

        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
        assertEquals(FixedOrderComparator.UnknownObjectBehavior.BEFORE, comparator.getUnknownObjectBehavior());

        LinkedList<String> keys = new LinkedList<>(Arrays.asList(TOP_CITIES));
        keys.addFirst("Minneapolis");
        verifySortingOrder(keys.toArray(ArrayUtils.EMPTY_STRING_ARRAY), comparator);

        assertEquals(-1, comparator.compare("Minneapolis", "New York"));
        assertEquals(1, comparator.compare("New York", "Minneapolis"));
        assertEquals(0, comparator.compare("Minneapolis", "St Paul"));

        comparator.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
        keys.add("Minneapolis");
        verifySortingOrder(keys.toArray(ArrayUtils.EMPTY_STRING_ARRAY), comparator);

        assertEquals(1, comparator.compare("Minneapolis", "New York"));
        assertEquals(-1, comparator.compare("New York", "Minneapolis"));
        assertEquals(0, comparator.compare("Minneapolis", "St Paul"));
    }
}