package org.apache.commons.collections4.bag;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.SortedMap;

import org.junit.Test;

public class TreeBagTest {

    // A simple comparator that can compare any two references (including null)
    // by always returning 0. This makes TreeBag accept null values.
    private static final class AlwaysEqualComparator<T> implements Comparator<T> {
        @Override
        public int compare(final T o1, final T o2) {
            return 0;
        }
    }

    // A type that does not implement Comparable, used to exercise error paths.
    private static final class NonComparable {
        final int id;
        NonComparable(final int id) { this.id = id; }
    }

    @Test
    public void comparator_isReturnedWhenProvided() {
        Comparator<String> cmp = Comparator.naturalOrder();
        TreeBag<String> bag = new TreeBag<>(cmp);

        assertSame("Comparator should be the one supplied", cmp, bag.comparator());
    }

    @Test
    public void comparator_isNullWhenUsingNaturalOrdering_defaultCtor() {
        TreeBag<String> bag = new TreeBag<>();
        assertNull("Natural ordering should report null comparator", bag.comparator());
    }

    @Test
    public void comparator_isNullWhenUsingNaturalOrdering_collectionCtor() {
        Collection<Integer> source = new LinkedList<>();
        TreeBag<Integer> bag = new TreeBag<>(source);

        assertNull("Natural ordering should report null comparator", bag.comparator());
    }

    @Test
    public void add_null_withCustomComparator_isAllowed_andBecomesFirstAndLast() {
        TreeBag<Object> bag = new TreeBag<>(new AlwaysEqualComparator<>());
        assertTrue(bag.add(null));

        assertEquals(1, bag.size());
        assertNull("Only element is null, so first() must be null", bag.first());
        assertNull("Only element is null, so last() must be null", bag.last());
    }

    @Test
    public void first_onEmpty_throwsNoSuchElementException() {
        TreeBag<Integer> bag = new TreeBag<>();
        assertThrows(NoSuchElementException.class, bag::first);
    }

    @Test
    public void last_onEmpty_throwsNoSuchElementException() {
        TreeBag<Integer> bag = new TreeBag<>();
        assertThrows(NoSuchElementException.class, bag::last);
    }

    @Test
    public void getMap_isEmptyForNewBag_thenNotEmptyAfterAdd() {
        TreeBag<Locale.FilteringMode> bag = new TreeBag<>();
        SortedMap<Locale.FilteringMode, AbstractMapBag.MutableInteger> map = bag.getMap();

        assertTrue("New bag should have empty backing map", map.isEmpty());

        bag.add(Locale.FilteringMode.IGNORE_EXTENDED_RANGES);
        assertFalse("Backing map should reflect inserted content", map.isEmpty());
    }

    @Test
    public void add_nonComparable_withNaturalOrdering_throwsIllegalArgumentException() {
        TreeBag<Object> bag = new TreeBag<>();

        assertThrows("Adding a non-Comparable element with natural ordering should fail",
                IllegalArgumentException.class,
                () -> bag.add(new NonComparable(1)));
    }

    @Test
    public void construct_fromIterableContainingNonComparable_throwsIllegalArgumentException() {
        Iterable<NonComparable> iterable = java.util.Collections.singletonList(new NonComparable(42));

        assertThrows("Constructing from iterable of non-Comparable elements should fail",
                IllegalArgumentException.class,
                () -> new TreeBag<>(iterable));
    }

    @Test
    public void construct_fromNullIterable_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new TreeBag<>((Iterable<?>) null));
    }

    @Test
    public void add_incomparableTypesWithNaturalOrdering_throwsClassCastException() {
        // Start with an enum value, then attempt to add a String: they are incomparable.
        TreeBag<Object> bag = new TreeBag<>();
        bag.add(Locale.Category.FORMAT);

        assertThrows(ClassCastException.class, () -> bag.add(""));
    }

    @Test
    public void construct_fromIterable_singleElement_thenFirstReturnsThatElement() {
        EnumSet<Locale.Category> oneElement = EnumSet.of(Locale.Category.FORMAT);

        TreeBag<Locale.Category> bag = new TreeBag<>(oneElement);
        assertEquals(Locale.Category.FORMAT, bag.first());
    }

    @Test
    public void add_null_withNaturalOrdering_throwsNullPointerException() {
        TreeBag<String> bag = new TreeBag<>();
        assertThrows(NullPointerException.class, () -> bag.add(null));
    }
}