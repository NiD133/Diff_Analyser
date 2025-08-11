package org.apache.commons.collections4.bag;

import org.apache.commons.collections4.SortedBag;
import org.apache.commons.collections4.bag.CollectionSortedBag;
import org.apache.commons.collections4.bag.PredicatedSortedBag;
import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.collections4.bag.UnmodifiableSortedBag;
import org.apache.commons.collections4.functors.EqualPredicate;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Focused, readable tests for CollectionSortedBag.
 * These tests avoid mocks and framework scaffolding, and use simple,
 * descriptive names with minimal setup.
 */
public class CollectionSortedBagTest {

    // Factory method

    @Test
    public void factoryCreatesLiveDecoratorOverGivenBag() {
        SortedBag<String> underlying = new TreeBag<>();
        SortedBag<String> decorated = CollectionSortedBag.collectionSortedBag(underlying);

        decorated.add("x", 2);

        assertEquals(2, underlying.getCount("x"));
        assertEquals(2, decorated.getCount("x"));
    }

    @Test(expected = NullPointerException.class)
    public void factoryRejectsNull() {
        CollectionSortedBag.collectionSortedBag(null);
    }

    // Constructor

    @Test(expected = NullPointerException.class)
    public void constructorRejectsNull() {
        new CollectionSortedBag<>(null);
    }

    // add / add(count)

    @Test
    public void addIncrementsCount() {
        SortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        assertTrue(bag.add("a"));
        assertEquals(1, bag.getCount("a"));

        assertTrue(bag.add("a", 3));
        assertEquals(4, bag.getCount("a"));
        assertEquals(4, bag.size());
    }

    @Test(expected = NullPointerException.class)
    public void addNullRejectedByTreeBag() {
        SortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        bag.add(null);
    }

    @Test(expected = NullPointerException.class)
    public void addCountNullRejectedByTreeBag() {
        SortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        bag.add(null, 2);
    }

    @Test
    public void addAllFromCollectionAddsAllElements() {
        SortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        boolean changed = bag.addAll(Arrays.asList("a", "b", "a"));

        assertTrue(changed);
        assertEquals(3, bag.size());
        assertEquals(2, bag.getCount("a"));
        assertEquals(1, bag.getCount("b"));
    }

    @Test
    public void addAllWithSelfIsNoOp() {
        CollectionSortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        bag.add("x");

        // Adding all elements from itself should not change it
        boolean changed = bag.addAll(bag);
        assertFalse(changed);
        assertEquals(1, bag.getCount("x"));
    }

    // containsAll

    @Test
    public void containsAllReturnsTrueWhenAllPresent() {
        SortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        bag.add("a", 2);
        bag.add("b");

        assertTrue(bag.containsAll(Arrays.asList("a", "b")));
        assertTrue(bag.containsAll(Collections.emptyList()));
    }

    @Test
    public void containsAllReturnsFalseWhenMissing() {
        SortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        bag.add("a");

        assertFalse(bag.containsAll(Arrays.asList("a", "b")));
    }

    // remove

    @Test
    public void removeDecrementsCountAndReturnsTrueWhenPresent() {
        SortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        bag.add("a", 2);

        assertTrue(bag.remove("a"));
        assertEquals(1, bag.getCount("a"));

        assertTrue(bag.remove("a"));
        assertEquals(0, bag.getCount("a"));
        assertFalse(bag.contains("a"));
    }

    @Test
    public void removeReturnsFalseWhenNotPresent() {
        SortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        assertFalse(bag.remove("missing"));
    }

    @Test(expected = NullPointerException.class)
    public void removeNullRejectedByTreeBag() {
        SortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        bag.remove(null);
    }

    // removeAll / retainAll

    @Test
    public void removeAllWithNullIsNoOpAndReturnsFalse() {
        SortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        bag.add("a");

        assertFalse(bag.removeAll(null));
        assertEquals(1, bag.size());
    }

    @Test
    public void removeAllRemovesGivenElements() {
        SortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        bag.add("a", 2);
        bag.add("b");

        boolean changed = bag.removeAll(Arrays.asList("a", "c"));
        assertTrue(changed);
        assertEquals(0, bag.getCount("a"));
        assertEquals(1, bag.getCount("b"));
    }

    @Test(expected = NullPointerException.class)
    public void retainAllWithNullThrowsNPE() {
        SortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        bag.retainAll(null);
    }

    @Test
    public void retainAllKeepsOnlySpecifiedElements() {
        SortedBag<String> bag = new CollectionSortedBag<>(new TreeBag<>());
        bag.add("a", 2);
        bag.add("b");

        boolean changed = bag.retainAll(Collections.singletonList("a"));
        assertTrue(changed);
        assertEquals(2, bag.getCount("a"));
        assertEquals(0, bag.getCount("b"));
    }

    // Behavior with decorated bags

    @Test(expected = UnsupportedOperationException.class)
    public void wrappingUnmodifiableBagDisallowsAdd() {
        SortedBag<String> unmodifiable = UnmodifiableSortedBag.unmodifiableSortedBag(new TreeBag<>());
        SortedBag<String> bag = new CollectionSortedBag<>(unmodifiable);

        bag.add("x");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void wrappingUnmodifiableBagDisallowsAddCount() {
        SortedBag<String> unmodifiable = UnmodifiableSortedBag.unmodifiableSortedBag(new TreeBag<>());
        SortedBag<String> bag = new CollectionSortedBag<>(unmodifiable);

        bag.add("x", 3);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeAllAgainstUnmodifiableUnderlyingBagThrowsUOE() {
        SortedBag<String> unmodifiable = UnmodifiableSortedBag.unmodifiableSortedBag(new TreeBag<>());
        SortedBag<String> bag = new CollectionSortedBag<>(unmodifiable);

        bag.removeAll(Arrays.asList("a", "b"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrappingPredicatedBagPropagatesPredicateViolations() {
        // Predicate accepts only "only"; adding anything else should fail
        SortedBag<String> predicated = PredicatedSortedBag.predicatedSortedBag(
                new TreeBag<>(), EqualPredicate.equalPredicate("only"));
        SortedBag<String> bag = new CollectionSortedBag<>(predicated);

        bag.add("something-else"); // rejected by predicate
    }
}