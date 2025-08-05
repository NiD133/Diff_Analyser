/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * Tests for the {@link ObjectGraphIterator}.
 */
class ObjectGraphIteratorTest extends AbstractIteratorTest<Object> {

    // Domain model classes for tree structure testing
    static class Forest {
        List<Tree> trees = new ArrayList<>();
        Tree addTree() {
            trees.add(new Tree());
            return trees.get(trees.size() - 1);
        }
        Tree getTree(int index) {
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
            return branches.get(branches.size() - 1);
        }
        Iterator<Branch> branchIterator() {
            return branches.iterator();
        }
        Branch getBranch(int index) {
            return branches.get(index);
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
        String color;
        String getColor() {
            return color;
        }
        void setColor(String color) {
            this.color = color;
        }
    }

    // Transformer for tree traversal
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
            throw new ClassCastException();
        }
    }

    // Test data
    private static final String[] TEST_ARRAY = { "One", "Two", "Three", "Four", "Five", "Six" };
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
        list1 = new ArrayList<>(List.of("One", "Two", "Three"));
        list2 = new ArrayList<>(List.of("Four"));
        list3 = new ArrayList<>(List.of("Five", "Six"));
        
        iteratorList = new ArrayList<>();
        iteratorList.add(list1.iterator());
        iteratorList.add(list2.iterator());
        iteratorList.add(list3.iterator());
    }

    // Helper method to create a forest with a single leaf
    private Forest createSingleLeafForest() {
        Forest forest = new Forest();
        forest.addTree().addBranch().addLeaf();
        return forest;
    }

    // Helper method to create a complex forest structure
    private Forest createComplexForest() {
        Forest forest = new Forest();
        
        // Tree 0: Two branches with three leaves
        Tree tree0 = forest.addTree();
        Branch branch0 = tree0.addBranch();
        branch0.addLeaf(); // Leaf 0
        branch0.addLeaf(); // Leaf 1
        Branch branch1 = tree0.addBranch();
        branch1.addLeaf(); // Leaf 2
        
        // Tree 2: Three branches with two leaves
        Tree tree2 = forest.addTree();
        Branch branch2 = tree2.addBranch();
        branch2.addLeaf(); // Leaf 3
        tree2.addBranch(); // Empty branch
        Branch branch4 = tree2.addBranch();
        branch4.addLeaf(); // Leaf 4
        
        return forest;
    }

    // ========================
    // Null Root Tests
    // ========================
    
    @Test
    void whenRootIsNull_ThenHasNextReturnsFalse() {
        Iterator<Object> it = new ObjectGraphIterator<>(null, null);
        assertFalse(it.hasNext());
    }

    @Test
    void whenRootIsNull_ThenNextThrowsException() {
        Iterator<Object> it = new ObjectGraphIterator<>(null, null);
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void whenRootIsNull_ThenRemoveThrowsException() {
        Iterator<Object> it = new ObjectGraphIterator<>(null, null);
        assertThrows(IllegalStateException.class, it::remove);
    }

    @Test
    void whenConstructedWithNullIterator_ThenHasNextReturnsFalse() {
        Iterator<Object> it = new ObjectGraphIterator<>(null);
        assertFalse(it.hasNext());
    }

    // ========================
    // Iterator Chain Tests
    // ========================
    
    @Test
    void whenRootIsIteratorOfIterators_ThenIteratesAllElements() {
        Iterator<Object> it = new ObjectGraphIterator<>(iteratorList.iterator());
        
        for (int i = 0; i < TEST_ARRAY.length; i++) {
            assertTrue(it.hasNext());
            assertEquals(TEST_ARRAY[i], it.next());
        }
        assertFalse(it.hasNext());
    }

    @Test
    void whenRootIsIteratorOfIteratorsWithEmpties_ThenIteratesAllElements() {
        List<Iterator<String>> iterators = new ArrayList<>();
        iterators.add(IteratorUtils.emptyIterator());
        iterators.add(list1.iterator());
        iterators.add(IteratorUtils.emptyIterator());
        iterators.add(list2.iterator());
        iterators.add(IteratorUtils.emptyIterator());
        iterators.add(list3.iterator());
        iterators.add(IteratorUtils.emptyIterator());
        
        Iterator<Object> it = new ObjectGraphIterator<>(iterators.iterator());
        
        for (String expected : TEST_ARRAY) {
            assertTrue(it.hasNext());
            assertEquals(expected, it.next());
        }
        assertFalse(it.hasNext());
    }

    @Test
    void whenCallingNextWithoutHasNext_ThenIteratesAllElements() {
        Iterator<Object> it = new ObjectGraphIterator<>(iteratorList.iterator());
        
        for (String expected : TEST_ARRAY) {
            assertEquals(expected, it.next());
        }
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void whenIteratorChainIsEmpty_ThenHasNextReturnsFalse() {
        Iterator<Object> it = new ObjectGraphIterator<>(new ArrayList<>().iterator());
        assertFalse(it.hasNext());
    }

    @Test
    void whenRemovingElements_ThenUnderlyingCollectionsAreModified() {
        Iterator<Object> it = new ObjectGraphIterator<>(iteratorList.iterator());
        
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        
        assertEquals(0, list1.size());
        assertEquals(0, list2.size());
        assertEquals(0, list3.size());
    }

    // ========================
    // Tree Structure Tests
    // ========================
    
    @Test
    void whenRootIsNotIteratorAndNoTransformer_ThenReturnsRootOnly() {
        Forest forest = new Forest();
        Iterator<Object> it = new ObjectGraphIterator<>(forest, null);
        
        assertTrue(it.hasNext());
        assertSame(forest, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void withTransformer_WhenSingleLeaf_ThenReturnsLeaf() {
        Forest forest = createSingleLeafForest();
        Leaf expectedLeaf = forest.getTree(0).getBranch(0).getLeaf(0);
        
        Iterator<Object> it = new ObjectGraphIterator<>(forest, new LeafFinder());
        
        assertTrue(it.hasNext());
        assertSame(expectedLeaf, it.next());
        assertFalse(it.hasNext());
    }

    @Test
    void withTransformer_WhenComplexForest_ThenReturnsAllLeaves() {
        Forest forest = createComplexForest();
        Iterator<Object> it = new ObjectGraphIterator<>(forest, new LeafFinder());
        
        // Verify all 5 leaves are returned in expected order
        assertTrue(it.hasNext());
        assertSame(forest.getTree(0).getBranch(0).getLeaf(0), it.next());
        assertTrue(it.hasNext());
        assertSame(forest.getTree(0).getBranch(0).getLeaf(1), it.next());
        assertTrue(it.hasNext());
        assertSame(forest.getTree(0).getBranch(1).getLeaf(0), it.next());
        assertTrue(it.hasNext());
        assertSame(forest.getTree(1).getBranch(0).getLeaf(0), it.next());
        assertTrue(it.hasNext());
        assertSame(forest.getTree(1).getBranch(2).getLeaf(0), it.next());
        assertFalse(it.hasNext());
    }
}