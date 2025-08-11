package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.IntPredicate;

import org.apache.commons.collections4.IteratorUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ZippingIterator}.
 * 
 * The tests aim to clearly demonstrate the behavior of ZippingIterator:
 * - When given one iterator, it yields the same sequence.
 * - When given multiple iterators, it yields elements in round-robin order.
 * - Removal delegates to the iterator that provided the last returned element.
 */
class ZippingIteratorTest extends AbstractIteratorTest<Integer> {

    private static final int N = 20; // number of integers used to build evens/odds (0..19)

    private List<Integer> evens;
    private List<Integer> odds;
    private List<Integer> fib;

    @Override
    public ZippingIterator<Integer> makeEmptyIterator() {
        return new ZippingIterator<>(IteratorUtils.<Integer>emptyIterator());
    }

    @Override
    public ZippingIterator<Integer> makeObject() {
        return new ZippingIterator<>(evens.iterator(), odds.iterator(), fib.iterator());
    }

    @BeforeEach
    void setUp() {
        evens = buildRange(0, N, 2); // 0, 2, 4, ..., 18
        odds  = buildRange(1, N, 2); // 1, 3, 5, ..., 19
        fib   = Arrays.asList(1, 1, 2, 3, 5, 8, 13, 21);
    }

    // ------------------------------
    // Iteration behavior
    // ------------------------------

    @Test
    void singleIterator_yieldsSameOrder() {
        final ZippingIterator<Integer> iter = new ZippingIterator<>(evens.iterator());

        final List<Integer> actual = drain(iter);
        assertEquals(evens, actual, "Zipping a single iterator should preserve its order");

        assertFalse(iter.hasNext(), "Iterator should be exhausted");
    }

    @Test
    void twoIterators_sameIterator_roundRobinAlternates() {
        final ZippingIterator<Integer> iter = new ZippingIterator<>(evens.iterator(), evens.iterator());

        final List<Integer> expected = duplicateEach(evens);
        final List<Integer> actual = drain(iter);

        assertEquals(expected, actual, "Elements should alternate from the two identical iterators");
        assertFalse(iter.hasNext(), "Iterator should be exhausted");
    }

    @Test
    void twoIterators_evenThenOdd_produces0Through19() {
        final ZippingIterator<Integer> iter = new ZippingIterator<>(evens.iterator(), odds.iterator());

        final List<Integer> expected = buildRange(0, N, 1);
        final List<Integer> actual = drain(iter);

        assertEquals(expected, actual, "Zipping evens then odds should yield 0..19 in order");
        assertFalse(iter.hasNext(), "Iterator should be exhausted");
    }

    @Test
    void twoIterators_oddThenEven_alternateStartingWithOdd() {
        final ZippingIterator<Integer> iter = new ZippingIterator<>(odds.iterator(), evens.iterator());

        final List<Integer> expected = alternate(odds, evens);
        final List<Integer> actual = drain(iter);

        assertEquals(expected, actual, "Zipping odds then evens should alternate [1,0,3,2,...,19,18]");
        assertFalse(iter.hasNext(), "Iterator should be exhausted");
    }

    @Test
    void threeIterators_fibEvenOdd_roundRobinUntilAllExhausted() {
        final ZippingIterator<Integer> iter = new ZippingIterator<>(fib.iterator(), evens.iterator(), odds.iterator());

        final List<Integer> expected = roundRobinOf(fib, evens, odds);
        final List<Integer> actual = drain(iter);

        assertEquals(expected, actual, "Round-robin across [fib, evens, odds] until all are exhausted");
        assertFalse(iter.hasNext(), "Iterator should be exhausted");
    }

    // ------------------------------
    // Removal behavior
    // ------------------------------

    @Test
    void removeFromTwoIterators_removesFromCorrectSource() {
        final ZippingIterator<Integer> iter = new ZippingIterator<>(evens.iterator(), odds.iterator());

        final int initialTotal = evens.size() + odds.size();
        final int removed = removeMatchingAndCount(iter, v -> v % 4 == 0 || v % 3 == 0);

        assertEquals(initialTotal - removed, evens.size() + odds.size(),
                "Total size should reflect number of elements removed across both sources");
    }

    @Test
    void removeFromSingleIterator_removesFromThatSource() {
        final ZippingIterator<Integer> iter = new ZippingIterator<>(evens.iterator());

        final int initialSize = evens.size();
        final int removed = removeMatchingAndCount(iter, v -> v % 4 == 0);

        assertEquals(initialSize - removed, evens.size(),
                "Evens list size should decrease by exactly the number of removed elements");
    }

    // ------------------------------
    // Helpers
    // ------------------------------

    private static List<Integer> buildRange(final int startInclusive, final int endExclusive, final int step) {
        final List<Integer> list = new ArrayList<>();
        for (int i = startInclusive; i < endExclusive; i += step) {
            list.add(i);
        }
        return list;
    }

    private static List<Integer> duplicateEach(final List<Integer> source) {
        final List<Integer> out = new ArrayList<>(source.size() * 2);
        for (final Integer value : source) {
            out.add(value);
            out.add(value);
        }
        return out;
    }

    private static List<Integer> alternate(final List<Integer> first, final List<Integer> second) {
        final int size = Math.min(first.size(), second.size());
        final List<Integer> out = new ArrayList<>(size * 2);
        for (int i = 0; i < size; i++) {
            out.add(first.get(i));
            out.add(second.get(i));
        }
        return out;
    }

    /**
     * Builds the round-robin sequence you would expect from a correct ZippingIterator
     * without using ZippingIterator itself (to avoid circular testing).
     */
    @SafeVarargs
    private static List<Integer> roundRobinOf(final List<Integer>... sources) {
        final List<Iterator<Integer>> iterators = new ArrayList<>(sources.length);
        for (final List<Integer> s : sources) {
            iterators.add(s.iterator());
        }

        final List<Integer> out = new ArrayList<>();
        boolean anyEmitted;
        do {
            anyEmitted = false;
            for (final Iterator<Integer> it : iterators) {
                if (it.hasNext()) {
                    out.add(it.next());
                    anyEmitted = true;
                }
            }
        } while (anyEmitted);
        return out;
    }

    private static List<Integer> drain(final Iterator<Integer> iterator) {
        final List<Integer> out = new ArrayList<>();
        while (iterator.hasNext()) {
            out.add(iterator.next());
        }
        return out;
    }

    private static int removeMatchingAndCount(final ZippingIterator<Integer> iterator, final IntPredicate predicate) {
        int removed = 0;
        while (iterator.hasNext()) {
            final int value = iterator.next();
            if (predicate.test(value)) {
                iterator.remove();
                removed++;
            }
        }
        return removed;
    }
}