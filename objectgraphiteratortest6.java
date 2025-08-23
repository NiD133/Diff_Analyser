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

public class ObjectGraphIteratorTestTest6 extends AbstractIteratorTest<Object> {

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
    void testIteration_Transformed2() {
        final Forest forest = new Forest();
        forest.addTree();
        forest.addTree();
        forest.addTree();
        final Branch b1 = forest.getTree(0).addBranch();
        final Branch b2 = forest.getTree(0).addBranch();
        final Branch b3 = forest.getTree(2).addBranch();
        /* Branch b4 = */
        forest.getTree(2).addBranch();
        final Branch b5 = forest.getTree(2).addBranch();
        final Leaf l1 = b1.addLeaf();
        final Leaf l2 = b1.addLeaf();
        final Leaf l3 = b2.addLeaf();
        final Leaf l4 = b3.addLeaf();
        final Leaf l5 = b5.addLeaf();
        final Iterator<Object> it = new ObjectGraphIterator<>(forest, new LeafFinder());
        assertTrue(it.hasNext());
        assertSame(l1, it.next());
        assertTrue(it.hasNext());
        assertSame(l2, it.next());
        assertTrue(it.hasNext());
        assertSame(l3, it.next());
        assertTrue(it.hasNext());
        assertSame(l4, it.next());
        assertTrue(it.hasNext());
        assertSame(l5, it.next());
        assertFalse(it.hasNext());
        assertThrows(NoSuchElementException.class, () -> it.next());
    }
}
