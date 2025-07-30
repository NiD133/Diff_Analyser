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
 * Unit tests for ObjectGraphIterator.
 */
class ObjectGraphIteratorTest extends AbstractIteratorTest<Object> {

    // Helper classes to build a tree-like structure
    static class Branch {
        List<Leaf> leaves = new ArrayList<>();

        Leaf addLeaf() {
            Leaf leaf = new Leaf();
            leaves.add(leaf);
            return leaf;
        }

        Iterator<Leaf> leafIterator() {
            return leaves.iterator();
        }
    }

    static class Forest {
        List<Tree> trees = new ArrayList<>();

        Tree addTree() {
            Tree tree = new Tree();
            trees.add(tree);
            return tree;
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

        void setColor(String color) {
            this.color = color;
        }
    }

    static class LeafFinder implements Transformer<Object, Object> {
        @Override
        public Object transform(Object input) {
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
            throw new ClassCastException("Unsupported object type");
        }
    }

    static class Tree {
        List<Branch> branches = new ArrayList<>();

        Branch addBranch() {
            Branch branch = new Branch();
            branches.add(branch);
            return branch;
        }

        Iterator<Branch> branchIterator() {
            return branches.iterator();
        }
    }

    private String[] expectedElements = {"One", "Two", "Three", "Four", "Five", "Six"};
    private List<String> list1;
    private List<String> list2;
    private List<String> list3;
    private List<Iterator<String>> iteratorList;

    @Override
    public ObjectGraphIterator<Object> makeEmptyIterator() {
        return new ObjectGraphIterator<>(new ArrayList<>().iterator());
    }

    @Override
    public ObjectGraphIterator<Object> makeObject() {
        setUp();
        return new ObjectGraphIterator<>(iteratorList.iterator());
    }

    @BeforeEach
    public void setUp() {
        list1 = List.of("One", "Two", "Three");
        list2 = List.of("Four");
        list3 = List.of("Five", "Six");
        iteratorList = List.of(list1.iterator(), list2.iterator(), list3.iterator());
    }

    @Test
    void testIterationOverIteratorOfIterators() {
        Iterator<Object> iterator = new ObjectGraphIterator<>(iteratorList.iterator(), null);

        for (String expected : expectedElements) {
            assertTrue(iterator.hasNext());
            assertEquals(expected, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIterationWithEmptyIterators() {
        List<Iterator<String>> iteratorsWithEmpty = List.of(
            IteratorUtils.emptyIterator(),
            list1.iterator(),
            IteratorUtils.emptyIterator(),
            list2.iterator(),
            IteratorUtils.emptyIterator(),
            list3.iterator(),
            IteratorUtils.emptyIterator()
        );

        Iterator<Object> iterator = new ObjectGraphIterator<>(iteratorsWithEmpty.iterator(), null);

        for (String expected : expectedElements) {
            assertTrue(iterator.hasNext());
            assertEquals(expected, iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIterationWithRootNoTransformer() {
        Forest forest = new Forest();
        Iterator<Object> iterator = new ObjectGraphIterator<>(forest, null);

        assertTrue(iterator.hasNext());
        assertSame(forest, iterator.next());
        assertFalse(iterator.hasNext());

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testIterationWithNullRoot() {
        Iterator<Object> iterator = new ObjectGraphIterator<>(null, null);

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
        assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    void testTransformedIteration() {
        Forest forest = new Forest();
        Leaf leaf1 = forest.addTree().addBranch().addLeaf();
        Iterator<Object> iterator = new ObjectGraphIterator<>(forest, new LeafFinder());

        assertTrue(iterator.hasNext());
        assertSame(leaf1, iterator.next());
        assertFalse(iterator.hasNext());

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testComplexTransformedIteration() {
        Forest forest = new Forest();
        forest.addTree();
        forest.addTree();
        forest.addTree();
        Branch branch1 = forest.getTree(0).addBranch();
        Branch branch2 = forest.getTree(0).addBranch();
        Branch branch3 = forest.getTree(2).addBranch();
        Branch branch4 = forest.getTree(2).addBranch();
        Branch branch5 = forest.getTree(2).addBranch();
        Leaf leaf1 = branch1.addLeaf();
        Leaf leaf2 = branch1.addLeaf();
        Leaf leaf3 = branch2.addLeaf();
        Leaf leaf4 = branch3.addLeaf();
        Leaf leaf5 = branch5.addLeaf();

        Iterator<Object> iterator = new ObjectGraphIterator<>(forest, new LeafFinder());

        assertTrue(iterator.hasNext());
        assertSame(leaf1, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(leaf2, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(leaf3, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(leaf4, iterator.next());
        assertTrue(iterator.hasNext());
        assertSame(leaf5, iterator.next());
        assertFalse(iterator.hasNext());

        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testIteratorConstructorWithNull() {
        Iterator<Object> iterator = new ObjectGraphIterator<>(null);
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
        assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    void testEmptyIteratorConstructor() {
        List<Iterator<Object>> emptyIteratorList = new ArrayList<>();
        Iterator<Object> iterator = new ObjectGraphIterator<>(emptyIteratorList.iterator());

        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
        assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    void testSimpleIteratorConstructor() {
        Iterator<Object> iterator = new ObjectGraphIterator<>(iteratorList.iterator());

        for (String expected : expectedElements) {
            assertTrue(iterator.hasNext());
            assertEquals(expected, iterator.next());
        }
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testSimpleIteratorWithoutHasNext() {
        Iterator<Object> iterator = new ObjectGraphIterator<>(iteratorList.iterator());

        for (String expected : expectedElements) {
            assertEquals(expected, iterator.next());
        }
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testIteratorWithEmptyIterators() {
        List<Iterator<String>> iteratorsWithEmpty = List.of(
            IteratorUtils.emptyIterator(),
            list1.iterator(),
            IteratorUtils.emptyIterator(),
            list2.iterator(),
            IteratorUtils.emptyIterator(),
            list3.iterator(),
            IteratorUtils.emptyIterator()
        );

        Iterator<Object> iterator = new ObjectGraphIterator<>(iteratorsWithEmpty.iterator());

        for (String expected : expectedElements) {
            assertTrue(iterator.hasNext());
            assertEquals(expected, iterator.next());
        }
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testIteratorRemove() {
        Iterator<Object> iterator = new ObjectGraphIterator<>(iteratorList.iterator());

        for (String ignored : expectedElements) {
            assertEquals(ignored, iterator.next());
            iterator.remove();
        }
        assertFalse(iterator.hasNext());
        assertEquals(0, list1.size());
        assertEquals(0, list2.size());
        assertEquals(0, list3.size());
    }
}