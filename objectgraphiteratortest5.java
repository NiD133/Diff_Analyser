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
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ObjectGraphIterator} focusing on its ability to traverse
 * a tree-like object structure using a {@link Transformer}.
 */
public class ObjectGraphIteratorTest extends AbstractIteratorTest<Object> {

    // --- AbstractIteratorTest Implementation ---

    @Override
    public ObjectGraphIterator<Object> makeEmptyIterator() {
        final ArrayList<Object> emptyList = new ArrayList<>();
        return new ObjectGraphIterator<>(emptyList.iterator());
    }

    /**
     * Creates a non-empty iterator for the abstract test suite.
     * This iterator traverses a simple, pre-defined object graph.
     */
    @Override
    public ObjectGraphIterator<Object> makeObject() {
        final Forest forest = new Forest();
        forest.addTree().addBranch().addLeaf(); // A simple graph with one leaf
        return new ObjectGraphIterator<>(forest, new LeafFinder());
    }

    // --- Test-Specific Helper Classes for Graph Simulation ---

    /** A mock object representing the root of a graph. */
    private static class Forest {
        private final List<Tree> trees = new ArrayList<>();
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
    private static class Tree {
        private final List<Branch> branches = new ArrayList<>();
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
    private static class Branch {
        private final List<Leaf> leaves = new ArrayList<>();
        Leaf addLeaf() {
            final Leaf leaf = new Leaf();
            leaves.add(leaf);
            return leaf;
        }
        Iterator<Leaf> leafIterator() {
            return leaves.iterator();
        }
    }

    /** A mock object representing a terminal leaf node in the graph. */
    private static class Leaf {
        // A simple terminal object with no children.
    }

    /**
     * A Transformer that navigates the Forest -> Tree -> Branch -> Leaf object graph.
     * It returns an iterator for container nodes (Forest, Tree, Branch) and the
     * node itself for terminal nodes (Leaf), which stops the traversal down that path.
     */
    private static class LeafFinder implements Transformer<Object, Object> {
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
                return input; // Return the leaf itself as a final value
            }
            throw new ClassCastException("Unsupported object type in graph: " + input.getClass().getName());
        }
    }

    // --- Test Cases ---

    /**
     * Verifies that the iterator can correctly traverse a simple graph
     * with a single path to one terminal leaf node.
     */
    @Test
    void testIteratorWithTransformer_traversesSinglePathGraph() {
        // Arrange: Set up a simple object graph: Forest -> Tree -> Branch -> Leaf
        final Forest forest = new Forest();
        final Leaf expectedLeaf = forest.addTree().addBranch().addLeaf();
        final Transformer<Object, Object> transformer = new LeafFinder();

        // Act: Create the iterator for the graph
        final Iterator<Object> graphIterator = new ObjectGraphIterator<>(forest, transformer);

        // Assert: Verify the iterator behaves as expected for a single-item iteration
        assertTrue(graphIterator.hasNext(), "Iterator should have an element before the first call to next()");

        final Object actualLeaf = graphIterator.next();
        assertSame(expectedLeaf, actualLeaf, "The first element should be the created leaf");

        assertFalse(graphIterator.hasNext(), "Iterator should be exhausted after retrieving the single element");

        assertThrows(NoSuchElementException.class, graphIterator::next,
                     "Calling next() on an exhausted iterator should throw an exception");
    }
}