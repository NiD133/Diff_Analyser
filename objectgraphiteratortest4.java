package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the {@link ObjectGraphIterator}, including edge cases and compliance
 * with the Iterator contract through {@link AbstractIteratorTest}.
 */
public class ObjectGraphIteratorTest extends AbstractIteratorTest<Object> {

    protected String[] testArray = { "One", "Two", "Three", "Four", "Five", "Six" };

    protected List<String> list1;
    protected List<String> list2;
    protected List<String> list3;
    protected List<Iterator<String>> iteratorList;

    @Override
    public ObjectGraphIterator<Object> makeEmptyIterator() {
        final ArrayList<Object> list = new ArrayList<>();
        return new ObjectGraphIterator<>(list.iterator());
    }

    /**
     * Creates an ObjectGraphIterator that iterates over a series of nested iterators.
     * This is used by the tests inherited from {@link AbstractIteratorTest}.
     */
    @Override
    public ObjectGraphIterator<Object> makeObject() {
        setUp(); // setUp is called by makeObject() in the test framework.
        return new ObjectGraphIterator<>(iteratorList.iterator());
    }

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

    // --- Test Fixture: Object Graph Structure ---
    // These classes model a tree-like structure (Forest -> Tree -> Branch -> Leaf)
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
                return input;
            }
            throw new ClassCastException();
        }
    }

    /**
     * Tests that creating an ObjectGraphIterator with a null root results
     * in an empty iterator that behaves correctly according to the Iterator contract.
     */
    @Test
    void testConstructorWithNullRootCreatesEmptyIterator() {
        // Arrange: Create an iterator with a null root object and null transformer.
        final Iterator<Object> iterator = new ObjectGraphIterator<>(null, null);

        // Assert: The iterator should behave as if it's empty.
        assertFalse(iterator.hasNext(), "Iterator with a null root should not have a next element.");

        assertThrows(NoSuchElementException.class, iterator::next,
                "Calling next() on an exhausted iterator should throw NoSuchElementException.");

        assertThrows(IllegalStateException.class, iterator::remove,
                "Calling remove() before a successful next() call should throw IllegalStateException.");
    }
}