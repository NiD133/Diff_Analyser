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
 * Tests for ObjectGraphIterator.
 * Note: The class name "ObjectGraphIteratorTestTest7" from the original is unconventional.
 * A better name would be "ObjectGraphIteratorTest". The existing setup methods
 * (e.g., setUp, makeObject) are used by the inherited AbstractIteratorTest framework
 * to verify the general iterator contract.
 */
public class ObjectGraphIteratorTestTest7 extends AbstractIteratorTest<Object> {

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

    // Inner classes representing the object graph structure for the test.
    static class Forest {
        List<Tree> trees = new ArrayList<>();
        Tree addTree() {
            trees.add(new Tree());
            return trees.get(trees.size() - 1);
        }
        Iterator<Tree> treeIterator() {
            return trees.iterator();
        }
    }

    static class Tree {
        List<Branch> branches = new ArrayList<>();
        Branch addBranch() {
            branches.add(new Branch());
            return branches.get(branches.size() - 1);
        }
        Iterator<Branch> branchIterator() {
            return branches.iterator();
        }
    }

    static class Branch {
        List<Leaf> leaves = new ArrayList<>();
        Leaf addLeaf() {
            leaves.add(new Leaf());
            return leaves.get(leaves.size() - 1);
        }
        Iterator<Leaf> leafIterator() {
            return leaves.iterator();
        }
    }

    static class Leaf {
        // A simple terminal object in the graph.
    }

    /**
     * A Transformer that navigates the Forest object graph.
     * It returns an iterator for composite objects (Forest, Tree, Branch)
     * and the object itself for terminal objects (Leaf).
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
            throw new ClassCastException("Unsupported object type: " + input.getClass().getSimpleName());
        }
    }

    /**
     * Tests that the iterator correctly traverses a complex, nested object graph
     * using a Transformer to navigate between nodes.
     */
    @Test
    void testIteratorTraversesComplexObjectGraphUsingTransformer() {
        // Arrange
        // Create a complex object graph (a forest) and a corresponding list of the leaves
        // that the iterator is expected to return, in the correct traversal order.
        // The graph includes empty nodes (trees/branches) to ensure they are handled correctly.
        //
        // The graph structure is:
        // Forest
        //  |- Tree 0 (empty)
        //  |- Tree 1
        //  |   |- Branch 1.1 -> Leaf A, Leaf B
        //  |   '- Branch 1.2 -> Leaf C
        //  '- Tree 2
        //      |- Branch 2.1 -> Leaf D
        //      |- Branch 2.2 -> Leaf E
        //      '- Branch 2.3 (empty)
        final Forest forest = new Forest();
        final List<Leaf> expectedLeaves = new ArrayList<>();

        // Tree 0 is empty
        forest.addTree();

        // Tree 1 with its branches and leaves
        final Tree tree1 = forest.addTree();
        final Branch branch1_1 = tree1.addBranch();
        expectedLeaves.add(branch1_1.addLeaf()); // Leaf A
        expectedLeaves.add(branch1_1.addLeaf()); // Leaf B
        final Branch branch1_2 = tree1.addBranch();
        expectedLeaves.add(branch1_2.addLeaf()); // Leaf C

        // Tree 2 with its branches and leaves
        final Tree tree2 = forest.addTree();
        final Branch branch2_1 = tree2.addBranch();
        expectedLeaves.add(branch2_1.addLeaf()); // Leaf D
        final Branch branch2_2 = tree2.addBranch();
        expectedLeaves.add(branch2_2.addLeaf()); // Leaf E
        tree2.addBranch(); // An empty branch

        // Act
        // Create the iterator to traverse the forest and find all leaves.
        final Iterator<Object> iterator = new ObjectGraphIterator<>(forest, new LeafFinder());
        
        // Consume the iterator and collect the results into a list.
        final List<Object> actualLeaves = IteratorUtils.toList(iterator);

        // Assert
        // 1. Verify that the collected list of leaves matches the expected list.
        assertEquals(expectedLeaves, actualLeaves, "The iterator did not return the leaves in the correct order.");

        // 2. Verify that the iterator is exhausted after traversal.
        assertFalse(iterator.hasNext(), "Iterator should be exhausted after consuming all elements.");
        assertThrows(NoSuchElementException.class, iterator::next,
            "Iterator should throw NoSuchElementException when no elements remain.");
    }
}