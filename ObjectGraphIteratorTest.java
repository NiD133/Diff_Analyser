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
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.Transformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for ObjectGraphIterator which can traverse multiple iterators down an object graph.
 * 
 * The ObjectGraphIterator can work in two modes:
 * 1. Iterator of iterators - flattening nested iterators into a single sequence
 * 2. Object graph traversal - using a transformer to navigate complex object hierarchies
 */
class ObjectGraphIteratorTest extends AbstractIteratorTest<Object> {

    // Test data for iterator-of-iterators scenarios
    private static final String[] EXPECTED_SEQUENCE = {"One", "Two", "Three", "Four", "Five", "Six"};
    
    private List<String> firstGroup;   // ["One", "Two", "Three"]
    private List<String> secondGroup;  // ["Four"]
    private List<String> thirdGroup;   // ["Five", "Six"]
    private List<Iterator<String>> nestedIterators;

    @BeforeEach
    void setupTestData() {
        firstGroup = Arrays.asList("One", "Two", "Three");
        secondGroup = Arrays.asList("Four");
        thirdGroup = Arrays.asList("Five", "Six");
        
        nestedIterators = Arrays.asList(
            firstGroup.iterator(),
            secondGroup.iterator(), 
            thirdGroup.iterator()
        );
    }

    @Override
    public ObjectGraphIterator<Object> makeEmptyIterator() {
        return new ObjectGraphIterator<>(new ArrayList<>().iterator());
    }

    @Override
    public ObjectGraphIterator<Object> makeObject() {
        return new ObjectGraphIterator<>(nestedIterators.iterator());
    }

    // ========== Iterator of Iterators Tests ==========
    
    @Test
    void shouldFlattenNestedIteratorsIntoSingleSequence() {
        ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(nestedIterators.iterator());
        
        assertIteratorProducesExpectedSequence(iterator, EXPECTED_SEQUENCE);
    }

    @Test
    void shouldSkipEmptyIteratorsWhenFlattening() {
        List<Iterator<String>> iteratorsWithEmpties = Arrays.asList(
            IteratorUtils.emptyIterator(),
            firstGroup.iterator(),
            IteratorUtils.emptyIterator(),
            secondGroup.iterator(),
            IteratorUtils.emptyIterator(),
            thirdGroup.iterator(),
            IteratorUtils.emptyIterator()
        );
        
        ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(iteratorsWithEmpties.iterator());
        
        assertIteratorProducesExpectedSequence(iterator, EXPECTED_SEQUENCE);
    }

    @Test
    void shouldSupportRemovalFromUnderlyingCollections() {
        // Use mutable lists for this test
        List<String> mutableFirst = new ArrayList<>(firstGroup);
        List<String> mutableSecond = new ArrayList<>(secondGroup);
        List<String> mutableThird = new ArrayList<>(thirdGroup);
        
        List<Iterator<String>> mutableIterators = Arrays.asList(
            mutableFirst.iterator(),
            mutableSecond.iterator(),
            mutableThird.iterator()
        );
        
        ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(mutableIterators.iterator());
        
        // Remove all elements while iterating
        for (int i = 0; i < EXPECTED_SEQUENCE.length; i++) {
            assertEquals(EXPECTED_SEQUENCE[i], iterator.next());
            iterator.remove();
        }
        
        // Verify all underlying collections are now empty
        assertEquals(0, mutableFirst.size());
        assertEquals(0, mutableSecond.size());
        assertEquals(0, mutableThird.size());
    }

    @Test
    void shouldWorkWithoutCallingHasNext() {
        ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(nestedIterators.iterator());
        
        // Call next() directly without hasNext() checks
        for (String expected : EXPECTED_SEQUENCE) {
            assertEquals(expected, iterator.next());
        }
        
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    // ========== Object Graph Traversal Tests ==========
    
    @Test
    void shouldReturnSingleObjectWhenNoTransformerProvided() {
        Forest forest = new Forest();
        ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(forest, null);
        
        assertTrue(iterator.hasNext());
        assertSame(forest, iterator.next());
        assertFalse(iterator.hasNext());
        
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void shouldTraverseSimpleObjectGraphWithTransformer() {
        // Create: Forest -> Tree -> Branch -> Leaf
        Forest forest = new Forest();
        Leaf expectedLeaf = forest.addTree().addBranch().addLeaf();
        
        ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(forest, new LeafFinder());
        
        assertTrue(iterator.hasNext());
        assertSame(expectedLeaf, iterator.next());
        assertFalse(iterator.hasNext());
        
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void shouldTraverseComplexObjectGraphWithMultipleLeaves() {
        Forest forest = createComplexForest();
        List<Leaf> expectedLeaves = extractAllLeavesFromForest(forest);
        
        ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(forest, new LeafFinder());
        
        for (Leaf expectedLeaf : expectedLeaves) {
            assertTrue(iterator.hasNext(), "Should have more leaves");
            assertSame(expectedLeaf, iterator.next());
        }
        
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void shouldTraverseObjectGraphWithEmptyBranches() {
        Forest forest = createForestWithEmptyBranches();
        List<Leaf> expectedLeaves = extractAllLeavesFromForest(forest);
        
        ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(forest, new LeafFinder());
        
        for (Leaf expectedLeaf : expectedLeaves) {
            assertTrue(iterator.hasNext(), "Should have more leaves");
            assertSame(expectedLeaf, iterator.next());
        }
        
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    // ========== Edge Cases and Error Conditions ==========
    
    @Test
    void shouldHandleNullRootGracefully() {
        ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(null, null);
        
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
        assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    void shouldHandleEmptyIteratorList() {
        List<Iterator<Object>> emptyList = new ArrayList<>();
        ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(emptyList.iterator());
        
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
        assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    void shouldThrowExceptionWhenRemoveCalledBeforeNext() {
        ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(null);
        assertThrows(IllegalStateException.class, iterator::remove);
    }

    @Test
    void shouldThrowExceptionWhenNextCalledOnEmptyIterator() {
        ObjectGraphIterator<Object> iterator = new ObjectGraphIterator<>(null);
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    // ========== Helper Methods ==========
    
    private void assertIteratorProducesExpectedSequence(Iterator<Object> iterator, String[] expected) {
        for (String expectedValue : expected) {
            assertTrue(iterator.hasNext(), "Iterator should have next element: " + expectedValue);
            assertEquals(expectedValue, iterator.next());
        }
        assertFalse(iterator.hasNext(), "Iterator should be exhausted");
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    /**
     * Creates a complex forest structure:
     * Forest with 3 trees, where:
     * - Tree 0: 2 branches with leaves
     * - Tree 1: empty  
     * - Tree 2: 3 branches, some with leaves
     */
    private Forest createComplexForest() {
        Forest forest = new Forest();
        
        // Tree 0: 2 branches with leaves
        Tree tree0 = forest.addTree();
        tree0.addBranch().addLeaf(); // leaf 1
        tree0.getBranch(0).addLeaf(); // leaf 2
        tree0.addBranch().addLeaf(); // leaf 3
        
        // Tree 1: empty
        forest.addTree();
        
        // Tree 2: 3 branches with varying leaves
        Tree tree2 = forest.addTree();
        tree2.addBranch().addLeaf(); // leaf 4
        tree2.addBranch(); // empty branch
        tree2.addBranch().addLeaf(); // leaf 5
        
        return forest;
    }

    /**
     * Creates a forest with some empty branches to test skipping behavior
     */
    private Forest createForestWithEmptyBranches() {
        Forest forest = new Forest();
        
        // Tree 1: mix of empty and populated branches
        Tree tree1 = forest.addTree();
        tree1.addBranch(); // empty branch
        tree1.addBranch().addLeaf(); // branch with leaf 1
        tree1.getBranch(1).addLeaf(); // add leaf 2 to same branch
        tree1.addBranch().addLeaf(); // branch with leaf 3
        tree1.addBranch(); // another empty branch
        
        // Tree 2: mix of empty and populated branches  
        Tree tree2 = forest.addTree();
        tree2.addBranch().addLeaf(); // leaf 4
        tree2.addBranch().addLeaf(); // leaf 5
        
        return forest;
    }

    private List<Leaf> extractAllLeavesFromForest(Forest forest) {
        List<Leaf> leaves = new ArrayList<>();
        for (Tree tree : forest.trees) {
            for (Branch branch : tree.branches) {
                leaves.addAll(branch.leaves);
            }
        }
        return leaves;
    }

    // ========== Test Domain Objects ==========
    
    /**
     * Represents a forest containing multiple trees
     */
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

    /**
     * Represents a tree containing multiple branches
     */
    static class Tree {
        List<Branch> branches = new ArrayList<>();

        Branch addBranch() {
            branches.add(new Branch());
            return branches.get(branches.size() - 1);
        }

        Branch getBranch(int index) {
            return branches.get(index);
        }

        Iterator<Branch> branchIterator() {
            return branches.iterator();
        }
    }

    /**
     * Represents a branch containing multiple leaves
     */
    static class Branch {
        List<Leaf> leaves = new ArrayList<>();

        Leaf addLeaf() {
            leaves.add(new Leaf());
            return leaves.get(leaves.size() - 1);
        }

        Leaf getLeaf(int index) {
            return leaves.get(index);
        }

        Iterator<Leaf> leafIterator() {
            return leaves.iterator();
        }
    }

    /**
     * Represents a leaf node with optional color property
     */
    static class Leaf {
        String color;

        String getColor() {
            return color;
        }

        void setColor(String color) {
            this.color = color;
        }
    }

    /**
     * Transformer that navigates the Forest->Tree->Branch->Leaf hierarchy,
     * returning iterators for container objects and the leaf objects themselves.
     */
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
                return input; // Terminal node - return the leaf itself
            }
            throw new ClassCastException("Unexpected object type: " + input.getClass());
        }
    }
}