package org.apache.commons.collections4.iterators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests the ObjectGraphIterator, focusing on the constructor that accepts a root iterator.
 * The original class name "ObjectGraphIteratorTestTest12" was not descriptive.
 */
@DisplayName("ObjectGraphIterator Constructor Test")
public class ObjectGraphIteratorConstructorTest extends AbstractIteratorTest<Object> {

    // testArray is used by the inherited tests from AbstractIteratorTest.
    protected final String[] testArray = { "One", "Two", "Three", "Four", "Five", "Six" };

    private List<String> list1;
    private List<String> list2;
    private List<String> list3;
    private List<Iterator<String>> iteratorList;

    @Override
    public ObjectGraphIterator<Object> makeEmptyIterator() {
        final ArrayList<Object> emptyList = new ArrayList<>();
        return new ObjectGraphIterator<>(emptyList.iterator());
    }

    /**
     * Creates an ObjectGraphIterator for the abstract tests.
     * The iterator is created from a root iterator that itself contains other iterators.
     * The @BeforeEach setUp method is called by JUnit before this method.
     */
    @Override
    public ObjectGraphIterator<Object> makeObject() {
        return new ObjectGraphIterator<>(iteratorList.iterator());
    }

    @BeforeEach
    public void setUp() {
        // Use mutable ArrayLists as the underlying collections to ensure
        // that iterator.remove() can be tested by other tests in the suite.
        list1 = new ArrayList<>(List.of("One", "Two", "Three"));
        list2 = new ArrayList<>(List.of("Four"));
        list3 = new ArrayList<>(List.of("Five", "Six"));

        // This setup creates a list of iterators, which will be used as the root
        // for the ObjectGraphIterator. The iterator should flatten this structure.
        iteratorList = List.of(list1.iterator(), list2.iterator(), list3.iterator());
    }

    // The following nested classes (Forest, Tree, Branch, Leaf) and the LeafFinder transformer
    // are part of a larger test fixture for ObjectGraphIterator. They model a complex object graph
    // and are used to test the graph traversal capabilities with a Transformer, which is not
    // the focus of the specific test case below but is kept for context and potential other tests.

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
    @DisplayName("should iterate through all elements when constructed with an iterator of iterators")
    void shouldIterateWhenConstructedWithIteratorOfIterators() {
        // Arrange
        // The `iteratorList` is prepared in the setUp() method. It represents an
        // iterator containing three other iterators.
        final List<String> expectedElements = List.of(testArray);
        final Iterator<Object> iterator = new ObjectGraphIterator<>(iteratorList.iterator());

        // Act & Assert
        // Iterate through all expected elements and verify them one by one.
        for (final String expected : expectedElements) {
            assertTrue(iterator.hasNext(), "Iterator should have more elements but hasNext() returned false.");
            assertEquals(expected, iterator.next(), "Iterator returned an unexpected element.");
        }

        // Assert that the iterator is fully exhausted after the loop.
        assertFalse(iterator.hasNext(), "Iterator should be exhausted but hasNext() returned true.");
        assertThrows(NoSuchElementException.class, iterator::next,
                "Calling next() on an exhausted iterator should throw NoSuchElementException.");
    }
}