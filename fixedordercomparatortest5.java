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

public class FixedOrderComparatorTestTest5 extends AbstractComparatorTest<String> {

    /**
     * Top cities of the world, by population including metro areas.
     */
    private static final String[] topCities = { "Tokyo", "Mexico City", "Mumbai", "Sao Paulo", "New York", "Shanghai", "Lagos", "Los Angeles", "Calcutta", "Buenos Aires" };

    /**
     * Shuffles the keys and asserts that the comparator sorts them back to
     * their original order.
     */
    private void assertComparatorYieldsOrder(final String[] orderedObjects, final Comparator<String> comparator) {
        final String[] keys = orderedObjects.clone();
        // shuffle until the order changes. It's extremely rare that
        // this requires more than one shuffle.
        boolean isInNewOrder = false;
        final Random rand = new Random();
        while (keys.length > 1 && !isInNewOrder) {
            // shuffle:
            for (int i = keys.length - 1; i > 0; i--) {
                final String swap = keys[i];
                final int j = rand.nextInt(i + 1);
                keys[i] = keys[j];
                keys[j] = swap;
            }
            // testShuffle
            for (int i = 0; i < keys.length; i++) {
                if (!orderedObjects[i].equals(keys[i])) {
                    isInNewOrder = true;
                    break;
                }
            }
        }
        // The real test: sort and make sure they come out right.
        Arrays.sort(keys, comparator);
        for (int i = 0; i < orderedObjects.length; i++) {
            assertEquals(orderedObjects[i], keys[i]);
        }
    }

    @Override
    public List<String> getComparableObjectsOrdered() {
        return Arrays.asList(topCities);
    }

    @Override
    public String getCompatibilityVersion() {
        return "4";
    }

    @Override
    public Comparator<String> makeObject() {
        return new FixedOrderComparator<>(topCities);
    }

    @Nested
    class Equals {

        @Test
        void expectFalseWhenBothComparatorsWithDifferentItems() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(2, 3, 4);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void expectFalseWhenBothComparatorsWithDifferentUnknownObjectBehavior() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            comparator1.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.BEFORE);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            comparator2.setUnknownObjectBehavior(FixedOrderComparator.UnknownObjectBehavior.AFTER);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void expectFalseWhenFixedOrderComparatorIsComparedWithNull() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(null));
        }

        @Test
        void expectFalseWhenFixedOrderComparatorIsComparedWithOtherObject() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertFalse(comparator.equals(new Object()));
        }

        @Test
        void expectFalseWhenOneComparatorIsLocked() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);
            comparator2.compare(1, 2);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void expectFalseWhenOneComparatorsWithDuplicateItems() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3, 3);
            assertFalse(comparator1.equals(comparator2));
        }

        @Test
        void expectTrueWhenBothComparatorsAreLocked() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>(1, 2, 3);
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>(1, 2, 3);
            comparator1.compare(1, 2);
            comparator2.compare(1, 2);
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        void expectTrueWhenBothComparatorsWithoutAnyItems() {
            final FixedOrderComparator<Integer> comparator1 = new FixedOrderComparator<>();
            final FixedOrderComparator<Integer> comparator2 = new FixedOrderComparator<>();
            assertTrue(comparator1.equals(comparator2));
        }

        @Test
        void expectTrueWhenBothObjectsAreSame() {
            final FixedOrderComparator<Integer> comparator = new FixedOrderComparator<>();
            assertTrue(comparator.equals(comparator));
        }
    }

    /**
     * Tests whether or not updates are disabled after a comparison is made.
     */
    @Test
    void testLock() {
        final FixedOrderComparator<String> comparator = new FixedOrderComparator<>(topCities);
        assertFalse(comparator.isLocked());
        comparator.compare("New York", "Tokyo");
        assertTrue(comparator.isLocked());
        assertThrows(UnsupportedOperationException.class, () -> comparator.add("Minneapolis"), "Should have thrown an UnsupportedOperationException");
        assertThrows(UnsupportedOperationException.class, () -> comparator.addAsEqual("New York", "Minneapolis"), "Should have thrown an UnsupportedOperationException");
    }
}
