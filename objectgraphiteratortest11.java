package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObjectGraphIterator}.
 * <p>
 * This class extends {@link AbstractIteratorTest} to leverage a suite of
 * standard iterator tests. The setup code involving lists, arrays, and the
 * nested Forest/Tree/Branch/Leaf classes is primarily for satisfying the
 * requirements of the abstract test base and for more complex graph traversal scenarios
 * tested in other methods.
 * </p>
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

    @Override
    public ObjectGraphIterator<Object> makeObject() {
        setUp();
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

    // --- Helper classes for graph traversal tests ---

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

    static class Leaf {
        String color;
        String getColor() {
            return color;
        }
        void setColor(final String color) {
            this.color = color;
        }
    }

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

    @Test
    @DisplayName("An ObjectGraphIterator initialized with an empty root iterator should be empty")
    void whenCreatedWithEmptyRootIterator_thenIteratorIsEmpty() {
        // Arrange: Create a root iterator that is itself empty.
        // This simulates providing an empty collection of iterators to the graph iterator.
        final Iterator<Iterator<Object>> emptyRootIterator = Collections.emptyIterator();

        // Act: Construct the ObjectGraphIterator with the empty root iterator.
        final Iterator<Object> graphIterator = new ObjectGraphIterator<>(emptyRootIterator);

        // Assert: The resulting iterator should behave as an empty iterator.
        assertFalse(graphIterator.hasNext(), "hasNext() should return false for an empty iterator.");

        assertThrows(NoSuchElementException.class, graphIterator::next,
                "next() should throw NoSuchElementException for an empty iterator.");

        assertThrows(IllegalStateException.class, graphIterator::remove,
                "remove() before next() should throw IllegalStateException.");
    }
}