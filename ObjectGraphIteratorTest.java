package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ObjectGraphIterator. The tests cover both "iterator of iterators" usage
 * and object-graph traversal via a Transformer.
 *
 * The simple domain model below (Forest -> Tree -> Branch -> Leaf) is used to
 * exercise graph traversal behavior.
 */
class ObjectGraphIteratorTest extends AbstractIteratorTest<Object> {

    // ----- Domain model used for graph traversal tests -----

    /** A branch holds leaves. */
    static class Branch {
        final List<Leaf> leaves = new ArrayList<>();

        Leaf addLeaf() {
            leaves.add(new Leaf());
            return getLeaf(leaves.size() - 1);
        }

        Leaf getLeaf(final int index) {
            return leaves.get(index);
        }

        Iterator<Leaf> leafIterator() {
            return leaves.iterator();
        }
    }

    /** A tree holds branches. */
    static class Tree {
        final List<Branch> branches = new ArrayList<>();

        Branch addBranch() {
            branches.add(new Branch());
            return getBranch(branches.size() - 1);
        }

        Branch getBranch(final int index) {
            return branches.get(index);
        }

        Iterator<Branch> branchIterator() {
            return branches.iterator();
        }
    }

    /** A forest holds trees. */
    static class Forest {
        final List<Tree> trees = new ArrayList<>();

        Tree addTree() {
            trees.add(new Tree());
            return getTree(trees.size() - 1);
        }

        Tree getTree(final int index) {
            return trees.get(index);
        }

        Iterator<Tree> treeIterator() {
            return trees.iterator();
        }
    }

    /** A simple leaf with a color, unused except to represent a terminal node. */
    static class Leaf {
        String color;

        String getColor() {
            return color;
        }

        void setColor(final String color) {
            this.color = color;
        }
    }

    /**
     * Transformer that walks a Forest -> Trees -> Branches -> Leaves graph and yields Leaves.
     * - If given a container node, returns its iterator.
     * - If given a Leaf, returns the Leaf itself (a terminal value).
     */
    static class LeafFinder implements Transformer<Object, Object> {
        @Override
        public Object transform(final Object input) {
            if (input instanceof Forest) {
                return ((Forest) input).treeIterator();
            }
            if (input instanceof Tree) {
                return ((Tree) input).branchIterator();
            }
            if (input instanceof Branch) {
                return ((Branch) input).leafIterator();
            }
            if (input instanceof Leaf) {
                return input; // terminal value
            }
            throw new ClassCastException("Unsupported node type: " + (input == null ? "null" : input.getClass()));
        }
    }

    // ----- Shared data for "iterator of iterators" tests -----

    private static final String[] TEST_DATA = {"One", "Two", "Three", "Four", "Five", "Six"};

    protected List<String> list1;
    protected List<String> list2;
    protected List<String> list3;

    @BeforeEach
    public void setUp() {
        initLists();
    }

    private void initLists() {
        list1 = new ArrayList<>();
        list1.add("One");
        list1.add("Two");
        list1.add("Three");

        list2 = new ArrayList<>();
        list2.add("Four");

        list3 = new ArrayList<>();
        list3.add("Five");
        list3.add("Six");
    }

    // ----- AbstractIteratorTest hooks -----

    @Override
    public ObjectGraphIterator<Object> makeEmptyIterator() {
        final List<Object> empty = new ArrayList<>();
        return new ObjectGraphIterator<>(empty.iterator());
    }

    @Override
    public ObjectGraphIterator<Object> makeObject() {
        // Ensure lists are initialized when called outside JUnit lifecycle.
        initLists();
        final List<Iterator<String>> iterators = new ArrayList<>();
        iterators.add(list1.iterator());
        iterators.add(list2.iterator());
        iterators.add(list3.iterator());
        return new ObjectGraphIterator<>(iterators.iterator());
    }

    // ----- Helper methods for clearer assertions -----

    private static void assertExhausted(final Iterator<?> it) {
        assertFalse(it.hasNext(), "Iterator should be exhausted");
        assertThrows(NoSuchElementException.class, it::next, "next() on exhausted iterator should throw");
    }

    private static void assertRemoveIllegalBeforeNext(final Iterator<?> it) {
        assertThrows(IllegalStateException.class, it::remove, "remove() before next() should throw");
    }

    private static void assertIteratesExactly(final Iterator<?> it, final Object... expected) {
        for (final Object value : expected) {
            assertTrue(it.hasNext(), "hasNext() should be true before consuming expected value");
            assertEquals(value, it.next(), "next() should yield expected value");
        }
        assertExhausted(it);
    }

    private Iterator<Object> newIteratorOfIterators(final boolean includeEmptyBetween) {
        final List<Iterator<String>> iterators = new ArrayList<>();
        if (includeEmptyBetween) {
            iterators.add(IteratorUtils.<String>emptyIterator());
        }
        iterators.add(list1.iterator());
        if (includeEmptyBetween) {
            iterators.add(IteratorUtils.<String>emptyIterator());
        }
        iterators.add(list2.iterator());
        if (includeEmptyBetween) {
            iterators.add(IteratorUtils.<String>emptyIterator());
        }
        iterators.add(list3.iterator());
        if (includeEmptyBetween) {
            iterators.add(IteratorUtils.<String>emptyIterator());
        }
        return new ObjectGraphIterator<>(iterators.iterator());
    }

    // ----- Tests: Iterator of Iterators usage -----

    @Test
    void iteratesOverIteratorOfIterators() {
        final List<Iterator<String>> iterators = new ArrayList<>();
        iterators.add(list1.iterator());
        iterators.add(list2.iterator());
        iterators.add(list3.iterator());

        final Iterator<Object> it = new ObjectGraphIterator<>(iterators.iterator(), null);
        assertIteratesExactly(it, (Object[]) TEST_DATA);
    }

    @Test
    void iteratesOverIteratorOfIterators_skippingEmptyIterators() {
        final Iterator<Object> it = newIteratorOfIterators(true);
        assertIteratesExactly(it, (Object[]) TEST_DATA);
    }

    @Test
    void iteratorOnlyConstructor_emptyRootIteratorBehavesAsEmpty() {
        final List<Iterator<Object>> iterators = new ArrayList<>();
        final Iterator<Object> it = new ObjectGraphIterator<>(iterators.iterator());

        assertExhausted(it);
        assertRemoveIllegalBeforeNext(it);
    }

    @Test
    void iteratorOnlyConstructor_simpleIteration() {
        final Iterator<Object> it = newIteratorOfIterators(false);
        assertIteratesExactly(it, (Object[]) TEST_DATA);
    }

    @Test
    void iteratorOnlyConstructor_simpleIteration_withoutCallingHasNext() {
        final Iterator<Object> it = newIteratorOfIterators(false);
        for (final String expected : TEST_DATA) {
            assertEquals(expected, it.next());
        }
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iteratorOnlyConstructor_iterationWithEmptyIterators() {
        final Iterator<Object> it = newIteratorOfIterators(true);
        assertIteratesExactly(it, (Object[]) TEST_DATA);
    }

    @Test
    void iteratorOnlyConstructor_removeDelegatesToCurrentIterator() {
        final List<Iterator<String>> iterators = new ArrayList<>();
        iterators.add(list1.iterator());
        iterators.add(list2.iterator());
        iterators.add(list3.iterator());

        final Iterator<Object> it = new ObjectGraphIterator<>(iterators.iterator());

        // Remove every element as we traverse; each remove should affect the underlying list.
        for (final String expected : TEST_DATA) {
            assertEquals(expected, it.next());
            it.remove();
        }

        assertFalse(it.hasNext());
        assertEquals(0, list1.size());
        assertEquals(0, list2.size());
        assertEquals(0, list3.size());
    }

    // ----- Tests: Root object with/without Transformer -----

    @Test
    void rootWithoutTransformer_yieldsRootAsSingleElement() {
        final Forest forest = new Forest();
        final Iterator<Object> it = new ObjectGraphIterator<>(forest, null);

        assertTrue(it.hasNext());
        assertSame(forest, it.next());
        assertExhausted(it);
    }

    @Test
    void nullRootIsEmpty_andRemoveBeforeNextIsIllegal() {
        final Iterator<Object> it = new ObjectGraphIterator<>(null, null);

        assertExhausted(it);
        assertRemoveIllegalBeforeNext(it);
    }

    // ----- Tests: Graph traversal via LeafFinder Transformer -----

    @Test
    void transformed_singleLeaf() {
        final Forest forest = new Forest();
        final Leaf onlyLeaf = forest.addTree().addBranch().addLeaf();

        final Iterator<Object> it = new ObjectGraphIterator<>(forest, new LeafFinder());
        assertIteratesExactly(it, onlyLeaf);
    }

    @Test
    void transformed_multipleLeaves_acrossVariousSubtrees_case1() {
        final Forest forest = new Forest();
        forest.addTree();
        forest.addTree();
        forest.addTree();

        final Branch b1 = forest.getTree(0).addBranch();
        final Branch b2 = forest.getTree(0).addBranch();

        final Branch b3 = forest.getTree(2).addBranch();
        forest.getTree(2).addBranch();          // unused branch in this test
        final Branch b5 = forest.getTree(2).addBranch();

        final Leaf l1 = b1.addLeaf();
        final Leaf l2 = b1.addLeaf();
        final Leaf l3 = b2.addLeaf();
        final Leaf l4 = b3.addLeaf();
        final Leaf l5 = b5.addLeaf();

        final Iterator<Object> it = new ObjectGraphIterator<>(forest, new LeafFinder());
        assertIteratesExactly(it, l1, l2, l3, l4, l5);
    }

    @Test
    void transformed_multipleLeaves_acrossVariousSubtrees_case2() {
        final Forest forest = new Forest();
        forest.addTree();
        forest.addTree();
        forest.addTree();

        final Branch b1 = forest.getTree(1).addBranch();
        final Branch b2 = forest.getTree(1).addBranch();
        final Branch b3 = forest.getTree(2).addBranch();
        final Branch b4 = forest.getTree(2).addBranch();
        forest.getTree(2).addBranch();          // unused branch in this test

        final Leaf l1 = b1.addLeaf();
        final Leaf l2 = b1.addLeaf();
        final Leaf l3 = b2.addLeaf();
        final Leaf l4 = b3.addLeaf();
        final Leaf l5 = b4.addLeaf();

        final Iterator<Object> it = new ObjectGraphIterator<>(forest, new LeafFinder());
        assertIteratesExactly(it, l1, l2, l3, l4, l5);
    }

    // ----- Tests: Iterator constructed with null -----

    @Test
    void iteratorConstructor_null_nextThrows() {
        final Iterator<Object> it = new ObjectGraphIterator<>(null);
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void iteratorConstructor_null_removeBeforeNextThrows() {
        final Iterator<Object> it = new ObjectGraphIterator<>(null);
        assertRemoveIllegalBeforeNext(it);
    }

    @Test
    void iteratorConstructor_null_behavesAsEmpty() {
        final Iterator<Object> it = new ObjectGraphIterator<>(null);
        assertExhausted(it);
        assertRemoveIllegalBeforeNext(it);
    }
}