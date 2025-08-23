package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObjectGraphIterator}.
 * <p>
 * This test suite extends {@link AbstractIteratorTest} to verify the standard
 * {@link Iterator} contract, in addition to testing specific behaviors of the
 * {@code ObjectGraphIterator}.
 * </p>
 */
public class ObjectGraphIteratorTest extends AbstractIteratorTest<Object> {

    // --- Test Fixtures for AbstractIteratorTest ---

    /** Data for the iterator returned by makeObject(), used by AbstractIteratorTest. */
    protected String[] testArray = { "One", "Two", "Three", "Four", "Five", "Six" };

    protected List<String> list1;
    protected List<String> list2;
    protected List<String> list3;
    protected List<Iterator<String>> iteratorList;

    /**
     * Sets up the fixture for the tests inherited from {@link AbstractIteratorTest}.
     * <p>
     * This creates a nested structure of iterators (an iterator of iterators)
     * that {@link ObjectGraphIterator} is expected to flatten into a single sequence.
     * </p>
     */
    @BeforeEach
    public void setUp() {
        list1 = new ArrayList<>();
        list1.add("One");
        list1.add("Two");
        list1.add("Three");
        list2 = new ArrayList<>();
        list2.add("Four");
        list3 = new ArrayList<>();
        list3.add("Five");
        list3.add("Six");
        iteratorList = new ArrayList<>();
        iteratorList.add(list1.iterator());
        iteratorList.add(list2.iterator());
        iteratorList.add(list3.iterator());
    }

    // --- AbstractIteratorTest Implementation ---

    /**
     * Creates a new, non-empty iterator for the superclass tests.
     * This iterator is expected to flatten a list of iterators.
     */
    @Override
    public ObjectGraphIterator<Object> makeObject() {
        // The iteratorList is initialized in setUp() before each test.
        return new ObjectGraphIterator<>(iteratorList.iterator());
    }

    /**
     * Creates a new, empty iterator for the superclass tests.
     */
    @Override
    public ObjectGraphIterator<Object> makeEmptyIterator() {
        final ArrayList<Object> emptyList = new ArrayList<>();
        return new ObjectGraphIterator<>(emptyList.iterator());
    }

    // --- Custom Behavior Tests for ObjectGraphIterator ---

    @Test
    void iteratorWithNullTransformerShouldReturnOnlyRootObject() {
        // Arrange: Create an iterator with a root object but no transformer.
        final Forest forest = new Forest();
        final Iterator<Object> graphIterator = new ObjectGraphIterator<>(forest, null);

        // Assert: The iterator should yield only the root object and then be exhausted.
        assertTrue(graphIterator.hasNext(), "Iterator should have an element.");
        assertSame(forest, graphIterator.next(), "The only element should be the root object.");
        assertFalse(graphIterator.hasNext(), "Iterator should be exhausted after yielding the root.");

        // Assert: Calling next() again throws an exception.
        assertThrows(NoSuchElementException.class, graphIterator::next,
                "Calling next() on an exhausted iterator should throw.");
    }

    // --- Test Fixture: Object Graph Model ---
    // These classes model a hierarchical data structure (Forest -> Tree -> Branch -> Leaf)
    // to test the graph traversal capabilities of ObjectGraphIterator.

    static class Forest {
        List<Tree> trees = new ArrayList<>();
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

    static class Tree {
        List<Branch> branches = new ArrayList<>();
        Branch addBranch() {
            branches.add(new Branch());
            return getBranch(branches.size() - 1);
        }
        Iterator<Branch> branchIterator() {
            return branches.iterator();
        }
        Branch getBranch(final int index) {
            return branches.get(index);
        }
    }

    static class Branch {
        List<Leaf> leaves = new ArrayList<>();
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
     * A transformer that navigates the Forest object graph to find Leaf objects.
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
                return input; // Return the leaf itself as a terminal node
            }
            throw new ClassCastException("Unsupported object type in graph: " + input.getClass().getName());
        }
    }
}