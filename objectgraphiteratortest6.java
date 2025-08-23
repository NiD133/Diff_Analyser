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
import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests the ObjectGraphIterator with a complex, tree-like object structure.
 * This class verifies that the iterator can correctly navigate a graph
 * containing nested iterators, empty branches, and multiple levels of depth.
 */
public class ObjectGraphIteratorComplexGraphTest extends AbstractIteratorTest<Object> {

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

    // --- Test domain objects for graph traversal ---

    /** A mock object representing the root of the graph. */
    static class Forest {
        final List<Tree> trees = new ArrayList<>();
        Tree addTree() {
            final Tree tree = new Tree();
            trees.add(tree);
            return tree;
        }
        Iterator<Tree> treeIterator() {
            return trees.iterator();
        }
    }

    /** A mock object representing a node in the graph. */
    static class Tree {
        final List<Branch> branches = new ArrayList<>();
        Branch addBranch() {
            final Branch branch = new Branch();
            branches.add(branch);
            return branch;
        }
        Iterator<Branch> branchIterator() {
            return branches.iterator();
        }
    }

    /** A mock object representing a sub-node in the graph. */
    static class Branch {
        final List<Leaf> leaves = new ArrayList<>();
        Leaf addLeaf() {
            final Leaf leaf = new Leaf();
            leaves.add(leaf);
            return leaf;
        }
        Iterator<Leaf> leafIterator() {
            return leaves.iterator();
        }
    }

    /** A mock object representing a terminal node (a leaf) in the graph. */
    static class Leaf {
        // No behavior needed for this test
    }

    /**
     * A Transformer that navigates the object graph.
     * It returns an iterator for container nodes (Forest, Tree, Branch)
     * and the node itself for terminal nodes (Leaf).
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
                return input; // Return the leaf itself
            }
            throw new ClassCastException("Unsupported object type: " + input.getClass().getName());
        }
    }

    /** A helper class to hold the constructed graph and the expected results. */
    private static class TestGraph {
        final Forest forest;
        final List<Leaf> expectedLeaves;

        TestGraph(final Forest forest, final List<Leaf> expectedLeaves) {
            this.forest = forest;
            this.expectedLeaves = expectedLeaves;
        }
    }

    /**
     * Creates a complex graph structure for testing.
     * The graph contains multiple trees, branches, and leaves, including
     * an empty tree and an empty branch to test edge cases.
     *
     * @return A {@link TestGraph} instance containing the graph root and the expected list of leaves.
     */
    private TestGraph createComplexForestWithExpectedLeaves() {
        final Forest forest = new Forest();
        final List<Leaf> expectedLeaves = new ArrayList<>();

        // Tree 1: Contains two branches with leaves.
        final Tree tree1 = forest.addTree();
        final Branch tree1branch1 = tree1.addBranch();
        expectedLeaves.add(tree1branch1.addLeaf());
        expectedLeaves.add(tree1branch1.addLeaf());
        final Branch tree1branch2 = tree1.addBranch();
        expectedLeaves.add(tree1branch2.addLeaf());

        // Tree 2: An empty tree with no branches. The iterator should skip this.
        forest.addTree();

        // Tree 3: Contains three branches, one of which is empty.
        final Tree tree3 = forest.addTree();
        final Branch tree3branch1 = tree3.addBranch();
        expectedLeaves.add(tree3branch1.addLeaf());

        // This branch has no leaves. The iterator should skip it.
        tree3.addBranch();

        final Branch tree3branch3 = tree3.addBranch();
        expectedLeaves.add(tree3branch3.addLeaf());

        return new TestGraph(forest, expectedLeaves);
    }

    /**
     * Tests that the iterator correctly traverses a complex object graph using a transformer
     * to find all terminal 'Leaf' nodes. It verifies the order of traversal and the
     * iterator's termination behavior.
     */
    @Test
    void testIteratorTraversesComplexGraphWithTransformer() {
        // Arrange: Create a complex object graph and define the expected traversal order.
        final TestGraph testData = createComplexForestWithExpectedLeaves();
        final Forest forest = testData.forest;
        final List<Leaf> expectedLeaves = testData.expectedLeaves;

        // Act: Create the iterator to traverse the graph.
        final Iterator<Object> iterator = new ObjectGraphIterator<>(forest, new LeafFinder());

        // Assert: Verify that the iterator visits all leaves in the expected order.
        for (final Leaf expectedLeaf : expectedLeaves) {
            assertTrue(iterator.hasNext(), "Iterator should have more leaves.");
            assertSame(expectedLeaf, iterator.next(), "Iterator returned an unexpected leaf.");
        }

        // Assert: Verify that the iterator correctly reports that it is exhausted.
        assertFalse(iterator.hasNext(), "Iterator should be exhausted after visiting all leaves.");
        assertThrows(NoSuchElementException.class, iterator::next,
                "Calling next() on an exhausted iterator should throw NoSuchElementException.");
    }
}